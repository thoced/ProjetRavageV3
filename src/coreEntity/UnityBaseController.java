package coreEntity;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import coreAI.ICallBackAStar;
import coreAI.Node;
import coreEntity.Unity.ANIMATE;
import coreEntity.UnityBaseView.TYPE_ANIMATION;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import coreEntityManager.NodeReserved;
import coreEntityManager.ReservationManager;
import coreEvent.IEventCallBack;
import coreLevel.LevelManager;
import coreMessageManager.IPumpMessage;
import coreMessageManager.MessageRavage;
import coreNet.NetBase.TYPE;
import coreNet.NetDataUnity;
import coreNet.NetManager;
import coreNet.NetSendThread;
import corePhysic.PhysicWorldManager;
import coreSounds.SoundsManager;
import coreSounds.SoundsManager.TYPE_SOUNDS;
import ravage.IBaseRavage;

public class UnityBaseController implements IBaseRavage, ICallBackAStar,
		IEventCallBack,IPumpMessage {
	protected UnityBaseView view;

	protected UnityBaseModel model;

	protected Step step = null; // step du chemin

	protected Vec2 vecStep = null; // vecteur de déplacement step

	protected Vec2 dir = null;

	protected Object lock;

	protected ETAPE m_sequencePath = ETAPE.NONE;

	protected Node nodeTake = null; // node pris lors d'un déplacement
	
	protected boolean m_isOnNewNextStep = true;
	
	protected boolean	 m_isLastStep = false;
	
	protected Vec2	  m_nextStep;
	
	protected Vec2	  m_dirEnemy;
	
	protected UnityBaseController m_enemy;
	
	protected static float[] formulaStrike = {1f,0.9f,0.8f,0.7f,0.6f,0.5f,0.4f,0.3f,0.2f,0.1f,0f};
	
	
	public enum ETAPE {
		 MOVE, MOVE_TO_ENEMY,STRIKE,NONE
	};

	public enum TYPEUNITY {
		KNIGHT,BUCHE,PIQUIER,ESCRIME,ARCHER,CATAPULTE
	};

	public UnityBaseController() {
		super();
		lock = new Object();
		// instance de la vue et du model

	}

	public void prepareModelToNet() {
		// prépare le model pour être envoyé au réseau car body et enemy ne sont
		// pas sérialisés
		// this.getModel().setPosition(this.getModel().getBody().getPosition());
		// this.getModel().setRotation(this.getModel().getBody().getAngle());
		/*if (this.getModel().getEnemy() != null)
			this.getModel().setIdEnemy(
					this.getModel().getEnemy().getModel().getId());*/
		this.getModel()
				.setOrigineSprite(this.getView().getSprite().getOrigin());

	}

	public UnityBaseView getView() {
		return view;
	}

	public UnityBaseModel getModel() {
		return model;
	}

	public void setView(UnityBaseView view) {
		this.view = view;
	}

	public void setModel(UnityBaseModel model) {
		if (this.model != null) // si il existe déja un model, il faut supprimer
								// le body
		{
			PhysicWorldManager.getWorld().destroyBody(this.model.getBody());
		}
		this.model = model;
	}

	private boolean checkNodeFree() {
		if (LevelManager.getLevel().getModel()
				.isNodeFree(step.getX(), step.getY(), this) != true) {
			this.getModel().DecrementIndice();
			this.getModel().getBody().setLinearVelocity(new Vec2(0, 0));
			// this.getView().playAnimation(TYPE_ANIMATION.NON);
			// on assigne le nouvelle enemy
			// this.getModel().setEnemy(LevelManager.getLevel().getModel().getUnityOnNode(step.getX(),
			// step.getY()));
			return false; // retourne false car le node est occupé, il faut
							// stopper la progression
		}
		// on libère sur celui où l'on se trouve
		if (nodeTake != null)
			nodeTake.releaseNode(this);
		// on take le node
		nodeTake = LevelManager.getLevel().getModel()
				.takeNode(step.getX(), step.getY(), this);

		return true; // pas de node occupé
	}

		
	// ---------------------------------
	// activation du mode de mouvement
	// - active le mouvement
	// - indique que l'on est sur un prochain step
	// - indique que l'on est plus sur le dernier step
	// - joue l'animation de marche
	// ---------------------------------
	public void move()
	{
		this.setSequence(ETAPE.MOVE);
		m_isOnNewNextStep = true;
		m_isLastStep = false;
		this.getView().playAnimation(TYPE_ANIMATION.WALK);
	}
	
	// ---------------------------------
	// Arret
	// - active l'arret de l'unité
	// - place la velocité à 0
	// - place null dans la liste des chemins
	// - arrête les animations
	// - précise que l'on est sur le dernier step
	// ---------------------------------
	
	public void stop()
	{
		this.getModel().getBody().setLinearVelocity(new Vec2(0f,0f));
		this.getModel().setPaths(null);
		this.setSequence(ETAPE.NONE);
		this.getView().playAnimation(TYPE_ANIMATION.NON);
		m_isLastStep = true;
		
	}
	
	// ---------------------------------
	// Frappe (strike)
	// - active le mode de frappe
	// - joue l'animation de frappe
	// ---------------------------------
	
	public void strike()
	{
		this.setSequence(ETAPE.STRIKE);
		this.getView().playAnimation(TYPE_ANIMATION.STRIKE);
		
	}
	
	// ---------------------------------
	// bouge vers l'enemy
	// - Si un enemy est bien attribué, on vérifie que le chemin en ligne droite ne passe pas par une node noire 
	// - si le chemin est libre, l'unité peut avancer en ligne droite
	// - activation du mode MOTE_TO_ENEMY
	// - on joue l'animaiton de marche
	// - activation du flag motetoenemy dans le model
	// - envoie sur le réseau
	// - si la ligne droite est obstruée par une node noire, on lance une recherche de chemin classique
	// ---------------------------------
	
	public void moveToEnemy()
	{
		// d'abord on regarde si les nodes sont passable (cros))
		if(m_enemy != null)
		{
			if(LevelManager.getLevel().getModel().isNodesCross(this.getModel().getPositionNode(), m_enemy.getModel().getPositionNode()))
			{
				// l'unité peut avancer en ligne droite
				this.setSequence(ETAPE.MOVE_TO_ENEMY);
				this.getView().playAnimation(TYPE_ANIMATION.WALK);
				this.getModel().setMoveToEnemy(true);
				// envoie du code sur le réseau
				// emission sur le réseau
				NetDataUnity data = new NetDataUnity();
				this.prepareModelToNet();
				try
				{
					data.setModel(this.getModel().clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				data.setTypeMessage(TYPE.UPDATE);
				NetSendThread.push(data);
				
			}
			else
			{
				// les nodes ne sont pas traversables, on lance une recherche de chemin classique et on spécifie que l'on avance normalement
				EntityManager.computeDestination(this, m_enemy.getModel().getPosition(), m_enemy.getModel().getPositionNode(), new Vec2(1f,0f));
				
			}
		}
		
		
	}
	
	// ---------------------------------
	// update de mouvement vers l'enemy (methode joué à chaque frame)
	// - si un enemy est bien attribué, 
	// - récupération du vecteur de direction vers l'enemy
	// - normalisation du vecteur
	// - création d'un vecteur de velocité créé à partir du vecteur de direction normalisé
	// - set du vecteur de velocité
	// - calcul constant de la rotation de l'unité, (pointe vers l'enemy)
	// ---------------------------------
	
	public void updateMoveToEnemy()
	{
		// on se déplace en ligne droite
		
		if(m_enemy != null)
		{
			
				// on récupère la direction
				Vec2 dirEnemy = m_enemy.getModel().getPosition().sub(this.getModel().getPosition());
				dirEnemy.normalize();
				Vec2 velocity = dirEnemy.mul(this.getModel().getSpeed());
				this.getModel().getBody().setLinearVelocity(velocity); // on avance
				// modification du sens de marche
				this.computeRotation(dirEnemy);

		}
	}
	
	// ---------------------------------
	// uupdate de mouvement
	// - si l'unité est sur une nouvelle step && qu'elle ne se trouve pas encore sur la dernière step 
	// - récupération du prochain step
	// - placement du flag isOnNewNextStep à false
	// - si non
	// - mouvement vers le step récupéré
	// - si l'exeption LastStepException est lancée, on récupère le dernier step (placement au pixel prêt-
	// - flag m_isLastStep placé à true
	// ---------------------------------
	
	public void updateMove()
	{	
		try
		{
			
			if(m_isOnNewNextStep && !m_isLastStep)
			{
				m_nextStep = this.getNextStep();
				m_isOnNewNextStep = false;
			}
			else
			{
				if(m_nextStep != null)
				{
					Vec2 dirStep = m_nextStep.sub(this.getModel().getPosition());
					dirStep.normalize();
					Vec2 velocity = dirStep.mul(this.getModel().getSpeed());
					this.getModel().getBody().setLinearVelocity(velocity);
					
					// calcul la rotation
					this.computeRotation(dirStep);
					
					if(this.getModel().getPosition().sub(m_nextStep).length() < 0.5f)
					{
						m_isOnNewNextStep = true;
						
					}
				}
					
	
			}
		}
		catch(LastStepException lse)
		{
			m_nextStep = lse.getLastStep();
			m_isLastStep = true;
			this.stop();
		}
	}
	
	// -------------------------------------------------------
	// récupère la prochaine position dans le chemin déterminé
	// retourne un VEC2
	// lance une exception si c'est la dernière destination avec la position finale
	//
	// -------------------------------------------------------
	
	
	private Vec2 getNextStep() throws LastStepException
	{
		if(this.getModel().getPaths() != null)
		{
			if(this.getModel().getIndicePaths() < this.getModel().getPaths().getLength() - 1)
			{
				Step stepPath = this.getModel().getPaths().getStep(this.getModel().getIndicePathsAndIncrement());
				Vec2 step = new Vec2(stepPath.getX(),stepPath.getY());
				step = step.add(new Vec2(0.5f,0.5f));
				return step;
			}
			else
			{
				
				throw new LastStepException(this.getModel().getPositionlFinal());
			}
		}
		
		return null;
		
	}
	
	
	// ---------------------------------
	// update (dispatch)
	// - modification du elpsedAnimationTime utilisé par la vue 
	// - placement d'une vélocité à 0
	// - dispatch du mode
	// - NONE : l'unité s'arrête et pointe dans la direction de formation souhaitée
	// - MOVE : l'unité avance, appel de la methode updateMove
	// - MOVE_TO_ENEMY: l'unité avance en ligne droite vers l'enemy
	// - STRIKE: l'unité se retourne constamment vers l'enemy
	// ---------------------------------
	

	@Override
	public void update(Time deltaTime) {
		// incrémentation du temps écoulé pour les animations
		this.getView().elapsedAnimationTime += deltaTime.asSeconds();
		
		// arret constant de la velocity
		this.getModel().getBody().setLinearVelocity(new Vec2(0f,0f));
		
		switch(m_sequencePath)
		{
			case NONE: 			this.computeRotation(this.getModel().getDirFormation());break;
			
			
			case MOVE: 			this.updateMove();break;
			
			case MOVE_TO_ENEMY: this.updateMoveToEnemy();break;
			
			case STRIKE:	
								if(m_dirEnemy != null)
								{
									Vec2 d = m_dirEnemy;
									d.normalize();
									this.computeRotation(d);
								}
								break;
			
		}
		
		
	}
	// ---------------------------------
	// lerp
	// - calcul lerp
	// ---------------------------------
	
	protected float lerp(float value, float start, float end) {
		return start + (end - start) * value;
	}
	// ---------------------------------
	// computeRotation (calcul de la rotation)
	// - si le vecteur passé n'est pas null
	// - calcul de l'angle de rotation en utilisant la methode lerp
	// - modification de l'angle dans le body
	// ---------------------------------
	protected void computeRotation(Vec2 vec) // calcul la rotation de l'unité
	{
		if (vec != null) {

			// on crée la class de rotation
			Rot r = new Rot();
			r.s = vec.y;
			r.c = vec.x;
			// receptin de l'angle de rotation
			// float angle = r.getAngle();
			// assouplissement en utilisant un lerp
			float angle = lerp(0.2f, this.getModel().getBody().getAngle(),
					r.getAngle()); // angle de la tourelle déterminé

			this.getModel()
					.getBody()
					.setTransform(this.getModel().getBody().getPosition(),
							angle);
		}
	}
	// ---------------------------------
	// onCallsearchPath (réception du chemin déterminé par le ASTAR
	// - placement du vecteur de chemnin
	// - applicatin du mode MOVE
	// - envoie sur le réseau
	// ---------------------------------
	@Override
	public void onCallsearchPath(Path finalPath) {
		// réception du chemin calculé

		synchronized (lock) 
		{
			this.getModel().setKnocking(false); // on place à false la frappe pour indiquer au réseau que le personnage ne frappe plus
			this.getModel().setIndicePaths(0);
			this.getModel().setPaths(finalPath);
			this.move();

			// emission sur le réseau
			NetDataUnity data = new NetDataUnity();
			this.prepareModelToNet();
			try {
				data.setModel(this.getModel().clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setTypeMessage(TYPE.UPDATE);
			NetSendThread.push(data);
		}

	}

	public void hit(int hitStrenght,int powerPenetration,int dexterity,boolean isCriticalStrike) {
		// on est frappé, diminution de l'energie
		
		// algorithme de combat
		
		// calcul de l'esquive
		int valueEsquive = this.getModel().getAgility() - dexterity;
		if(valueEsquive < 0)
			valueEsquive = 0;
		if(valueEsquive > 9)
			valueEsquive = 9;
		float formulaEsquive = formulaStrike[valueEsquive];
		float esquive = formulaEsquive * 100;
		Random randEsquive = new Random();
		int resultEsquive = randEsquive.nextInt(100);
		
		if(resultEsquive < esquive) // il n'y a pas d'esquive car le resultesquive est inférieur au pourcentage de chance de ne pas esquiver
		{
			// il n'y a pas eu d'esquive
		
			int valuepenetration = this.getModel().getArmor() - powerPenetration;
			if(valuepenetration < 0)
				valuepenetration = 0;
			if(valuepenetration > 9)
				valuepenetration = 9;
			float formula =  formulaStrike[valuepenetration];
			// multiplication du hitStrenght
			float damage = formula * hitStrenght;
			// vérification de coup critique, si il y a coup critque alors on multiplie par 3 les domages
			if(isCriticalStrike)
				damage *= 3;
		    
			
			
			this.getModel().setEnergy((int) (this.getModel().getEnergy() - damage));
			
					
			// FIN ALGORITHME DE COMBAT
			// on joue un peu de sang
			BloodManager.addBlood(this.getModel().getPosition());
			// si l'energie est égale à 0 ou inférieur, on meurt
			if (this.getModel().getEnergy() <= 0) // c'est la mort
			{
				// ensutie il faut le code réseau
				this.getModel().setKilled(true);
				NetDataUnity data = new NetDataUnity();
				data.setTypeMessage(TYPE.UPDATE);
				this.prepareModelToNet();
				try {
					data.setModel(this.getModel().clone());
					NetSendThread.push(data);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				// suppresin de l'unité dans le vecteur
				this.destroy(); // suppresion de l'objet dans le monde physique
				// suppresion de l'unité
				EntityManager.getVectorUnity().remove(this.getModel().getId());
				
				// ensutie il faut jouer la mort en view, on place un cadavre
				BloodManager.addUnityKilled(this.getModel().getPosition(), this.getModel().getMyCamp());
				// on joue le son d'un cri de mort
				SoundsManager.PlaySounds(TYPE_SOUNDS.CRY_SOUNDS);
				
			}
		}
	}

	public ETAPE getSequencePath() {
		return m_sequencePath;
	}

	public void setSequence(ETAPE sequencePath)
	{
		m_sequencePath = sequencePath;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {

		// libération du node take
		if (this.nodeTake != null)
			this.nodeTake.releaseNode(this);
		// destruction du body
		if (this.getModel().getBody() != null)
			PhysicWorldManager.getWorld()
					.destroyBody(this.getModel().getBody());

	}

	@Override
	public boolean onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseMove(MouseEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMousePressed(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseReleased(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void OnPumpMessage(MessageRavage message) {
		// TODO Auto-generated method stub
		
		
	}
	
	public class LastStepException extends Exception
	{
		private Vec2 m_lastStep;

		public LastStepException(Vec2 m_lastStep)
		{
			this.m_lastStep = m_lastStep;
		}
		
		public Vec2 getLastStep() {
			return m_lastStep;
		}

		public void setLastStep(Vec2 m_lastStep) {
			this.m_lastStep = m_lastStep;
		}
		
		
	}

	



}

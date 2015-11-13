package coreEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import coreAI.AstarManager;
import coreAI.ICallBackAStar;
import coreAI.Node;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import coreLevel.LevelManager;
import coreNet.NetHeader;
import coreNet.NetHeader.TYPE;

import coreNet.NetManager;

import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public abstract class Unity implements IBaseRavage,ICallBackAStar
{
	// appartenance à un camp
	protected EntityManager.CAMP myCamp = EntityManager.CAMP.YELLOW;
	
	public enum TYPEUNITY {KNIGHT};
	// temps avant téléportation
	protected final static float  TIME_BEFORE_TELEPORTATION = 0.5f;

	// id de l'unité
	protected int id;
	
	// type d'unité
	protected TYPEUNITY idType;
	
	/// position x et y en pixels
	protected float posx,posy;
	
	// 
	protected int tx,ty;
	
	// position target en pixels
	protected float tfx,tfy;  // target en pixels
	
	// position de l'unité sur une node
	protected int   nodex,nodey;
	
	// angle de rotation
	protected float rotation;
	
	// body physic
	protected Body body;
	
	// 
	protected Vec2 targetPosition;
	
	// vecteur direction 
	protected Vec2 vecTarget;
	
	// vecteur direction de formation
	protected Vec2 vecDirFormation; // vecteur de formation finale
	
	// IntRect du sprite d'animation à afficher
	protected IntRect[] animSpriteRect;
	protected IntRect	currentAnim;
	protected int		indAnim; // indice d'animation
	protected int 		indAnimWalk; // indice d'animation pour la marche
	protected Time		timeElapsedAnim = Time.ZERO; // temps entre deux anim
	
	// pathfinal pour le systeme classique
	protected List<Node> pathFinal;
	// pathfinal pour le systeme navmesh
	protected NavPath pathFinalNavMesh;
	protected Path pathFinalPath;
	protected int     indNavMesh = 0;
	protected int     cptNavMesh = 0;
	protected boolean isArrived = false;
	
	protected Clock resetSearchClock;
	protected Time  elapseSearchClock = Time.ZERO;

	protected float elapse = 0f;
	protected int ind = 0;
	
	protected Node next = null;
	
	// is selected
	protected boolean isSelected = false;
	
	protected boolean nextNode = true;
	protected int indexNode = 0;
	protected float mx =0;
	protected float my = 0;
	
	protected boolean isStop =false;
	
	// enum des modes dans lesquels l'unité peut se trouver
    public static enum ANIMATE {WALK,STRIKE,PAUSE,KILL};
	
    // mode dans lequel l'unité se trouve
	protected ANIMATE animate = ANIMATE.PAUSE; 
	
	// ennemy attribué
	protected Unity enemyAttribute;
	protected Time elapsedSearchFollowEnemy = Time.ZERO;
	
	// est on mort ?
	protected boolean isKilled = false;

	private Time elapsedSearchClock;
	
	@Override
	public void init() 
	{
				
	
	}
	

	
	public EntityManager.CAMP getMyCamp() {
		return myCamp;
	}




	public void setMyCamp(EntityManager.CAMP myCamp) {
		this.myCamp = myCamp;
	}


	public Unity getEnemyAttribute() 
	{
		// on vérifie si cette enemy existe toujours dans l'entity mananger (si il a n'a pas été tué)
		if(this.enemyAttribute != null)
		{
			/*if(EntityManager.getVectorUnityNet().contains(this.enemyAttribute))
				return enemyAttribute;
			else
			{
				this.enemyAttribute = null;
			}*/
	
		}
		
		return this.enemyAttribute;
		
	}



	public void setEnemyAttribute(Unity enemyAttribute) {
		this.enemyAttribute = enemyAttribute;
	}



	public TYPEUNITY getIdType() {
		return idType;
	}



	public void setIdType(TYPEUNITY idType) {
		this.idType = idType;
	}



	public IntRect getCurrentAnim()
	{
		return currentAnim;
	}
	
	
	public Vec2 getVecTarget() {
		return vecTarget;
	}



	public void setVecTarget(Vec2 vecTarget) {
		this.vecTarget = vecTarget;
	}



	public void stop()
	{
		this.isStop = true;
	}
	
	
	
	public Vec2 getVecDirFormation() {
		return vecDirFormation;
	}



	public void setVecDirFormation(Vec2 vecDirFormation) {
		this.vecDirFormation = vecDirFormation;
	}



	
	
	public void setLinearVelocity(Vec2 v)
	{
		body.setLinearVelocity(v);
		
	}
	
	public void setPosition(float x,float y)
	{
		if(body != null)
		{
			body.setTransform(new Vec2((float)x + 0.5f,(float)y + 0.5f),0f);
			posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
			posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
			this.tfx = posx;
			this.tfy = posy;
			this.isArrived = true;
		}
		
	}
	
	public float getPositionMeterX()
	{
		return body.getPosition().x;
	}
	
	public float getPositionMeterY()
	{
		return body.getPosition().y;
	}
	
	protected void NetKill(int id)
	{
		// emission sur le réseau que je suis mort
		/*NetHeader header = new NetHeader();
		header.setTypeMessage(TYPE.KILL);
		NetKill kill = new NetKill();
		kill.setId(id);
		header.setMessage(kill);
		// émission
		try
		{
			NetManager.PackMessage(header);
			//NetManager.SendMessage(header);
								
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	protected void NetStrike(int idTarget, int force)
	{
		// test de sang
				//BloodManager.addBlood(this.getPosx(), this.getPosy());
		// emission d'une frappe sur le reseau
	/*	NetHeader header = new NetHeader();
		header.setTypeMessage(TYPE.STRIKE);
		NetStrike strike = new NetStrike();
		strike.setIdStriker(this.getId());
		strike.setIdTarget(idTarget);
		strike.setForce(force);
		header.setMessage(strike);
		// émission
		try
		{
			NetManager.PackMessage(header);
			//NetManager.SendMessage(header);
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	protected void NetSend(float x,float y,float dx,float dy,Vec2 vecDir)
	{
		 /*   NetHeader header = new NetHeader();
			header.setTypeMessage(TYPE.MOVE);
			NetMoveUnity move = new NetMoveUnity();
			move.setId(this.getId());
			move.setPosx(x);
			move.setPosy(y);
			move.setNextPosx(dx);
			move.setNextPosy(dy);
			move.setVecDirFormation(vecDir); // si null alors ce n'est pas encore le message de fin - permet de tourner l'unité vers son sens de formation
			header.setMessage(move);
			// émission
			try
			{
				NetManager.PackMessage(header);
				//NetManager.SendMessage(header);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 */
	}
	
	
	protected void NetSendSynchronise()
	{
		/*NetHeader header = new NetHeader();
		header.setTypeMessage(TYPE.SYNC);
		NetSynchronize sync = new NetSynchronize();
		sync.setIdUnity(this.getId());
		sync.setPosx(this.getPositionMeterX());
		sync.setPosy(this.getPositionMeterY());
		//sync.setRotation(this.getBody().getAngle());
		sync.setVectorDirFormation(this.vecDirFormation);
		header.setMessage(sync);
		// émission
		try
		{
			NetManager.PackMessage(header);
			//NetManager.SendMessage(header);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	protected float lerp(float value, float start, float end)
	{
	    return start + (end - start) * value;
	}
	
	protected void computeRotation(Vec2 vectarget)
	{
		if(vectarget != null)
		{
			// on crée la class de rotation
			Rot r = new Rot();
			r.s = vectarget.y;
			r.c = vectarget.x;
			// receptin de l'angle de rotation
			//float angle = r.getAngle(); 
			// assouplissement en utilisant un lerp
			float angle = lerp(0.2f,this.body.getAngle(), r.getAngle() ); // angle de la tourelle déterminé
			
			this.body.setTransform(this.getBody().getPosition(), angle);
		}
	}

	@Override
	public void update(Time deltaTime) 
	{
		// on positionne les coordonnÃ©es Ã©cran par rapport au coordonnÃ©e physique
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
		
		if(this.isStop)
			return;
		
		// -------------
		// téléportation
		// -------------
		
		
		/*if(!nextNode && this.enemyAttribute == null) // si il n'y a pas de nextnode et qu'il n'y a pas d'unité enemy attribuée
		{
			elapseSearchClock = Time.add(elapseSearchClock, deltaTime);
			if(elapseSearchClock.asSeconds() > TIME_BEFORE_TELEPORTATION) // si bloquÃ© plus de 2 secondes
			{
				
				if(this.enemyAttribute != null) // on est bloqué et on devrai se téléporter, ici on va rechercher un enemy proche
				{
					// on applique un nouvelle enemy
					UnityNet best = EntityManager.searchEnemyZoneNear(this);
					if(best != null)
						this.setEnemyAttribute(best);
					// on replace le timer de téléportation à 0
					elapsedSearchClock = Time.ZERO;
					
				}
				else
				{
				
					elapseSearchClock = Time.ZERO;
					// on saute une node de recherche
					if(this.pathFinalPath != null && this.pathFinalPath.getLength() > 0 && this.indexNode < this.pathFinalPath.getLength())
					{
						int x = this.pathFinalPath.getX(this.indexNode);
						int y = this.pathFinalPath.getY(this.indexNode);
						//this.pathFinal.remove(0);
						// on tÃ©lÃ©porte l'unitÃ©
						this.body.setTransform(new Vec2(x,y), this.body.getAngle());
						nextNode = true;
					}
				}
				

			}
		}
		*/
		
		// ------------------------------------
		// récupération d'une nouvelle position
		// ------------------------------------
		
				if(nextNode && this.pathFinalPath != null && this.pathFinalPath.getLength() > 0 && this.indexNode  < this.pathFinalPath.getLength()  )
				{
					// on récupère la node suivante
				
					int nx = this.pathFinalPath.getX(this.indexNode);
					int ny = this.pathFinalPath.getY(this.indexNode);
					
					// on capte la node si 
					
					// on lance le clock du resetSearchClock
					elapseSearchClock = Time.ZERO;
					
					// transformation en postion meter
					
					 mx = (float)nx + 0.5f;
					 my = (float)ny +0.5f;
					
					 nextNode = false;
					 
					 this.indexNode++;
					 
					 // envoie des informations sur le réseau
					 this.NetSend(this.body.getPosition().x, this.body.getPosition().y, mx, my,null); // null car ce n'est pas encore la destination finale (pour le retournement de formation)
					 
				}
				else
				{
				// -------------------------------------
				// mouvement vers la destination finale
				// ------------------------------------
						
					Vec2 n =  new Vec2(this.tfx / PhysicWorldManager.getRatioPixelMeter(),this.tfy  / PhysicWorldManager.getRatioPixelMeter());
					this.vecTarget = n.sub(this.body.getPosition());
					
					if(this.vecTarget.length() < 0.2f )
						{
							this.body.setLinearVelocity(new Vec2(0f,0f));
						
									
							if(!this.isArrived)
							{
								this.NetSend(this.body.getPosition().x, this.body.getPosition().y,this.body.getPosition().x, this.body.getPosition().y,this.vecDirFormation);
								this.isArrived = true;
							
							}
							
							
							// il est arrivé, on tourne l'axe vers la vecteur fleche pointé
							this.vecTarget = this.vecDirFormation;
							// on détermine l'angle du sprite
							this.computeRotation(this.vecTarget);
							// envoie de la synchronisation finale
							this.NetSendSynchronise();
									
						}
						else
						{
							this.vecTarget.normalize();
							// on calcul la rotation
							this.computeRotation(this.vecTarget);
							// on applique un vecteur de déplacement
							this.body.setLinearVelocity(this.vecTarget.mul(6f));
							
							// envoie des informations sur le réseau
								this.NetSend(this.body.getPosition().x, this.body.getPosition().y, n.x, n.y,this.vecDirFormation);
								
								this.isArrived = false;
								
							// enclenchement de l'animatino
								this.setAnimate(ANIMATE.WALK);
						}
					
					
				}
				
				
				if(!nextNode)
				{
				
					// -----------------------------------
					// Mouvement vers une destination centrale à un node
					// ------------------------------------------------
				
					Vec2 n =  new Vec2(mx,my);
					this.vecTarget = n.sub(this.body.getPosition());
				
					if(this.vecTarget.length() < 0.4f)
					{
						nextNode = true;
						this.body.setLinearVelocity(new Vec2(0f,0f));
					
					
					}
					else
					{
						this.vecTarget.normalize();
						// on calcul la rotation
						this.computeRotation(this.vecTarget);
						// on applique un vecteur de déplacement
						this.body.setLinearVelocity(this.vecTarget.mul(6f));
						// animation
						this.setAnimate(ANIMATE.WALK);
					}
					
				}
				
		// déplacement vers un enemy, follow
				
				if(this.enemyAttribute != null && !this.enemyAttribute.isKilled())
				{
					elapsedSearchFollowEnemy = Time.add(elapsedSearchFollowEnemy, deltaTime);
					if(elapsedSearchFollowEnemy.asSeconds() > 2f)
					{
						// on calcul la formation final de frappe
						// on détermine la rotation finale
					//	Vec2 vFinal = EntityManager.searchPosition(this,this.enemyAttribute);
						Vec2 vEnemy = this.enemyAttribute.getBody().getPosition();
						// on détermine le vecteur de direction
				//		Vec2 vDir = vEnemy.sub(vFinal);
					//	vDir.normalize();
						
						//this.setTargetPosition(vFinal.x * PhysicWorldManager.getRatioPixelMeter(), vFinal.y * PhysicWorldManager.getRatioPixelMeter(), (int)vFinal.x, (int)vFinal.y, vDir);
						elapsedSearchFollowEnemy = Time.ZERO;
					}
				}
				else
					this.enemyAttribute = null;
				
				
		// ------------------------------------
		// Code pour tÃ©lÃ©porter une unitÃ© bloquÃ©
		// ------------------------------------
			
		// appel à la methode animate
				this.animate(deltaTime);
	}
	
	// abstract pour l'animation
	public abstract void animate(Time deltaTime);
	
	// abstract pour les dommages
	public abstract void setDamage(int damage);
	
	// abstract pour la mort
	public abstract void setKill();
	
	public Vec2 getVectorTarget()
	{
		if(this.vecTarget == null)
			return new Vec2(1,0);
		else
			return this.vecTarget;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public float getPosXMeter()
	{
		return this.body.getPosition().x;
	}
	
	public float getPosYMeter()
	{
		return this.body.getPosition().y;
	}
	
	public void setPosXYMeter(float x,float y)
	{
		this.body.setTransform(new Vec2(x,y), this.body.getAngle());
		posx = body.getPosition().x * PhysicWorldManager.getRatioPixelMeter();
		posy = body.getPosition().y * PhysicWorldManager.getRatioPixelMeter();
	}

	/**
	 * @return the posx
	 */
	public float getPosx() {
		return posx;
	}

	/**
	 * @param posx the posx to set
	 */
	public void setPosx(float posx) {
		this.posx = posx;
	}

	/**
	 * @return the posy
	 */
	public float getPosy() {
		return posy;
	}

	/**
	 * @param posy the posy to set
	 */
	public void setPosy(float posy) {
		this.posy = posy;
	}

	/**
	 * @return the nodex
	 */
	public int getNodex() {
		return nodex;
	}

	/**
	 * @param nodex the nodex to set
	 */
	public void setNodex(int nodex) {
		this.nodex = nodex;
	}

	/**
	 * @return the nodey
	 */
	public int getNodey() {
		return nodey;
	}

	/**
	 * @param nodey the vnodey to set
	 */
	public void setNodey(int nodey) {
		this.nodey = nodey;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
	

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) 
	{
		this.isSelected = isSelected;
		if(this.isSelected)
			System.out.println("UnitÃ© : " + this.toString() + " est sÃ©lectionnÃ©");
	}

	

	@Override
	public void onCallsearchPath(Path finalPath) 
	{
	
		
		if(finalPath != null)
		{
			this.pathFinalPath = finalPath;
			this.cptNavMesh = this.pathFinalPath.getLength();
			this.indNavMesh = 1;
			elapseSearchClock = Time.ZERO;
		
			this.indexNode = 1;
			this.nextNode = true;
			this.isArrived = false;
		}
	}
	
	public void setAnimate(ANIMATE animate)
	{
		// on spécifie le mode d'action
		this.animate = animate;
		
		
	}
	
	abstract public void strikeNow();
	
	public void attributeEnemy(Unity enemy)
	{
		this.setEnemyAttribute(enemy);
	}




	public boolean isKilled() {
		return isKilled;
	}




	public void setKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}


}

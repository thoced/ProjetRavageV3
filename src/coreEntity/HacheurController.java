package coreEntity;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.KeyEvent;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import CoreTexturesManager.TexturesManager;
import coreAI.Node;
import coreEntity.Unity.TYPEUNITY;
import coreEntity.UnityBaseView.TYPE_ANIMATION;
import coreEntityManager.ChoosePosition;
import coreEntityManager.EntityManager;
import coreEntityManager.ReservationManager;
import coreEntityManager.EntityManager.CAMP;
import coreEntityManager.NodeReserved;
import coreEvent.EventManager;
import coreLevel.LevelManager;
import coreNet.NetBase;
import coreNet.NetDataUnity;
import coreNet.NetHeader;
import coreNet.NetSendThread;
import coreNet.NetHeader.TYPE;
import coreNet.NetManager;
import coreNet.NetStrike;
import corePhysic.PhysicWorldManager;

public class HacheurController extends UnityBaseController 
{
	private float elapsedTimeAttack = 0f;
	

	
	
	public HacheurController() {
		super();
		// instance de la vue et du model
		
		this.setModel(new DuellisteModel(this));
		this.setView(new DuellisteView(this.getModel(),this));
		
		// energy
		this.getModel().setStreightStrike(25);
		this.getModel().setEnergy(90);
		this.getModel().setEnergyMax(90);
		this.getModel().setArmor(2);
		this.getModel().setPowerPenetration(2);
		this.getModel().setDexterity(2);
		this.getModel().setAgility(2);
	}

	@Override
	public void init()
	{
		// initialisation de la vue avec un sprite
		if(this.getModel().getMyCamp() == CAMP.YELLOW)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_HACHEUR_YELLOW.png"));
		if(this.getModel().getMyCamp() == CAMP.BLUE)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_HACHEUR_BLUE.png"));

		// initialisa la vue avec l'origine du sprite
		this.getView().getSprite().setOrigin(new Vector2f(16f,16f));
		// spécifie à la vue l'animation à joué par défaut
		this.getView().playAnimation(TYPE_ANIMATION.NON);
		
		// on parametre la vue pour préciser les indices des frames
		this.getModel().setMIN_IND_FOR_WALK(0);
		this.getModel().setMAX_IND_FOR_WALK(7);
		this.getModel().setMIN_IND_FOR_STRIKE(8);
		this.getModel().setMAX_IND_FOR_STRIKE(23);
		this.getModel().setNB_FRAME_BY_SECOND(12);
		this.getModel().setHEIGHT_FRAME(32);
		this.getModel().setWIDTH_FRAME(32);
		
		
		
		
		
		// ajout au event manager
		EventManager.addCallBack(this);
		
	}

	

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.update(deltaTime);
		
		// mise à jour du temps écoulé pour l'attaque
		elapsedTimeAttack += deltaTime.asSeconds();
		
		if(elapsedTimeAttack > 1f)
		{
			
			if(this.getModel().getIdEnemy() != -1 && m_isLastStep)
			{
				// récupération de l'objet enemy
				m_enemy = EntityManager.getVectorUnityNet().get(this.getModel().getIdEnemy());
				
				if(m_enemy != null)
				{
					this.m_dirEnemy = m_enemy.getModel().getPosition().sub(this.getModel().getPosition());
					
					if(this.m_dirEnemy.length() < 2f)
					{
					
						this.strike();
						
						// ---------------------------------------------
						// ---------------------------------------------
						// calcul du coup critique ?
						Random rand = new Random();
						int resultCritique = rand.nextInt(100);
						if(resultCritique <= 15)  // 15 étant le pourcentage de chance pour le hacheur
							this.getModel().setCriticalStrike(true);
						else
							this.getModel().setCriticalStrike(false);
						// ---------------------------------------------
						// ---------------------------------------------
						
						
						NetDataUnity data = new NetDataUnity();			// creatin du netdataunity
						data.setTypeMessage(NetBase.TYPE.UPDATE);		// on spécifie que c'est une update
						this.getModel().setKnocking(true);				// on spécifie au modèle que nous sommes en train de frapper
						this.prepareModelToNet();						// préparation du model pour l'envoi sur le réseau
						try
						{
							data.setModel(this.getModel().clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					// placement du model dans le netdataunity
						NetSendThread.push(data);						// envoi sur le réseau
					}
					else
					{
						// déplacement vers l'enemy
						this.moveToEnemy();
					}
					
				}
				{
					this.getModel().setIdEnemy(-1);
				}
				
				
			}
			
		// remise à zero du temps d'attaque
			elapsedTimeAttack = 0f;
		}
		
		
		
	}

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		super.onKeyboard(keyboardEvent);
		
		return false;
		
	}

	
}

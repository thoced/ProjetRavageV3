package coreEntity;

import java.io.IOException;
import java.util.List;

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
import coreSounds.SoundsManager;
import coreSounds.SoundsManager.TYPE_SOUNDS;

public class PiquierController extends UnityBaseController 
{
	private float elapsedTimeAttack = 0f;
	

	
	
	public PiquierController() {
		super();
		// instance de la vue et du model
		
		this.setModel(new PiquierModel(this));
		this.setView(new PiquierView(this.getModel(),this));
		
		// energy
		this.getModel().setStreightStrike(15);
		this.getModel().setEnergy(180);
		this.getModel().setEnergyMax(180);
		this.getModel().setArmor(3);
		this.getModel().setPowerPenetration(4);
		this.getModel().setDexterity(2);
		this.getModel().setAgility(1);
		this.getModel().setFrequencyStrike(1);
	}

	@Override
	public void init()
	{
		// initialisation de la vue avec un sprite
		if(this.getModel().getMyCamp() == CAMP.YELLOW)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_Piquiers_Jaunes.png"));
		if(this.getModel().getMyCamp() == CAMP.BLUE)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_Piquiers_Bleus.png"));

		// initialisa la vue avec l'origine du sprite
		this.getView().getSprite().setOrigin(new Vector2f(40f,40f));
		// sp�cifie � la vue l'animation � jou� par d�faut
		this.getView().playAnimation(TYPE_ANIMATION.NON);
		
		this.getModel().setMIN_IND_FOR_WALK(0);
		this.getModel().setMAX_IND_FOR_WALK(10);
		this.getModel().setMIN_IND_FOR_STRIKE(11);
		this.getModel().setMAX_IND_FOR_STRIKE(26);
		this.getModel().setNB_FRAME_BY_SECOND(20);
		this.getModel().setHEIGHT_FRAME(80);
		this.getModel().setWIDTH_FRAME(80);
		
		// ajout au event manager
		EventManager.addCallBack(this);
		
	}

	

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.update(deltaTime);
		
		// mise � jour du temps �coul� pour l'attaque
		elapsedTimeAttack += deltaTime.asSeconds();
		
		if(elapsedTimeAttack > this.getModel().getFrequencyStrike())
		{
			
			if(this.getModel().getIdEnemy() != -1 && m_isLastStep)
			{
				// r�cup�ration de l'objet enemy
				m_enemy = EntityManager.getVectorUnityNet().get(this.getModel().getIdEnemy());
				
				if(m_enemy != null)
				{
					this.m_dirEnemy = m_enemy.getModel().getPosition().sub(this.getModel().getPosition());
					
					if(this.m_dirEnemy.length() < 2f)
					{
					
						this.strike();
						
						SoundsManager.PlaySounds(TYPE_SOUNDS.STRIKE_SOUNDS);
						
						
						NetDataUnity data = new NetDataUnity();			// creatin du netdataunity
						data.setTypeMessage(NetBase.TYPE.UPDATE);		// on sp�cifie que c'est une update
						this.getModel().setKnocking(true);				// on sp�cifie au mod�le que nous sommes en train de frapper
						this.prepareModelToNet();						// pr�paration du model pour l'envoi sur le r�seau
						try
						{
							data.setModel(this.getModel().clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					// placement du model dans le netdataunity
						NetSendThread.push(data);						// envoi sur le r�seau
					}
					else
					{
						// d�placement vers l'enemy
						this.moveToEnemy();
					}
					
				}
				{
					this.getModel().setIdEnemy(-1);
				}
				
				
			}
			
		// remise � zero du temps d'attaque
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

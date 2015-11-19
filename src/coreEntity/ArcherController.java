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
import coreProjectil.Arrow;
import coreProjectil.ProjectilManager;
import coreSounds.SoundsManager;
import coreSounds.SoundsManager.TYPE_SOUNDS;

public class ArcherController extends UnityBaseController 
{
	private float elapsedTimeAttack = 0f;
	

	
	
	public ArcherController() {
		super();
		// instance de la vue et du model
		
		this.setModel(new ArcherModel(this));
		this.setView(new ArcherView(this.getModel(),this));
		
		// energy
		this.getModel().setStreightStrike(15);
		this.getModel().setEnergy(140);
		this.getModel().setEnergyMax(140);
		this.getModel().setArmor(1);
		this.getModel().setPowerPenetration(2);
		this.getModel().setDexterity(3);
		this.getModel().setAgility(2);
		this.getModel().setFrequencyStrike(0.66f);
	}

	@Override
	public void init()
	{
		// initialisation de la vue avec un sprite
		if(this.getModel().getMyCamp() == CAMP.YELLOW)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_ARCHER_YELLOW.png"));
		if(this.getModel().getMyCamp() == CAMP.BLUE)
			this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_ARCHER_BLUE.png"));

		// initialisa la vue avec l'origine du sprite
		this.getView().getSprite().setOrigin(new Vector2f(16f,16f));
		// spécifie à la vue l'animation à joué par défaut
		this.getView().playAnimation(TYPE_ANIMATION.NON);
		
		// on parametre la vue pour préciser les indices des frames
		this.getModel().setMIN_IND_FOR_WALK(0);
		this.getModel().setMAX_IND_FOR_WALK(5);
		this.getModel().setMIN_IND_FOR_STRIKE(17);
		this.getModel().setMAX_IND_FOR_STRIKE(31);
		this.getModel().setNB_FRAME_BY_SECOND(15);
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
		
		if(elapsedTimeAttack > this.getModel().getFrequencyStrike())
		{
			
			if(this.getModel().getIdEnemy() != -1 && m_isLastStep)
			{
				// récupération de l'objet enemy
				m_enemy = EntityManager.getVectorUnityNet().get(this.getModel().getIdEnemy());
				
				if(m_enemy != null)
				{
					this.m_dirEnemy = m_enemy.getModel().getPosition().sub(this.getModel().getPosition());
					
					Arrow arrow = new Arrow(EntityManager.getNewIdUnity(),this.getModel().getPosition(),this.m_dirEnemy,10f,2f,this.m_dirEnemy.length(),this.getModel().getIdEnemy());
					ProjectilManager.getProjectils().put(arrow.getController().getId(), arrow);
					
					
					// attaque à distance, tir
					if(this.m_dirEnemy.length() < 15f)
					{
						
						// attaque au corps à corps
						if(this.m_dirEnemy.length() < 2f)
						{
						
							this.strike();
							
							SoundsManager.PlaySounds(TYPE_SOUNDS.STRIKE_SOUNDS);
							
							NetDataUnity data = new NetDataUnity();			// creatin du netdataunity
							data.setTypeMessage(NetBase.TYPE.UPDATE);		// on spécifie que c'est une update
							this.getModel().setKnocking(true);				// on spécifie au modèle que nous sommes en train de frapper
							this.getModel().setStreightStrike(10);			// on spécifie la force de frappe
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
							// tir
							
						}
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

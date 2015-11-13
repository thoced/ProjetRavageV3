package coreEntity;

import org.jsfml.system.Time;

import CoreTexturesManager.TexturesManager;
import coreEntity.UnityBaseView.TYPE_ANIMATION;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import coreEntityManager.EntityManager.CAMP;
import coreNet.NetBase;
import coreNet.NetDataUnity;
import coreNet.NetSendThread;

public class UnityNetController extends UnityBaseController
{

	public UnityNetController() 
	{
		super();
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.update(deltaTime);
		
		// code pour les unités adverses réseaux
		
		// si l'unité est en train de frapper, on appel la methode play de la view
		if(this.getModel().isKnocking())
		{
			// on joue l'animation
			this.strike();
			// on empèche de frapper une seconde fois
			this.getModel().setKnocking(false);
			// on frappe réelleemnt l'enemy
		/*	UnityBaseController u = EntityManager.getVectorUnity().get(this.getModel().getIdEnemy());
			System.out.println("Enemy : " + u);
			if(u!=null)
				u.hit(this.getModel().getStreightStrike());*/
			
			
		}
		
		if(this.getModel().isKilled)
		{
			// on joue la mort
			this.destroy();
			EntityManager.getVectorUnityNet().remove(this.getModel().getId());
			
			// on ajoute un cadavre
			BloodManager.addUnityKilled(this.getModel().getPosition(), this.getModel().getMyCamp());
		}
		
		
		
	}
	
	

	@Override
	public void strike() {
		// TODO Auto-generated method stub
		super.strike();
		
	
		
		// on frappe réelleemnt l'enemy
		UnityBaseController u = EntityManager.getVectorUnity().get(this.getModel().getIdEnemy());
		System.out.println("Enemy : " + u);
		if(u!=null)
			u.hit(this.getModel().getStreightStrike(),this.getModel().getPowerPenetration(),this.getModel().getDexterity(),this.getModel().isCriticalStrike());
		
		
	}
	
	
    
	@Override
	public void moveToEnemy() 
	{
		// move to enemy pour l'unité réseau
		this.m_enemy = EntityManager.getVectorUnity().get(this.getModel().getIdEnemy());
		this.setSequence(ETAPE.MOVE_TO_ENEMY);
		this.getView().playAnimation(TYPE_ANIMATION.WALK);
				
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		super.init();
		
		// chargement des textures en fonction du model
		if(this.getModel().getMyCamp() == CAMP.YELLOW)
		{
			if(this.getModel().getIdType() == TYPEUNITY.PIQUIER)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_Piquiers_Jaunes.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.KNIGHT)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_KNIGHT_YELLOW.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.ESCRIME)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_DUELLISTE_YELLOW.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.BUCHE)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_HACHEUR_YELLOW.png"));
			}
		}
		
		if(this.getModel().getMyCamp() == CAMP.BLUE)
		{
			if(this.getModel().getIdType() == TYPEUNITY.PIQUIER)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_Piquiers_Bleus.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.KNIGHT)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_KNIGHT_BLUE.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.ESCRIME)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_DUELLISTE_BLUE.png"));
			}
			
			if(this.getModel().getIdType() == TYPEUNITY.BUCHE)
			{
				this.getView().setSprite(TexturesManager.GetSpriteByName("ANIM_HACHEUR_BLUE.png"));
			}
		}
		
		// création de l'origine pour le sprite
		this.getView().getSprite().setOrigin(this.getModel().getOrigineSprite());
		// spécifie à la vue l'animation à joué par défaut
		this.getView().playAnimation(TYPE_ANIMATION.NON);
		
	}
	
	
	
	

}

package coreEntity;

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

import coreEntity.Unity.ANIMATE;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import corePhysic.PhysicWorldManager;

public class Knight extends Unity 
{

	// Timer pour la cadence de frappe
	private final float frequenceStrike = 1f;
	private Time  timeElapsedStrike = Time.ZERO;
	
	// energie
	
	private int energy = 100;
	
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		super.init();
		
		// TODO Auto-generated method stub
				// intialisation du body
				BodyDef bdef = new BodyDef();
				bdef.active = true;
				bdef.bullet = false;
				bdef.type = BodyType.KINEMATIC;
				bdef.fixedRotation = false;
				bdef.userData = this;
			
				//bdef.gravityScale = 0.0f;
				
				// creation du body
				body = PhysicWorldManager.getWorld().createBody(bdef);
				
				Shape shape = new CircleShape();
				shape.m_radius = 0.55f;
				
				FixtureDef fDef = new FixtureDef();
				fDef.shape = shape;
				fDef.density = 1.0f;
				
				fDef.friction = 0.0f;
				fDef.restitution = 0.0f;
			
				Fixture fix = body.createFixture(fDef);
				
				// instance du resetSearch
				resetSearchClock = new Clock();
				
				// anim sprite
				this.animSpriteRect = new IntRect[19];
				for(int i=0;i<19;i++)
					this.animSpriteRect[i] = new IntRect(0 + i * 32,0,32,32);
				
				this.currentAnim = this.animSpriteRect[0];
				this.indAnim = 0;
				this.indAnimWalk = 0;
				
				
				
				// type d'unity
				this.idType = TYPEUNITY.KNIGHT;
	}

	@Override
	public void update(Time deltaTime) 
	{
		
			// appel au super constructeur
			super.update(deltaTime);
			
			// on vérifie si la distance entre l'ennemi est inférieur à 1
			// on frappe et en envoie sur le réseau
			// on incrémente le temps
			
			this.timeElapsedStrike = Time.add(this.timeElapsedStrike, deltaTime);
			
			if(this.getEnemyAttribute() != null && timeElapsedStrike.asSeconds() > this.frequenceStrike)
			{
				Unity enemy = this.getEnemyAttribute();
			
				// on calcul la distance
				float distance = enemy.getBody().getPosition().sub(this.getBody().getPosition()).length();
				if(distance < 2f )
				{
					// on frappe
					this.strikeNow();
					this.timeElapsedStrike = Time.ZERO; // on positinne à 0 le temps écouté entre deux frappes
					// on jette un peu de sang en dessous de l'ennemy virtuel
					//BloodManager.addBlood(enemy.getpo);
				}
				
			}
		
	}

	@Override
	public void strikeNow() {
		// TODO Auto-generated method stub
		// activation de l'animation de frappe
		this.setAnimate(ANIMATE.STRIKE);
		// on lance sur le réseau la frappe
		if(this.getEnemyAttribute() != null)
			this.NetStrike(this.getEnemyAttribute().getId(), 10);
	}

	@Override
	public void attributeEnemy(Unity enemy) {
		// TODO Auto-generated method stub
		super.attributeEnemy(enemy);
				
	}

	@Override
	public void animate(Time deltaTime) 
	{
		if(this.animate == ANIMATE.KILL)
		{
			this.currentAnim = this.animSpriteRect[15]; // on joue le sprite de kill
		}
		
		if(this.animate == ANIMATE.STRIKE) // en mode je frappe comme un gros lourd !!!
		{
				// on récupère l'animation courante
				this.currentAnim = this.animSpriteRect[indAnim];
				// on additionne le temps écoulé
				this.timeElapsedAnim = Time.add(this.timeElapsedAnim, deltaTime);
				// si le temps écoulé est supérieur à ***  on incrémente l'indice d'animation
				if(this.timeElapsedAnim.asSeconds() > 0.03f)
				{
					this.timeElapsedAnim = Time.ZERO;
					indAnim++;
					if(indAnim > 18)
					{
						indAnim = 4;
						this.animate = ANIMATE.PAUSE;
					}
				}
			
		}
		
		if(this.animate == ANIMATE.PAUSE) // en mode je suis sur place et je ne fais rien !!!
		{
			// on récupère l'animation courante
			this.currentAnim = this.animSpriteRect[0];
		}
		
		if(this.animate == ANIMATE.WALK) // en mode je me déplace !!!
		{
			// on récupère l'animation courante
			this.currentAnim = this.animSpriteRect[indAnimWalk];
			// on additionne le temps écoulé
			this.timeElapsedAnim = Time.add(this.timeElapsedAnim, deltaTime);
			// si le temps écoulé est supérieur à ***  on incrémente l'indice d'animation
			if(this.timeElapsedAnim.asSeconds() > 0.08f)
			{
				this.timeElapsedAnim = Time.ZERO;
				indAnimWalk++;
				if(indAnimWalk > 3)
				{
					indAnimWalk = 0;
					this.animate = ANIMATE.WALK;
				}
			}
		}
		
	}

	@Override
	public void setDamage(int damage) 
	{
		this.energy -= damage;
		
		if(this.energy <= 0 )
		{
			// l'unité meurt, je vais envoyer un message kill sur le réseau et lancer l'anim de la mort
			this.setAnimate(ANIMATE.KILL);
			// emission sur le réseau
			this.NetKill(this.getId());
			// on est mort
		//	EntityManager.IamKilled(this);
			// je place le mort
			//BloodManager.addUnityKilled(this.getPosx(), this.getPosy(),this.getMyCamp());
			
		}
		else
		{
			// on indique que l'on est mort
			this.setKill();
			//BloodManager.addBlood(this.getPosx(), this.getPosy()); // ajout du sang car je recois un damage
			
		}
		
	}

	@Override
	public void setKill() 
	{
		// TODO Auto-generated method stub
		this.isKilled = true;
	}

	

	
	
	
}

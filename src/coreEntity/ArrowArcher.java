package coreEntity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;








import coreEntityManager.EntityManager;
import coreNet.NetBase;
import coreNet.NetDataProjectil;
import coreNet.NetDataUnity;
import coreNet.NetSendThread;
import corePhysic.PhysicWorldManager;
import CoreTexturesManager.TexturesManager;
import ravage.IBaseRavage;

public class ArrowArcher extends UnityBaseController
{

	private ArrowArcherView view;
	private ArrowArcherModel model;
	
	
	// Sprite fleche
	private Sprite arrowSprite;
	private Vec2 startArrow;
	private float diffStartToTarget;
	private Vec2 dirArrow;
	private float timeToDie;
	private float elapsedTimeToDie = 0f;
	
	private UnityBaseController enemy;
	
	
	
	
	public ArrowArcherView getView() {
		return view;
	}



	public ArrowArcherModel getModel() {
		return model;
	}



	public Sprite getArrowSprite() {
		return arrowSprite;
	}
	
	

	public void setArrowSprite(Sprite arrowSprite) {
		this.arrowSprite = arrowSprite;
	}
	
	



	public UnityBaseController getEnemy() {
		return enemy;
	}



	public void setEnemy(UnityBaseController enemy) {
		this.enemy = enemy;
	}



	public float getDiffStartToTarget() {
		return diffStartToTarget;
	}



	public void setDiffStartToTarget(float diffStartToTarget) {
		this.diffStartToTarget = diffStartToTarget;
	}



	public float getTimeToDie() {
		return timeToDie;
	}



	public void setTimeToDie(float timeToDie) {
		this.timeToDie = timeToDie;
	}



	public Vec2 getStartArrow() {
		return startArrow;
	}



	public Vec2 getDirArrow() {
		return dirArrow;
	}



	public void setStartArrow(Vec2 startArrow) {
		this.startArrow = startArrow;
	}



	public void setDirArrow(Vec2 dirArrow)
	{
		this.dirArrow = dirArrow;
	
		// on cr�e la class de rotation
					Rot r = new Rot();
					r.s = dirArrow.y;
					r.c = dirArrow.x;
					// receptin de l'angle de rotation
					// float angle = r.getAngle();
					// assouplissement en utilisant un lerp
					

					this.getModel()
							.getBody()
							.setTransform(this.getModel().getBody().getPosition(),
									r.getAngle());
	
	}
	
	



	@Override
	public void update(Time deltaTime) 
	{
		// si l'elaspedTimeToDie est sup�rieur � le temps de vie, on tue le projectil
		elapsedTimeToDie += deltaTime.asSeconds();
		if(elapsedTimeToDie > timeToDie)
		{
			this.destroy();
			EntityManager.getVectorUnity().remove(this.getModel().getId());
		}
		else
		{	
			// si le chemin parcouru est sup�rieur � la distance entre l'enemy et le tireur, on touche la cible
			if(this.getModel().getPosition().sub(this.startArrow).length() > this.diffStartToTarget)
			{
				// on toucne la cible

				NetDataProjectil projectil = new NetDataProjectil();			// creatin du netdataunity
				projectil.setTypeMessage(NetBase.TYPE.PROJECTIL);		// on sp�cifie que c'est une update
				projectil.setIdProjectil(this.getModel().getId());
				projectil.setIdUnityTouch(this.getEnemy().getModel().getId());
				projectil.setDamage(15);
				this.prepareModelToNet();						// pr�paration du model pour l'envoi sur le r�seau
				
				
				NetSendThread.push(projectil);						// envoi sur le r�seau
				
			}
			
			
			Vec2 d = dirArrow.clone();
			d.normalize();
			this.getModel().getBody().setLinearVelocity( d.mul(35));
		}
	}



	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		super.init();
		
		// chargement de la texture de fleche
		this.getView().setSprite(TexturesManager.GetSpriteByName("Fleche_01.png"));
					
		IntRect rect = new IntRect(0,0,8,8);
		if(ArrowArcher.this.getArrowSprite() != null)
			ArrowArcher.this.getArrowSprite().setTextureRect(rect);
		// cr�ation des vecteur vide
		
		
		
		
	}


	
	
	public ArrowArcher() 
	{
		// instance du controller et de la vue
		super();
		// instance du controller et de la vue
		model = new ArrowArcherModel(this);
		this.setModel(model);
		view = new ArrowArcherView(model, this);
		this.setView(view);
	
	
	}

	
	
	
	public class ArrowArcherView extends UnityBaseView
	{

		public ArrowArcherView(UnityBaseModel model,
				UnityBaseController controller) 
		{
			super(model, controller);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void draw(RenderTarget arg0, RenderStates arg1) 
		{
			Vec2 pos = this.getController().getModel().getPosition();
			this.sprite.setPosition(this.toPixelVector2f(pos));
			this.sprite.setRotation((float)((this.getController().getModel().getBody().getAngle() * 180f) / Math.PI) % 360f);
			
			arg0.draw(sprite);
			
		}

		
	
	}
	

	public class ArrowArcherModel extends UnityBaseModel
	{

		public ArrowArcherModel() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ArrowArcherModel(UnityBaseController controller) {
			super(controller);
			// TODO Auto-generated constructor stub
		}
		
		

		@Override
		public void setPosition(Vec2 position)
		{
			// TODO Auto-generated method stub
			super.setPosition(position);
			
			// sauvegarde de la position de d�part
			ArrowArcher.this.setStartArrow(position);
		}

		@Override
		public void initModel(UnityBaseController controller)
		{
			
			this.controller = controller;
			// cr�ation du body
					// initialisation du body
					BodyDef bdef = new BodyDef();
					bdef.active = true;
					bdef.bullet = true;
					bdef.type = BodyType.KINEMATIC;
					bdef.fixedRotation = false;
					bdef.userData = controller;
					bdef.linearDamping = 1.0f;
					// creation du body
					this.setBody(PhysicWorldManager.getWorld().createBody(bdef));	
					Shape shape = new CircleShape();
					shape.m_radius = 0.01f;
					FixtureDef fDef = new FixtureDef();
					fDef.isSensor = false;
					fDef.shape = shape;
					fDef.density = 1f;
					fDef.friction = 0.0f;
					fDef.restitution = 0.0f;
				
					Fixture fix = this.getBody().createFixture(fDef);
					// creation du body
					this.body.setTransform(this.position, this.rotation);
					// cr�ation du lock
					lock = new Object();
		
		}
		
		
		
	}
	


}

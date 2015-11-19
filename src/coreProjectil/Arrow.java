package coreProjectil;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;

import CoreTexturesManager.TexturesManager;

public class Arrow extends ProjectilBase
{

	
	
	public Arrow() 
	{
		super();
		// TODO Auto-generated constructor stub
		this.controller = new ArrowController();
		this.controller.init();
		
		
		
	}
	
	public Arrow(int id,Vec2 startPosition,Vec2 direction,float speed,float timeToDie,float distanceToDie,int idEnemy)
	{
		super();
		
		this.controller = new ArrowController(id,startPosition.clone(),direction,speed,timeToDie,distanceToDie,idEnemy);
		this.controller.init();
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		// TODO Auto-generated method stub
		super.draw(arg0, arg1);
	    this.sprite.setPosition(this.toPixelVector2f(((ArrowController)this.controller).getStartPosition()));
		this.sprite.setRotation((float) (((ArrowController)this.controller).getAngle() * 180f / Math.PI % 360f));
		
	    arg0.draw(this.sprite);
	
	}
	
	public class ArrowController extends ProjectilController
	{
		
		private Vec2 startPosition;
		private Vec2 backupStartPosition;
		private float diffStartToTarget;
		private Vec2 direction;
		private float angle;
		private float timeToDie;
		private float speed;
		private int idEnemy;
		private float elapsedTimeToDie = 0f;
		private float distanceToDie = 0f;
		
		
		
		public ArrowController(int id,Vec2 startPosition,Vec2 direction,float speed,float timeToDie,float distanceToDie,int idEnemy)
		{
			this.id = id;
			this.startPosition = startPosition;
			this.backupStartPosition = startPosition;
			this.direction = direction;
			this.speed = speed;
			this.timeToDie = timeToDie;
			this.idEnemy = idEnemy;
			this.distanceToDie = distanceToDie;
			// normalize
			this.direction.normalize();
			
			// calcul de l'angle pour la rotation de la fleche
			Rot r = new Rot();
			r.s = direction.y;
			r.c = direction.x;
			this.angle = r.getAngle();
			
		
			
		}
		
		

		public ArrowController() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		

		public float getAngle() {
			return angle;
		}



		public Vec2 getStartPosition() {
			return startPosition;
		}



		public float getDiffStartToTarget() {
			return diffStartToTarget;
		}



		public Vec2 getDirection() {
			return direction;
		}



		public float getTimeToDie() {
			return timeToDie;
		}



		public float getSpeed() {
			return speed;
		}



		public int getIdEnemy() {
			return idEnemy;
		}



		public float getElapsedTimeToDie() {
			return elapsedTimeToDie;
		}



		public void setStartPosition(Vec2 startPosition) {
			this.startPosition = startPosition;
		}



		public void setDiffStartToTarget(float diffStartToTarget) {
			this.diffStartToTarget = diffStartToTarget;
		}



		public void setDirection(Vec2 direction) {
			this.direction = direction;
		}



		public void setTimeToDie(float timeToDie) {
			this.timeToDie = timeToDie;
		}



		public void setSpeed(float speed) {
			this.speed = speed;
		}



		public void setIdEnemy(int idEnemy) {
			this.idEnemy = idEnemy;
		}



		public void setElapsedTimeToDie(float elapsedTimeToDie) {
			this.elapsedTimeToDie = elapsedTimeToDie;
		}



		@Override
		public void init() 
		{
		
			super.init();
			
			// chargement de la texture arrow
			Arrow.this.sprite = new Sprite(TexturesManager.GetTextureByName("/Fleche_01.png"));
			Arrow.this.sprite.setTextureRect(new IntRect(0,0,8,8));
			
			
		}

		@Override
		public void update(Time deltaTime) 
		{
			elapsedTimeToDie += deltaTime.asSeconds();
			if(elapsedTimeToDie > timeToDie)
			{
				// la fleche meurt de sa belle mort
				ProjectilManager.getProjectils().remove(this.id);
			}
			
			if((backupStartPosition.sub(this.startPosition)).length() > this.distanceToDie)
			{
				// la fleche se détruit car elle touche sa cible
				ProjectilManager.getProjectils().remove(this.id);
			}
			
			startPosition = startPosition.add(this.direction.mul(this.speed * deltaTime.asSeconds()));
			super.update(deltaTime);
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			super.destroy();
		}
		
		
	}
	
	
	
}

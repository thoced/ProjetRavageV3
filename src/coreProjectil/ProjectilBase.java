package coreProjectil;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class ProjectilBase implements Drawable 
{

	protected ProjectilController controller;
	// Sprite
	protected Sprite sprite;
	
	
	protected Vector2f toPixelVector2f(Vec2 v)
	{
		return new Vector2f(v.x * PhysicWorldManager.getRatioPixelMeter(), v.y * PhysicWorldManager.getRatioPixelMeter());
	}
	
	
	
	public ProjectilController getController() {
		return controller;
	}

	
	public Sprite getSprite() {
		return sprite;
	}


	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// TODO Auto-generated method stub

	}
	
	public class ProjectilController implements IBaseRavage
	{
		
		protected int id;
		
		
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public void init() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(Time deltaTime) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			
		}
		
	}

}

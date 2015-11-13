package coreLevel;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;

import coreAI.Node;
import coreCamera.CameraManager;
import corePhysic.PhysicWorldManager;
import CoreLoader.TiledObjectPolylinePoint;
import ravage.IBaseRavage;

public class LevelController implements IBaseRavage
{
	public static LevelManager managerLevel = new LevelManager();
	protected LevelModel model;
	protected LevelView view;
	
	public LevelController()
	{
		view  = new LevelView();
				
	}

	public LevelModel getModel() {
		return model;
	}



	/**
	 * @param model the model to set
	 */
	public void setModel(LevelModel model) {
		this.model = model;
	}

	public LevelView getView() {
		return view;
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

package coreProjectil;

import java.util.concurrent.ConcurrentHashMap;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;

import coreEntity.UnityNetController;
import ravage.IBaseRavage;

public class ProjectilManager implements IBaseRavage, Drawable 
{
	private static ConcurrentHashMap<Integer,ProjectilBase> projectils = new ConcurrentHashMap();
	
	
	
	
	public static ConcurrentHashMap<Integer, ProjectilBase> getProjectils() {
		return projectils;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		for(ProjectilBase proj : projectils.values())
		{
			proj.draw(arg0, arg1);
		}

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Time deltaTime)
	{
		for(ProjectilBase proj : projectils.values())
		{
			proj.getController().update(deltaTime);
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

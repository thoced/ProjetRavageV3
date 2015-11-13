package corePhysic;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class PhysicWorldManager implements IBaseRavage
{
	// World
	private static World world;
	// ratio pixel meter
	private static float RatioPixelMeter = 16f;
	

	
	@Override
	public void init() 
	{
		// instance du World Physic
		world = new World(new Vec2(0,0));
		// contact manager
		ContactManager m = new ContactManager();
		world.setContactListener(m);
	
	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		world.step(deltaTime.asSeconds(), 2, 1);
		world.clearForces();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the world
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * @param world the world to set
	 */
	public static void setWorld(World world) {
		PhysicWorldManager.world = world;
	}

	/**
	 * @return the rationPixelMeter
	 */
	public static float getRatioPixelMeter() {
		return RatioPixelMeter;
	}

	/**
	 * @param rationPixelMeter the rationPixelMeter to set
	 */
	public static void setRatioPixelMeter(float ratioPixelMeter) {
		RatioPixelMeter = ratioPixelMeter;
	}

	

	
}

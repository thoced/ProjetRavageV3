package coreEntityManager;

import org.jbox2d.common.Vec2;

import coreEntity.UnityBaseController;

public class NodeReserved
{
	public Vec2 pos;
	
	public UnityBaseController owner;
	
	public NodeReserved(Vec2 p,UnityBaseController o)
	{
		pos = p;
		owner = o;
	}
}

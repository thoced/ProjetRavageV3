package coreAI;

import org.jbox2d.common.Vec2;

import coreEntity.Unity;
import coreEntity.UnityBaseController;
import coreEntity.UnityNetController;

public class Node 
{
	private int type; // wall, ground, ...
	
	private boolean isDiagonal;
	
	private float x,y;
	
	// node utilisé par une unité ennemie
	private UnityBaseController unityBusy;
	// node réservé par une unité alliée
	private UnityBaseController unityReserved;
	
	public Node(float x,float y,boolean isdiagonal)
	{
		this.x = x;
		this.y = y;
		this.isDiagonal  = isdiagonal;
		unityBusy = null;
	}
	
	public Vec2 getPositionVec2()
	{
		return new Vec2(this.x,this.y);
	}
	
	public void reset(boolean diagonal)
	{
	
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public boolean isNodeReserved(UnityBaseController u)
	{
		if(unityReserved != null && unityReserved.getClass() == u.getClass()) // si le node est réservé par une unité alliée
			return true;
		else
			return false;
	}

	public boolean isNodeFree(UnityBaseController u)
	{
		if(unityBusy == null || unityBusy.getClass() == u.getClass()) // si null ou est de la meme class alors le node est libre pour passer
			return true;
						
		return false;
	}
	
	public void bookNode(UnityBaseController u)
	{
		unityReserved = u;
	}
	
	public void takeNode(UnityBaseController u)
	{
		unityBusy = u;
		
			
	}
	
	public UnityBaseController getUnityOnNode(int x,int y)
	{
		return unityBusy;
	}
	
	public void releaseNode(UnityBaseController u)
	{
		if(unityBusy == u)
			unityBusy = null;
	}
	
	public void unBookNode()
	{
		unityReserved = null;
	}
	

	
	
	
}

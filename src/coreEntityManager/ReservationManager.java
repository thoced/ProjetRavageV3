package coreEntityManager;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import coreEntity.UnityBaseController;

public class ReservationManager
{
	// list de réservation
	private static ArrayListNodeReserved nodes = new ArrayListNodeReserved();
	
	public static boolean add(Vec2 pos, UnityBaseController owner)
	{
		if(nodes.contain(pos))
			return false;
		
		NodeReserved n = new NodeReserved(pos,owner);
		nodes.add(n);
		
		// placement du node réserved dans l'unité
		owner.getModel().setNodeReserved(n);
		return true;
		
	}
	
	public static boolean remove(NodeReserved n)
	{
		return nodes.remove(n);
	}
	
	public static boolean remove(Vec2 pos)
	{
		return nodes.remove(pos);
	}
	
	public static boolean contain(Vec2 pos)
	{
		for(NodeReserved n : nodes)
		{
			if(n.pos.equals(pos))
				return true;
		}
		
		return false;
	}
	
}

class ArrayListNodeReserved extends ArrayList<NodeReserved>
{
		
	// contient-il un node réserved ayant cette position
	public boolean contain(Vec2 posNode)
	{
		for(NodeReserved n : this)
		{
			if(n.pos.equals(posNode))
				return true;
		}
		
		return false;
	}
	
	// supprime un node ayant cette position
	public boolean remove(Vec2 posNode)
	{
		NodeReserved nodeRemoved = null;
		
		for(NodeReserved n : this)
		{
			if(n.pos.equals(posNode))
			{
				nodeRemoved = n;
				break;
			}
		}
		
		if(nodeRemoved != null)
			return this.remove(nodeRemoved);
		else
			return false;
	}
}




package coreEntityManager;

import java.util.Comparator;

import org.jbox2d.common.Vec2;

import coreEntity.UnityBaseController;

public class TriEnemy implements Comparator<UnityBaseController>
{
	private UnityBaseController m_owner;
	
	public TriEnemy(UnityBaseController owner)  // owner = l'unité à partir de laquel il faut comparer la distance
	{
		m_owner = owner;
	}
	
	@Override
	public int compare(UnityBaseController one, UnityBaseController two) 
	{
	
		if(m_owner.getModel().getPosition().sub(one.getModel().getPosition()).lengthSquared() < m_owner.getModel().getPosition().sub(two.getModel().getPosition()).lengthSquared())
		{
			return -1;
		}
		else
			return 1;
		
		
	}

	

}

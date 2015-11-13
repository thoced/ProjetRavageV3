package coreEntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;

import com.sun.glass.ui.Window.Level;

import coreEntity.UnityBaseController;
import coreLevel.LevelController;
import coreLevel.LevelManager;

public class ChoosePosition 
{
	private List<NearNode> listNode;
	
	private static LevelController level = LevelManager.getLevel();
	
	public ChoosePosition()
	{
		// instance de listNode
		listNode = new ArrayList<NearNode>();
		
	}
	
	public Vec2 findPositionForFight(UnityBaseController owner,UnityBaseController enemy)
	{
		// 1) on détermine la position de l'enemy
		Vec2 posEnemy = enemy.getModel().getPosition();
		// 2) pour chaque node autour de la position enni
		for(int y = -1; y <= 1 ;y ++)
		{
			for(int x = -1; x <= 1 ; x++)
			{
				Vec2 search = posEnemy.add(new Vec2(x,y));
				// 3) on regarde si le node n'est pas un obstacle
				if(level.getModel().isNodeObstacle((int)search.x, (int)search.y))
					continue;
				// 4) on regarde si le node n'est pas sur un enemi
				if(!level.getModel().isNodeFree((int)search.x, (int)search.y,owner))
					continue;
				// 5) on regarde si le node n'est pas réservé
				if(ReservationManager.contain(search))
					continue;
				
				// 6) on calcul le cout et on crée un NearNode
				NearNode nearNode = new NearNode(search,owner.getModel().getPositionNode());
				listNode.add(nearNode);
			}
		}
		
		// 7) on trie
		Collections.sort(listNode);
		
		//8 ) on récupère le meilleurs
		if(listNode.size() > 0)
			return listNode.get(0).pos;
		else
			return owner.getModel().getPositionNode();
		
	
	}
}

class NearNode implements Comparable
{
	public Vec2 pos;
	public float cout;
	
	public NearNode(Vec2 pos,Vec2 posOwner)
	{
		this.pos = pos;
		this.cout = pos.sub(posOwner).abs().length();
	}
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		NearNode c = (NearNode)arg0;
		if(this.cout < c.cout)
			return 1;
		if(this.cout > c.cout)
			return -1;
		
			return 0;
	}
	
	

}

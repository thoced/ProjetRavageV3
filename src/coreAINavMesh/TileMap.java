package coreAINavMesh;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import coreAI.Node;

public class TileMap implements TileBasedMap 
{
	// map
	private Node[] map;
	// size map
	private int m_sizeX,m_sizeY;
	
	private int testmap[] = {0,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							1,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							0,0,0,0,0,0,0,0,
							1,1,1,1,0,0,0,0,
							1,1,1,1,0,0,0,0,
							0,0,0,0,0,0,0,0,};
							
	
	
	public TileMap(Node[] map,int sizeX,int sizeY)
	{
		this.map = map;
		this.m_sizeX = sizeX;
		this.m_sizeY = sizeY;

	}

	@Override
	public boolean blocked(PathFindingContext arg0, int x, int y) {
		// TODO Auto-generated method stub
		if(this.map[(m_sizeX * y) + x ].getType() != 0)
			return true;
		else
			return false;
		
		
		
	}

	@Override
	public float getCost(PathFindingContext arg0, int x, int y) {
		// TODO Auto-generated method stub
		return  1.0f;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return this.m_sizeY;
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return this.m_sizeX;
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}

package coreLevel;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.Sprite;

import CoreLoader.TiledObjectPolylinePoint;
import coreAI.Node;
import coreEntity.UnityBaseController;
import corePhysic.PhysicWorldManager;

public class LevelModel 
{
	// Tableau des valeurs Tiled (pour la recherche de chemin)
		private Node[] nodes;
	// size
		private int m_sizeX,m_sizeY;
		
		// Obstacles box2d
		private Body obstacles;
		
	public LevelModel(int mapSizeX,int mapSizeY) 
	{
		super();
		this.m_sizeX = mapSizeX;
		this.m_sizeY = mapSizeY;
		// instance des nodes
		nodes = new Node[m_sizeX * m_sizeY];
	}
	
	// ---------------------------------- indique si tous les nodes sont passable sur un vecteur donn�
	//
	// -----------------------------------------------------------------------------------------------
	
	public boolean isNodesCross(Vec2 start, Vec2 end)
	{
		// on calcul le vecteur de direction
		Vec2 dir = end.sub(start);
		// on normalize
		dir.normalize();
		
		// on boucle depuis le start vers le end et on regarde si tous les nodes sont blanc
		// si c'est le cas alors la ligne droite est libre
		Vec2 etape = start;
		while(end.sub(etape).length() > 1f)
		{
			if(this.isNodeObstacle((int)etape.x, (int)etape.y))
			{
				return false; // il y a un obstacle, on retourne false
			}
			
			// on avance
			etape = etape.add(dir);
		}
		
		return true; // aucun obstacle d�tect�, on retourne true
		
		
	}
	
	
	
	
	/**
	 * @return the m_sizeX
	 */
	public int getM_sizeX() {
		return m_sizeX;
	}



	/**
	 * @return the m_sizeY
	 */
	public int getM_sizeY() {
		return m_sizeY;
	}



	public Node[] getNodes() {
		return nodes;
	}

 

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}



	public void setObstacles(Body obstacles) {
		this.obstacles = obstacles;
	}



	public Body getObstacles() {
		return obstacles;
	}



	public boolean isNodeObstacle(int x,int y)
	{
		Node n = this.nodes[(this.m_sizeX * y) + x];
		if(n.getType() != 0)
			return true;
		else
			return false;
	}
	
	public boolean isNodeFree(int x,int y,UnityBaseController u)
	{
		return this.nodes[(this.m_sizeX * y) + x].isNodeFree(u);
	}
	public boolean isNodeReserved(int x,int y, UnityBaseController u)
	{
		return this.nodes[(this.m_sizeX) + x].isNodeReserved(u);
	}
	
	public Node bookNode(int x,int y,UnityBaseController u)
	{
		this.nodes[(this.m_sizeX * y) + x].bookNode(u);
		return this.nodes[(this.m_sizeX * y) + x];
	}
	
	public void unBookNode(int x,int y)
	{
		this.nodes[(this.m_sizeX * y) + x].unBookNode();
	}
	
	public Node takeNode(int x,int y,UnityBaseController u)
	{
		this.nodes[(this.m_sizeX * y) + x].takeNode(u);
		return this.nodes[(this.m_sizeX * y) + x];
	}
	
	public void releaseNode(int x,int y,UnityBaseController u)
	{
		this.nodes[(this.m_sizeX) + x].releaseNode(u);
	}
	
	public UnityBaseController getUnityOnNode(int x,int y)
	{
		return this.nodes[(this.m_sizeX * y) + x].getUnityOnNode(x, y);
	}
	
	public void InsertObstacle(List<TiledObjectPolylinePoint> listePoint,int x,int y,String typeobstacle) throws java.lang.RuntimeException
	{
		// ajout d'un obstacle de type polyline
		
		// on créer un bodydef
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.STATIC;
		bdef.bullet = false;
		// on determine 
		
		// on crée la chainshape
		ChainShape cs = new ChainShape();
		// on créer un vecteur de vec2 correspondant au nombre de point
		Vec2[] vectors = new Vec2[listePoint.size()];
		// on instance la liste des vecteurs
		for(int i=0;i<vectors.length;i++)
			vectors[i] = new Vec2();
		// on ajoute les vecteurs
		
		// on récupère la position initial de polyline
		float bx = x / PhysicWorldManager.getRatioPixelMeter();
		float by = y / PhysicWorldManager.getRatioPixelMeter();
		
		int ind = 0;
		for(Vec2 v : vectors)
		{
			// onrécupère les points de la listes
			float diffx = listePoint.get(ind).x / PhysicWorldManager.getRatioPixelMeter();
			float diffy = listePoint.get(ind).y / PhysicWorldManager.getRatioPixelMeter();
			// on ajoute la différence entre les coordonnées du pont initial et la liste des points
			v.set(bx + diffx,by + diffy);
			ind++;
			
		}
		// on ajoute le tout dans le chainshape
		try
		{
			cs.createChain(vectors, vectors.length);
			
			// création du body
			Body bodyObstacle = PhysicWorldManager.getWorld().createBody(bdef);
			bodyObstacle.setUserData(typeobstacle);
			
			// creation dufixture
			FixtureDef fixture = new FixtureDef();
			fixture.shape = cs;
			fixture.friction = 0.6f;
			fixture.density = 1f;
			fixture.restitution = 0.0f;
	
			// ajout dans le body
			bodyObstacle.createFixture(fixture);
		}
		catch(java.lang.RuntimeException e)
		{
			
		}
		
		
	}
		
		
}

package coreAI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jbox2d.common.Vec2;
import org.jsfml.system.Time;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.navmesh.NavMesh;
import org.newdawn.slick.util.pathfinding.navmesh.NavMeshBuilder;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;
import org.newdawn.slick.util.pathfinding.navmesh.Space;

import coreAINavMesh.Agent;
import coreAINavMesh.TileMap;
import coreLevel.LevelManager;
import ravage.IBaseRavage;

public class AstarManager extends Thread implements IBaseRavage 
{


	
	private static AStarPathFinder navmesh;
	
	private static Agent agent;
	
	private final int MAX_AVAILABLE = 1;

	private static List<AskPath> listAsk;
	
	private static Semaphore semaphore;
	
	private static Lock lock;
	
	@Override
	public void init() 
	{
		
		agent = new Agent();
		// cr�ation du tilemap
		TileMap tilemap = new TileMap(LevelManager.getLevel().getModel().getNodes(),
				LevelManager.getLevel().getModel().getM_sizeX(),
				LevelManager.getLevel().getModel().getM_sizeY());
	
		// instance du navmesh
		//NavMeshBuilder build = new NavMeshBuilder();
		//navmesh = build.build(tilemap);
		navmesh = new AStarPathFinder(tilemap, 1024, true);
	
			
		// instance du star
		//star = new Astar();
		// instance de la liste des demandes
		this.listAsk = new ArrayList<AskPath>();
		// instance du semaphore
		semaphore = new Semaphore(MAX_AVAILABLE,true);
		// instance du lock
		lock = new ReentrantLock();
		// on lance le thread
		this.start();
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		super.run();
		
		//
		try
		{
			while(this.isAlive())
			{
					// on prend le semaphore
					semaphore.acquire();
					// on récupère la demande dans liste
					if(this.listAsk.size() > 0)
					{
						lock.lock();
							AskPath ask = this.listAsk.get(0); // récupèration de la demande 
							this.listAsk.remove(0); // suppresion de la demande
						lock.unlock();
						
						try
						{
							// on lance une recherche de chemin				
							Path paths = this.navmesh.findPath(null,(int)ask.getPositionInitial().x,(int) ask.getPositionInitial().y, (int)ask.getPositionFinal().x, (int)ask.getPositionFinal().y);
							// on appel le caller
							if(paths != null)
								ask.getCaller().onCallsearchPath(paths);
						
						}catch(ArrayIndexOutOfBoundsException aae)
						{
							
						}
						
					}
					
				
			}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/*public static void askPath(ICallBackAStar caller,int posx,int posy,int targetx,int targety) throws InterruptedException
	{
		// on créé une demande
		AskPath ask = new AskPath(caller,posx,posy,targetx,targety);
		
		
		// on place la demande dans la liste
		lock.lock();
			listAsk.add(ask);
		lock.unlock();
		
		semaphore.release();
		
		
	}*/
	
	public static void askPath(ICallBackAStar caller,Vec2 position,Vec2 positionFinal) throws InterruptedException
	{
		// on créé une demande
		AskPath ask = new AskPath(caller,position,positionFinal);
		
		
		// on place la demande dans la liste
		lock.lock();
			listAsk.add(ask);
		lock.unlock();
		
		semaphore.release();
		
		
	}

	
	

}

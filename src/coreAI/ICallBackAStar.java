package coreAI;

import java.util.List;

import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

public interface ICallBackAStar 
{
	//public void onCallSearchPath(List<Node> finalPath);
	//public void onCallSearchPath(NavPath finalPath);
	public void onCallsearchPath(Path finalPath);
}

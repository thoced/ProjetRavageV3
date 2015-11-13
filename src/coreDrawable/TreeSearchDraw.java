package coreDrawable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import coreEntity.UnityBaseController;
import coreEntity.UnityBaseModel;
import coreEntity.UnityNetController;

public class TreeSearchDraw implements QueryCallback 
{
	private List<UnityBaseController> listDraw;
	
	public TreeSearchDraw()
	{
		listDraw = new ArrayList<UnityBaseController>();
	}
	@Override
	public boolean reportFixture(Fixture arg0)
	{
		// TODO Auto-generated method stub
		Body b = arg0.getBody();
		if(b.getUserData() != null && b.getUserData().getClass() !=  String.class )
		{
			listDraw.add((UnityBaseController)b.getUserData());
		}
		
		return true;
	}
	
	public void clear()
	{
		// clear de la liste
		listDraw.clear();
	
	}
	
	public Iterator<UnityBaseController> getIterator()
	{
	//	System.out.println("Unite Affichée: " + listDraw.size());
		return listDraw.iterator();
	}

}

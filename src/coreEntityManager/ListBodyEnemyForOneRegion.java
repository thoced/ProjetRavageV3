package coreEntityManager;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import coreEntity.UnityBaseController;
import coreEntity.UnityNetController;

public class ListBodyEnemyForOneRegion implements QueryCallback {

	
	// list des body dans la région
	private List<UnityNetController> listUnityEnemy;
	
	public ListBodyEnemyForOneRegion()
	{
		// instance de listbody
		this.listUnityEnemy = new ArrayList<UnityNetController>();
	}
	
	@Override
	public boolean reportFixture(Fixture arg0) 
	{
		//on récupère le body
		Body b = arg0.getBody();
		// si le userdate provient de la class unity on incrément le nb
		if(b.getUserData() != null && b.getUserData().getClass() != String.class)
			{
				if(b.getUserData().getClass() == UnityNetController.class)
					{
						// ajout dans la liste body
						this.listUnityEnemy.add((UnityNetController)b.getUserData());
					}
			}
		
		return true;
	}
	
	public int getNbBodyInList()
	{
		// return du nb de body
		return this.listUnityEnemy.size();
	}

	public List<UnityNetController> getListEnemy() {
		return listUnityEnemy;
	}

	
	

}

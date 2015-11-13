package coreMessageManager;

import java.util.ArrayList;
import java.util.List;

public class ListRegistration extends ArrayList<IPumpMessage>
{
	
	
	public IPumpMessage getObject(Class idClass)
	{
		for(IPumpMessage obj : this)
		{
			if(obj.getClass() == idClass)
				return obj;
		}
		
		return null;
	}


	
}

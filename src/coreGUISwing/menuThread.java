package coreGUISwing;

import org.jsfml.system.Time;

import coreNet.NetManager;

public class menuThread extends Thread 
{
	private NetManager netManager;
	
	public menuThread(NetManager netManager)
	{
		this.netManager = netManager;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		super.run();
		
		try
		{
			while(this.isAlive())
			{
			Thread.sleep(100);
					
			this.netManager.update(Time.ZERO);
		
				
			}
		}catch(InterruptedException e)
		{
			
		}
		
	
	}

}

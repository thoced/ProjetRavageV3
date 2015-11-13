package coreNet;

public interface INetManagerCallBack 
{
	public void onHello(NetHello hello);
	
	public void onCreateUnity(NetDataUnity unity);
	
	public void onUpdateUnity(NetDataUnity unity);
	
	public void onSynchronised(NetDeleteSynchronised sync);
	
	/*public void onMoveUnity(NetMoveUnity unity);
	
	public void onSynchronize(NetSynchronize sync);
	
	public void onStrike(NetStrike strike);
	
	public void onKill(NetKill kill);*/
		
}

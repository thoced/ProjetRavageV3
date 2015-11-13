package coreNet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsfml.system.Time;

import coreEntity.Unity;
import coreGUI.IRegionSelectedCallBack;
import coreGUI.RectSelected;
import coreNet.NetHeader.TYPE;
import ravage.IBaseRavage;

public class NetManager implements IBaseRavage
{
	private final static  int MAX_INFO_IN_DATAGRAM = 4; 
	
	private static Lock lock;
	
	private static Lock lockSend;
	
	private static List<NetHeader> listNetMessage;
	
	//private static List<NetDatagram> listNetDatagram;
	private static List<NetBase> listNetDatagram;
	
	// list des callback attachés au netmanager
	private static  List<INetManagerCallBack> listCallBack;
	
	// NetReceiverThread
	private NetReceiverThread netReceiver;
	// NetSendThread
	private NetSendThread netSend;
	// Datagramsocket emission
	private static DatagramSocket socketEmission;
	// Inetadress
	private static InetAddress inet;
	
	// Positiion flags start
	private static int posxStartFlag;
	private static int posyStartFlag;
	

	
	// port adress
	private int portEcoute;
	private int portDestination;
			
	public NetManager()
	{
		// instance du listcallback
		listCallBack = new ArrayList<INetManagerCallBack>();
		// instance de listNetMessage
		listNetMessage = new ArrayList<NetHeader>();
		listNetDatagram = new ArrayList<NetBase>();
		// instance du lock
		lock = new ReentrantLock();
		lockSend = new ReentrantLock();
		// instance de socket emission
		// instance de NetDatagram
		//netDatagram = new NetDatagram();
		
		
		// port
		
						
	}
	
	
	
	



	public static void attachCallBack(INetManagerCallBack obj) 
	{
		// attach de callback
		listCallBack.add( obj);
	}
	
	/*public static void createNetDatagram()
	{
		netDatagram = new NetDatagram();
	}
*/
	public static void pushNetMessage(NetHeader header)
	{
		lock.lock();
		
		listNetMessage.add(header);
		
		lock.unlock();
	}
	
	public static void pushNetDatagram(NetBase data)
	{
		lock.lock();
		
		listNetDatagram.add(data);
		
		lock.unlock();
	}
		
	
	@Override
	public void init()
	{
		// lancement du NetReceiverThread
		netReceiver = new NetReceiverThread();
		netReceiver.start();
		netSend = new NetSendThread();
		netSend.start();
		
	}
	
	public static void configureIp(String ip) throws UnknownHostException, SocketException
	{
		// configure l'inetadress du joueur adverse
		//inet = InetAddress.getByName(ip);
		// configure la socket
		//socketEmission = new DatagramSocket();
		
		NetSendThread.configureIp(ip);
	}
	
	/*public static void SendMessage(NetHeader header) throws IOException
	{
		// création du message en buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(header);
		// on récupère le buffer array
		byte[] buffer = new byte[baos.size()];
		buffer = baos.toByteArray();
		// création d'un datagramudp
		DatagramPacket datagram = new DatagramPacket(buffer,buffer.length);
		datagram.setPort(1234);
		datagram.setAddress(inet);
		// emission
		if(socketEmission != null)
		socketEmission.send(datagram);
		
		
	}*/
	
	
	
	
	
	
	@Override
	public void update(Time deltaTime) 
	{
			
		
		// on vérifie si il n'y a pas qlq chose dans le listnetmessage
		lock.lock();	// lock du semaphore
		
		
		// code de réception de données par le réseau
		while(listNetDatagram.size() > 0) // si il y a au moins 1 nouveau message arrivé
		{
			// on récupère le Netdatagram
			NetBase data = listNetDatagram.get(0);
			// on supprime le premier de la liste
			listNetDatagram.remove(0);
			
			// on décapsule l'ensemble des NetHeader présent dans le datagram
			
					if(data != null)
						dispatcher(data);
				
		}
		
		// clear du listnetdatagram
			listNetDatagram.clear();
			listNetDatagram = new ArrayList<NetBase>();
		
		lock.unlock(); // unlock du semaphore


	}
	
	private void dispatcher(NetBase data)
	{
		switch(data.getTypeMessage())
		{
			case HELLO:  NetHello hello = (NetHello)data;
						callBackHello(hello);
						break;
						
			case CREATE: NetDataUnity create = (NetDataUnity)data;
						 callBackCreate(create);
						 break;
						 
			case UPDATE: 
						NetDataUnity update = (NetDataUnity)data;
						callBackUpdate(update);
						break;
						
			case SYNCHRONISED: 
						NetDeleteSynchronised sync = (NetDeleteSynchronised)data;
						callBackDeleteSynchronised(sync);
						break;
						
						
				
										   	
			default: break;
		}
	}
	
	private void callBackDeleteSynchronised(NetDeleteSynchronised sync)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onSynchronised(sync);
		}
	}
	
	private void callBackHello(NetHello hello)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onHello(hello);
		}
	}
	
	private void callBackCreate(NetDataUnity unity)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onCreateUnity(unity);
		}
	}
	
	private void callBackUpdate(NetDataUnity unity)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onUpdateUnity(unity);
		}
	}
	
	
	
	/*private void callBackMove(NetMoveUnity unity)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onMoveUnity(unity);
		}
	}
	
	private void callBackSync(NetSynchronize sync)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onSynchronize(sync);
		}
	}
	
	private void callBackStrike(NetStrike strike)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onStrike(strike);
		}
	}
	
	public void callBackKill(NetKill kill)
	{
		for(INetManagerCallBack i : listCallBack)
		{
			i.onKill(kill);
		}
	}
	*/
	public void close()
	{
		// fermeture de la connection et arret du thread
		netReceiver.closeConnection();
		netSend.closeThread();
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		this.close();
		
	}

	public static int getPosxStartFlag() {
		return posxStartFlag;
	}

	public static void setPosxStartFlag(int posxStartFlag) {
		NetManager.posxStartFlag = posxStartFlag;
	}

	public static int getPosyStartFlag() {
		return posyStartFlag;
	}

	public static void setPosyStartFlag(int posyStartFlag) {
		NetManager.posyStartFlag = posyStartFlag;
	}
	
}

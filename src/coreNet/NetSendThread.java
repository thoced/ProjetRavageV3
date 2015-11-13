package coreNet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetSendThread extends Thread 
{
	private final int TIME_TO_SLEEP = 10;
	
	// list linked FIFO
	private static LinkedList<NetBase> ListSend = new LinkedList<NetBase>();
	
	// Socket
	private static DatagramSocket socketEmission;
	private static InetAddress inet;
	// Semaphore
	private static Semaphore sem = new Semaphore(0);
	// Lock (mutext)
	private static Lock lock = new ReentrantLock();

	@Override
	public void run()
	{
		
		
	try
	{
		while(this.isAlive())
		{
			
				
			try {
				// attente de 10 ms
				this.sleep(TIME_TO_SLEEP);
				// on prend le jeton semphore
				sem.acquire();
				// on lock un verrou sur la list
				lock.lock();
				// récupération d'un data et emission sur le réseau
				this.Send(this.ListSend.removeFirst());
				// on déverouille
				lock.unlock();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
		}
	}
		catch(InterruptedException se)
		{
			System.out.println("THREAD NetSend interrupt");
		}
		 
	}
	
	public void closeThread()
	{
		this.interrupt();
	}
	
	public static void configureIp(String ip) throws UnknownHostException, SocketException
	{
		// configure l'inetadress du joueur adverse
		inet = InetAddress.getByName(ip);
		// configure la socket
		socketEmission = new DatagramSocket();
	}
	
	public static void push(NetBase netData)
	{
		// on lock la list
		lock.lock();
		// on dépose dans la liste
		ListSend.add(netData);
		// on délock
		lock.unlock();
		// on précise que l'on a ajouté un jeton
		sem.release();
	}

	private void Send(NetBase netData) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(netData);
		// on récupère le buffer array
		byte[] buffer = new byte[baos.size()];
		buffer = baos.toByteArray();
		// création d'un datagramudp
		DatagramPacket datagram = new DatagramPacket(buffer,buffer.length);
		System.out.println("taille paquet : " + buffer.length);
		datagram.setPort(1234);
		datagram.setAddress(inet);
		// emission
		if(socketEmission != null)
			socketEmission.send(datagram);
		
		oos.close();
		baos.close();
	}
	
	
}

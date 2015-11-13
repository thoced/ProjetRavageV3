package coreNet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetReceiverThread extends Thread 
{
	// num port receiver
	private final int PORT = 1234;
	// size of buffer
	private final int SIZEBUFFER = 16384;
	// buffer de réception 
	private byte[] buffer;
	// Datagram Socket de réception
	private DatagramSocket  socketReceiver;

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
		
		try
		{
			// instance du datagram receiver	
			socketReceiver = new DatagramSocket(PORT);
			// création du datagram packet 
			buffer = new byte[SIZEBUFFER];
			DatagramPacket datagram = new DatagramPacket(buffer,SIZEBUFFER);

			while(this.isAlive())
			{
				// reception bloquante d'un datagram udp
				socketReceiver.receive(datagram);
				// on vient de réceptionner un datagram
				// on envoie le tout dans le NetManager
				byte[] b = new byte[datagram.getLength()];
				b = datagram.getData();
				// transformation dans un bytearrayinputstream
				ByteArrayInputStream bais = new ByteArrayInputStream(b);
				ObjectInputStream ois = new ObjectInputStream(bais);
				//NetHeader header = (NetHeader) ois.readObject();
				//NetDatagram data = (NetDatagram) ois.readObject();
				NetBase data = (NetBase) ois.readObject();
				// push dans le netmanager
				//NetManager.pushNetMessage(header);
				
				/// push des NetDatagram
				NetManager.pushNetDatagram(data);
				
			}
			
		} 
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("SOCKET FAILED 1" + e.getMessage());
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("SOCKET FAILED 2" + e.getMessage());
		} catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("SOCKET FAILED 3" + e.getMessage());
		} 
		
		
	}
	
	public void closeConnection()
	{
		// fermeture
		socketReceiver.close();
	}

}

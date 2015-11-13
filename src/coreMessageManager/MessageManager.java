package coreMessageManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsfml.system.Time;

import ravage.IBaseRavage;

public class MessageManager implements IBaseRavage 
{
	// Model
	private static Model m_model = new Model();
	
	// Lock Registration Object
	private static Lock m_lockRegistration = new ReentrantLock();
	
	// Lock
	private static Lock m_lock  = new ReentrantLock();
	
	public MessageManager()
	{
		
	
	}
	
	public static void sendMessage(MessageRavage message)
	{
		m_lock.lock();
		
		m_model.m_messages.add(message); // push du message dans la pompe
		
		m_lock.unlock();
	}
	
	public static void registrationObject(IPumpMessage object)
	{
		m_lockRegistration.lock();
		
		m_model.m_registrations.add(object); // enregistrement d'un objet voulant utilisé la pompe à messages
		
		m_lockRegistration.unlock();
	}
	
	
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Time deltaTime) 
	{
		// BOUCLE DANS LA POMPE A MESSAGE
		m_lock.lock();
			
		while(!m_model.m_messages.isEmpty())
		{
			MessageRavage message = m_model.m_messages.removeFirst(); // on récupère le premier message
			IPumpMessage obj = m_model.m_registrations.getObject(message.receiverClass); // on récupère l'objet registration
			if(obj != null)
				obj.OnPumpMessage(message); // on envoie le message
		}
		
		m_lock.unlock();

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	static class Model
	{
		// parent controller
		private MessageManager m_controller;
		// pile de messages
		private LinkedList<MessageRavage> m_messages;
		// pile d'objet attaché
		private ListRegistration m_registrations;
		
		
		public Model()
		{
			
			// instance de la pile de messages
			m_messages = new LinkedList<MessageRavage>();
			// instance de la pile d'objet atatché
			m_registrations = new ListRegistration();
		
		}
	}

}

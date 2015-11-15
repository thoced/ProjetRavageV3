package coreSounds;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.*;
import javax.sound.sampled.*;

import org.jsfml.graphics.Drawable;
import org.jsfml.system.Time;

import ravage.IBaseRavage;
import sun.audio.AudioStream;
import coreNet.NetBase;



public class SoundsManager extends Thread implements IBaseRavage
{
	// list FIFO
	private static LinkedList<TYPE_SOUNDS> listSounds = new LinkedList<TYPE_SOUNDS>();
	// Semaphore
	private static Semaphore sem = new Semaphore(0);
	// Lock (mutext)
	private static Lock lock = new ReentrantLock();
	
	public enum TYPE_SOUNDS  {MUTE,WALK_SOUNDS,STRIKE_SOUNDS,CRY_SOUNDS};
	
	
	// banque de sons
	private Clip[] clipStrike; 
	private final int NB_STREAM_STRIKE = 9;
	private Clip[] clipCry; 
	private final int NB_STREAM_CRY = 11;
	
	// Random
	private static Random randomStrike = new Random();
	private static Random randomCry = new Random();
	


	
	
	public SoundsManager() 
	{
		super();
		
		
		
		  try 
		  {
			
			  
			  clipStrike = new Clip[NB_STREAM_STRIKE];
			  for(int i=0;i<clipStrike.length;i++)
			  {
				  InputStream in = SoundsManager.class.getResourceAsStream("/SOUNDS/cling" + (i+1) + ".wav");
				  clipStrike[i] = AudioSystem.getClip();
				  clipStrike[i].open(AudioSystem.getAudioInputStream(in));
				

			  }
			  
			  clipCry = new Clip[NB_STREAM_CRY];
			  for(int i=0;i<clipCry.length;i++)
			  {
				  InputStream in = SoundsManager.class.getResourceAsStream("/SOUNDS/cry" + (i+1) + ".wav");
				  clipCry[i] = AudioSystem.getClip();
				  clipCry[i].open(AudioSystem.getAudioInputStream(in));
				

			  }
			  
		
			  
		  } catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		


		
	}
	
	public static void PlaySounds(TYPE_SOUNDS type)
	{
		// lock du processus
		lock.lock();
		// ajout du type dans la liste
		listSounds.add(type);
		// unlock du processus
		lock.unlock();
		// on précise qu'un nouvelle élément est dans la liste
		sem.release();
	}
	
	private void Play(TYPE_SOUNDS type)
	{
		try
		{
		
			switch(type)
			{
				case MUTE : break;
				
				case CRY_SOUNDS: PlayCrySounds();break;
				
				case STRIKE_SOUNDS: PlayStrikeSounds(); break;
				
				default: break;
			}
		}
		catch(LineUnavailableException l)
		{
			
		}
		catch(IOException io)
		{
			
		}
	}
	
	private void PlayStrikeSounds() throws LineUnavailableException, IOException
	{
		
		// random du son
	
		int indice = randomStrike.nextInt(NB_STREAM_STRIKE - 1);
		
		clipStrike[indice].stop();
		clipStrike[indice].setFramePosition(0);
		clipStrike[indice].start();
		
	}
	
	private void PlayCrySounds() throws LineUnavailableException, IOException
	{
		
		// random du son
	
		int indice = randomCry.nextInt(NB_STREAM_CRY - 1);
		
		clipCry[indice].stop();
		clipCry[indice].setFramePosition(0);
		clipCry[indice].start();
		
	}

	@Override
	public void run() 
	{
	
		super.run();
		
		try
		{
		
			while(this.isAlive())
			{
				// acquisition du semaphore si un nouveau son doit être joué
				Thread.sleep(20);
				
				sem.acquire();
				// lock du processus
				lock.lock();
				// récupération du type du son à jouer
				Play(listSounds.removeFirst());
				// unlock
				lock.unlock();
			}
			
		}catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("SOCKET FAILED 2" + e.getMessage());
		}
	}

	@Override
	public void init()
	{
		// lancement du thread
	   this.start();
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void closeThread()
	{
		
		
		this.interrupt();
	}

	
	
}

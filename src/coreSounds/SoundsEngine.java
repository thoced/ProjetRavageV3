package coreSounds;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.*;
import javax.sound.sampled.*;

public class SoundsEngine
{

	
	
	public SoundsEngine() 
	{
		super();
		
		  try {
		         // Open an audio input stream.
		         InputStream url = SoundsEngine.class.getResourceAsStream("/cling.wav");
		         InputStream url2 = SoundsEngine.class.getResourceAsStream("/cling2.wav");
		         InputStream url3 = SoundsEngine.class.getResourceAsStream("/cling3.wav");
		         InputStream url4 = SoundsEngine.class.getResourceAsStream("/cling4.wav");
		         InputStream url5 = SoundsEngine.class.getResourceAsStream("/cling5.wav");
		         InputStream url6 = SoundsEngine.class.getResourceAsStream("/cling6.wav");
		         
		        
		         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
		         AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(url2);
		         AudioInputStream audioIn3 = AudioSystem.getAudioInputStream(url3);
		         AudioInputStream audioIn4 = AudioSystem.getAudioInputStream(url4);
		         AudioInputStream audioIn5 = AudioSystem.getAudioInputStream(url5);
		         AudioInputStream audioIn6 = AudioSystem.getAudioInputStream(url6);
		         // Get a sound clip resource.
		         Clip clip = AudioSystem.getClip();
		         Clip clip2 = AudioSystem.getClip();
		         Clip clip3 = AudioSystem.getClip();
		         Clip clip4 = AudioSystem.getClip();
		         Clip clip5 = AudioSystem.getClip();
		         Clip clip6 = AudioSystem.getClip();
		        
		         // Open audio clip and load samples from the audio input stream.
		        
		         clip.open(audioIn);
		         clip2.open(audioIn2);
		         clip3.open(audioIn3);
		         clip4.open(audioIn4);
		         clip5.open(audioIn5);
		         clip6.open(audioIn6);
		           
		     
		      
		         try
		         {
		        	clip.start();
					Thread.sleep(200);
					clip2.start();
					Thread.sleep(200);
					clip3.start();
					Thread.sleep(200);
					clip4.start();
					Thread.sleep(200);
					clip5.start();
					Thread.sleep(200);
					clip6.start();
					Thread.sleep(200);
					
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		         
		         
		         
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		   
		
	}

	
	
}

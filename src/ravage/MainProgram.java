package ravage;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import CoreLoader.*;

public class MainProgram 
{
	public static void main(String[] args) 
	{
		
		/// instance du framework
		FrameWork frameWork = new FrameWork();
		try
		{
			try {
				try {
					frameWork.init();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (TextureCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: MainProgram: " + e.getMessage());
		}
		
		// framework run
		frameWork.run();
		
		
	}

}

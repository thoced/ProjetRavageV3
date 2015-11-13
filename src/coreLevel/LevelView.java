package coreLevel;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

import coreCamera.CameraManager;

public class LevelView implements Drawable
{
	// Textures de background
	private List<Sprite> backgrounds;
	// Textures de foreground - Arbres et toits
	private List<Sprite> foregrounds;
	// Textures de shadow
	private List<Sprite> shadowgrounds;
	// Texture de lightmap
	private List<Sprite> lightgrounds;
	
	
	
	
	public LevelView()
	{
		super();
		// instances du background
		backgrounds = new ArrayList<Sprite>();
		// instance de foreground
		foregrounds = new ArrayList<Sprite>();
		// instance du shadow
		shadowgrounds = new ArrayList<Sprite>();
		// instance du light map
		lightgrounds = new ArrayList<Sprite>();
		
	}



	public List<Sprite> getBackgrounds() {
		return backgrounds;
	}



	public List<Sprite> getForegrounds() {
		return foregrounds;
	}
	
	

	public List<Sprite> getShadowgrounds() {
		return shadowgrounds;
	}
	
	



	public List<Sprite> getLightgrounds() {
		return lightgrounds;
	}



	public void drawBackground(RenderTarget render,RenderStates state)
	{
		// on affiche le background du level
		
				for(Sprite s : backgrounds)
				{
					FloatRect result = s.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(s);
				}
	}
	
	public void drawForeground(RenderTarget render,RenderStates state)
	{
		// on affiche le foreground
				for(Sprite f : foregrounds)
				{
					FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(f);
				}
	}
	
	public void drawShadowground(RenderTarget render,RenderStates state)
	{
		// on affiche le foreground
				for(Sprite f : shadowgrounds)
				{
					FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(f);
				}
	}
	
	public void drawLightground(RenderTarget render,RenderStates state)
	{
		// on affiche le foreground
				for(Sprite f : lightgrounds)
				{
					FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
					if(result != null)
						render.draw(f);
				}
	}

	@Override
	public void draw(RenderTarget render, RenderStates state)
	{
		for(Sprite s : backgrounds)
		{
			FloatRect result = s.getGlobalBounds().intersection(CameraManager.getCameraBounds());
			if(result != null)
				render.draw(s);
		}
		
		// affichage du shadogrounds
		
		for(Sprite s : shadowgrounds)
		{
			FloatRect result = s.getGlobalBounds().intersection(CameraManager.getCameraBounds());
			if(result != null)
				render.draw(s);
		}
		
		// on affiche le foreground
		for(Sprite f : foregrounds)
		{
			FloatRect result = f.getGlobalBounds().intersection(CameraManager.getCameraBounds());
			if(result != null)
				render.draw(f);
		}
		
	}

}

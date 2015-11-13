package coreLevel;

import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

public class ForegroundLevel implements Drawable {

	private List<Sprite> foregrounds;
	
	
	
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		

	}



	public List<Sprite> getForegrounds() {
		return foregrounds;
	}
	
	

}

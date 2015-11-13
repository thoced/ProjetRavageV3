package coreEntity;

import org.jbox2d.common.Vec2;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class PiquierView extends UnityBaseView
{
	
	
	public PiquierView(UnityBaseModel model, UnityBaseController controller) {
		super(model, controller);
	
	}
	
	

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		super.draw(arg0, arg1);
		
	}



	@Override
	public void prepareAnimations(Animations animations)
	{
		animations.makeAnimation(27);
		
		for(int i=0;i<animations.getAnimations().length;i++)
		{
			animations.getAnimations()[i] = new IntRect(i*80,0 ,80, 80);
		}
		
	}



	

}

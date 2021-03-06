package coreEntity;

import java.io.Serializable;

import org.jsfml.graphics.IntRect;

public class Animations implements Serializable
{
	private IntRect[] animations;

	public IntRect[] getAnimations() {
		return animations;
	}
	
	public int lenght()
	{
		if(animations != null)
			return animations.length;
		else
			return 0;
	}
	
	public void makeAnimation(int totalFrame)
	{
		animations = new IntRect[totalFrame];
	}
	
	public  IntRect getInd(int i)
	{
		return animations[i];
	}
	
	public void append(int ind,IntRect r)
	{
		animations[ind] = r;
	}
	
	
}

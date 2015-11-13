package coreEntityManager;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;

public class Blood 
{
	private Sprite spriteBlood;
	
	private Time elapsedTimeBlood;

	public Blood()
	{
		elapsedTimeBlood = Time.ZERO;
	}
	
	public Sprite getSpriteBlood() {
		return spriteBlood;
	}

	public Time getElapsedTimeBlood() {
		return elapsedTimeBlood;
	}

	public void setSpriteBlood(Sprite spriteBlood) {
		this.spriteBlood = spriteBlood;
	}

	public void setElapsedTimeBlood(Time elapsedTimeBlood) {
		this.elapsedTimeBlood = elapsedTimeBlood;
	}
	
	
}

package coreLevel;

import org.jsfml.system.Time;

public class LevelArena01 extends LevelController 
{
	

	public LevelArena01() {
		super();
		// chargement du niveau
		managerLevel.loadLevel(this,"levelarena01.json");
				
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
	}
	
}

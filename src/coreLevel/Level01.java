package coreLevel;

import org.jsfml.system.Time;

public class Level01 extends LevelController 
{
	

	public Level01() {
		super();
		// chargement du niveau
		managerLevel.loadLevel(this,"testlevel01.json");
				
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

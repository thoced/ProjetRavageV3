package coreEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreTexturesManager.TexturesManager;
import coreDrawable.DrawableUnityManager;
import coreDrawable.DrawableUnityManager.LAYERS;
import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class BloodManager implements IBaseRavage,Drawable 
{
	// list des sprites blood
	private static List<Blood> listBlood;
	// liste destruct blood
	private static List<Blood> listDestroyBlood;
	// tableau de texturerect
	private static IntRect[] listIntRect;
	// tableau des textures de cadavres
	private static IntRect[][] listCadavres;  //[x][y]
	
	// texture blood
	private static Texture textureBlood;
	// Nombre de type de blood
	private final int MAX_IND_BLOOD = 15;
	// texture des cadavres
	private static Texture textureCadavre;
	// Nombre de type de cadavre
	private final int MAX_IND_CADAVRE = 1;
	
	@Override
	public void init()
	{
		// on s'inscrit au DrawableUnityManager
		DrawableUnityManager.AddDrawable(this,LAYERS.BACK);
		// on créé les listes
		listBlood = new ArrayList<Blood>();
		listDestroyBlood = new ArrayList<Blood>();
		// chargement de la texture blood
		textureBlood = new Texture(TexturesManager.GetTextureByName("Blood_Splatt.png"));
		//chargement de la texture des cadavres
		textureCadavre = new Texture(TexturesManager.GetTextureByName("cadavres.png"));
		// instance des intrects
		listIntRect = new IntRect[MAX_IND_BLOOD];
		listCadavres = new IntRect[MAX_IND_CADAVRE][2];
		
		int tx = 0;
		for(int i=0;i<MAX_IND_BLOOD;i++)
		{
			listIntRect[i] = new IntRect(tx,0,32,32);
			tx+=32;
		}
		// instance des intrect pour le cadavre
		for(int y=0;y<2;y++)
		{
			for(int x=0;x<MAX_IND_CADAVRE;x++)
			{
				
				listCadavres[x][y] = new IntRect(x*32,y*32,32,32);
				
			}
		}

	}

	@Override
	public void update(Time deltaTime) 
	{
		for(Blood blood : listBlood)
		{
			Time elapse = blood.getElapsedTimeBlood();
			blood.setElapsedTimeBlood(Time.add(elapse, deltaTime));
			// si le sang est resté plus de 5 seconde
			if(blood.getElapsedTimeBlood().asSeconds() > 120f)
				listDestroyBlood.add(blood);
				
		}
		
		// on supprime de la liste
		listBlood.removeAll(listDestroyBlood);
		listDestroyBlood.clear();
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	public static void addBlood(Vec2 posBlood)
	{
		
		Sprite sprite = new Sprite(textureBlood);
		sprite.setPosition(new Vector2f(posBlood.x * PhysicWorldManager.getRatioPixelMeter(),posBlood.y * PhysicWorldManager.getRatioPixelMeter()));
		sprite.setOrigin(new Vector2f(16f,16f));
		// on cré un rand pour la selection aléatoire du sang
		Random rand = new Random();
		int randomNum = rand.nextInt(15);
		sprite.setTextureRect(listIntRect[randomNum]);
		// random de l'angle du sang
		float angleBlood = rand.nextFloat();
		angleBlood = angleBlood * 360f;
		sprite.setRotation(angleBlood);
		Blood blood = new Blood();
		blood.setSpriteBlood(sprite);
		
		// ajout dans la liste
		listBlood.add(blood);
	}
	
	public static void addUnityKilled(Vec2 posBody,EntityManager.CAMP camp)
	{
		int indy = 0;
		switch(camp)
		{
			case BLUE : indy = 1;break;
			
			case YELLOW:indy = 0;break;
		}
	
		Sprite sprite = new Sprite(textureCadavre);
		sprite.setPosition(new Vector2f(posBody.x * PhysicWorldManager.getRatioPixelMeter(),posBody.y * PhysicWorldManager.getRatioPixelMeter()));
		sprite.setOrigin(new Vector2f(16f,16f));
	
		// on place l'indice 15 qui est le corp décédé
		sprite.setTextureRect(listCadavres[0][indy]);
		// random de l'angle du sang
		Random rand = new Random();
		float angleBlood = rand.nextFloat();
		angleBlood = angleBlood * 360f;
		sprite.setRotation(angleBlood);
		Blood blood = new Blood();
		blood.setSpriteBlood(sprite);
		
		// ajout dans la liste
		listBlood.add(blood);
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		for(Blood blood : listBlood)
		{
			arg0.draw(blood.getSpriteBlood());
		}
		
	}

}

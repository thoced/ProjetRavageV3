package coreDrawable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.scene.Camera;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreTexturesManager.TexturesManager;
import coreCamera.CameraManager;
import coreEntity.Knight;
import coreEntity.Unity;
import coreEntity.UnityBaseController;
import coreEntity.UnityNetController;
import coreEntityManager.EntityManager;
import coreEntityManager.EntityManager.CAMP;
import corePhysic.PhysicWorldManager;
import ravage.IBaseRavage;

public class DrawableUnityManager implements IBaseRavage, Drawable
{

	public static enum LAYERS {BACK,MIDDLE,FRONT};

	// listCallBack remove
	private static List<Drawable> listCallBackRemove;
	
	// listCallback des drawables layers back
	private static List<Drawable> listCallBackDrawableBACK;
	// listCallback des drawables layers middle
	private static List<Drawable> listCallBackDrawableMIDDLE;
	// listCallback des drawables layers middle
	private static List<Drawable> listCallBackDrawableFRONT;
	
	private VertexArray buffer;
	
	private RenderStates state = new RenderStates(TexturesManager.GetTextureByName("unity_sprite_01.png"));
	
	
	// test utilisation de sprite pour l'affichage des unités
	private Sprite sprite_unite;
	
	// KNIGHTS
	private Sprite sprite_knight_YELLOW;
	private Sprite sprite_knight_BLUE;
	
	// SPIRTE CURRENT
	private Sprite SpriteCurrent;
	private Sprite SpriteCurrentNet;
	
	// Query de recherche d'affichage
	private TreeSearchDraw treeSearch;

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// crÃ©ation du VertexBuffer
	
	 
	 // instance de treeSearchDraw
	 treeSearch = new TreeSearchDraw();
	 
	 // instance du listCallBackDrawable
	 listCallBackDrawableBACK = new ArrayList<Drawable>();
	 listCallBackDrawableMIDDLE = new ArrayList<Drawable>();
	 listCallBackDrawableFRONT = new ArrayList<Drawable>();
	 listCallBackRemove = new ArrayList<Drawable>();
	 
	 // instance de sprite_unite
	 sprite_knight_YELLOW = new Sprite(TexturesManager.GetTextureByName("ANIM_KNIGHT_YELLOW.png"));
	 sprite_knight_YELLOW.setOrigin(new Vector2f(16f,16f));
	 sprite_knight_BLUE = new Sprite(TexturesManager.GetTextureByName("ANIM_KNIGHT_BLUE.png"));
	 sprite_knight_BLUE.setOrigin(new Vector2f(16f,16f));
	 
	 
	 
	}

	@Override
	public void update(Time deltaTime) 
	{
		// appel au callback update
	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(RenderTarget render, RenderStates state) 
	{
		// affichage des unity
	
			
		// on appel le BACK draw
		this.CallBackDrawableBACK(render, state);
		
		// -------------------------------------------------------------------------------------------------------------------------
		//
		//
		// l'algorithem recherchant dans le quadtree de Box2d, les unités présente à l'écran
		//
		//
		// -------------------------------------------------------------------------------------------------------------------------
		
		// appel à l'abre pour l'affichage quadtree
		AABB aabb = new AABB(); // création du AABB
		FloatRect fc = CameraManager.getCameraBounds(); // on récupère le floatrect de la caméra
		// on spécifie les bordures des recheches
		aabb.lowerBound.set(new Vec2(fc.left / PhysicWorldManager.getRatioPixelMeter(), fc.top / PhysicWorldManager.getRatioPixelMeter()));
		aabb.upperBound.set(new Vec2((fc.left + fc.width) / PhysicWorldManager.getRatioPixelMeter(), (fc.top + fc.height) / PhysicWorldManager.getRatioPixelMeter()));
		// on remet à zero la liste des recheches
		treeSearch.clear();
		// on recherche les unités à afficher
		PhysicWorldManager.getWorld().queryAABB(treeSearch, aabb);
		
		// -----------------------------------------------------------------------------------------------------------------------
		//
		//
		// on affiche les unités
		// ici l'algorithme déterminant les unités enemy qui sont affichés en fonction d'une distance par rapport à une unité player
		// cette distance est établie pour les test à 10 mètre
		// 
		//
		// -----------------------------------------------------------------------------------------------------------------------
		
		Iterator<UnityBaseController> i = treeSearch.getIterator();
		while(i.hasNext())
		{
			UnityBaseController u = i.next();
			if(u.getModel().isPlayer())  // Si l'unité est un player, on affiche
				u.getView().draw(render, state);  
			else
			{
				// pour chaque non player on vérifie la distance avec les autres
				Collection<UnityBaseController>  tempCollection = EntityManager.getVectorUnity().values();
							// on récupère un intérateur
				Iterator<UnityBaseController> other = tempCollection.iterator();
				
				while(other.hasNext())
				{
					UnityBaseController o = other.next();
					if(u.getModel().getPosition().sub(o.getModel().getPosition()).length() < 40f) //  si la distance entre l'enemy et un player est inférieur à 10, on affiche
					{
						u.getView().draw(render, state);
						break; // on break;
					}
				}
			}
		}
	
	
		
		// on appel le middle
		this.CallBackDrawableMIDDLE(render, state);
		
		// on appel le front
		this.CallBackDrawableFRONT(render, state);	
		
		// on supprimer le list remove
		this.listCallBackRemove.clear();
	
		
		
		
	}
	
	public Vector2f getCoordText(int ind)
	{
		switch(ind)
		{
		case 1 : return new Vector2f(0,0);
		
		case 2 : return new Vector2f(32,0);
		
		case 3 : return new Vector2f(32,32);
		
		case 4 : return new Vector2f(0,32);
		
		default: return new Vector2f(0,0);
		}
	}
	
	// attachement
	public static void AddDrawable(Drawable d,LAYERS layer )
	{
		switch(layer)
		{
		case BACK: listCallBackDrawableBACK.add(d);break;
			
		case MIDDLE: listCallBackDrawableMIDDLE.add(d);break;
		
		case FRONT: listCallBackDrawableFRONT.add(d);break;
			
		}
		
		
	}
	
	public static void RemoveDrawable(Drawable d)
	{
		listCallBackRemove.add(d);
	}
	

	
	private void CallBackDrawableBACK(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableBACK)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableBACK.removeAll(listCallBackRemove);
		
	}
	
	private void CallBackDrawableMIDDLE(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableMIDDLE)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableMIDDLE.removeAll(listCallBackRemove);
		
		
	}
	private void CallBackDrawableFRONT(RenderTarget render, RenderStates states)
	{
	
		for(Drawable d : listCallBackDrawableFRONT)
			d.draw(render, states);
		
		// suppresion des élements dans la liste remove
		listCallBackDrawableFRONT.removeAll(listCallBackRemove);

	}

	
}

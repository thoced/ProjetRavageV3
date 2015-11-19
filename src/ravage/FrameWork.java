package ravage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.newdawn.slick.geom.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.navmesh.*;

import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import UI.PanelInfoBuild;
import UI.PanelInfoGold;
import UI.PanelInfoUnite;
import CoreTexturesManager.TexturesManager;
import coreAI.AstarManager;
import coreAI.Node;
import coreCamera.CameraManager;
import coreDrawable.DrawableUnityManager;
import coreDrawable.FogManager;
import coreDrawable.ForegroundEffectManager;
import coreEntity.Unity;
import coreEntity.UnityBaseModel;
import coreEntityManager.BloodManager;
import coreEntityManager.EntityManager;
import coreEntityManager.ReservationManager;
import coreEvent.EventManager;
import coreGUI.RectSelected;
import coreGUISwing.menuDialogRavage;
import coreGuiRavage.*;
import coreLevel.Level01;
import coreLevel.LevelArena01;
import coreLevel.LevelController;
import coreLevel.LevelManager;
import coreMessageManager.MessageManager;
import coreMessageManager.RegistrationObject;
import coreNet.NetManager;
import corePhysic.PhysicWorldManager;
import coreProjectil.ProjectilManager;
import coreSounds.SoundsManager;


public class FrameWork
{
	// max fps
	private final int MAX_FRAME = 60;
	// Managers
	private PhysicWorldManager physicWorld;
	//private LevelManager levelManager;
	private TexturesManager texturesManager;
	private CameraManager cameraManager;
	private EntityManager entityManager;
	private DrawableUnityManager drawaUnityManager;
	private AstarManager astarManager;
	private RectSelected rect;
	private NetManager netManager;
	private EventManager eventManager;
	private BloodManager bloodManager;
	private GuiRavageManager guiManager;
	private MessageManager messageManager;
	private FogManager fogManager;
	private ForegroundEffectManager foregroundEffectManager;
	private SoundsManager soundsManager;
	private ProjectilManager projectilManager;
	// Clocks
	private Clock frameClock;
	// fps
	private Time fpsTime;
	private int fps;
	// Level
	//private LevelController currentLevel;
	private  LevelController currentLevel;
	// RenderWindown
	private static RenderWindow window;
	// RenderTarget
	private RenderTexture renderTexture;
	private Sprite	spriteBackground;
	// RenderForeground
	private RenderTexture renderForeground;
	private Sprite spriteForeground;
	
	// Render pour le Gui
	private RenderTexture renderGui;
	private Sprite spriteGui;
	
	private menuDialogRavage menu;
			
	public void init() throws TextureCreationException, InterruptedException, IOException 
	{
		
		
		
		// Instance du réseau
		netManager = new NetManager();
		netManager.init();
		
		// Lancement du menu
		
		window = new RenderWindow(new VideoMode(1024,768),"ProjetRavage",RenderWindow.DEFAULT);
		// Instance des variables
		frameClock = new Clock();
		fpsTime = Time.ZERO;
		
		menu = new menuDialogRavage(null, "Projet Ravage Menu", true,netManager);
		menu.setVisible(true);
		menu.dispose();
		
		// creation de l'environnemnet graphique jsfml
		window.close();
		if(menu.isFullScreen())
			window = new RenderWindow(new VideoMode(menu.getResolutionScreenXY()[0],menu.getResolutionScreenXY()[1]),"ProjetRavage",RenderWindow.FULLSCREEN);
		else
			window = new RenderWindow(new VideoMode(menu.getResolutionScreenXY()[0],menu.getResolutionScreenXY()[1]),"ProjetRavage",RenderWindow.DEFAULT);
		
		window.setFramerateLimit(MAX_FRAME);
				
		
		messageManager = new MessageManager();
		messageManager.init();
		// Instance des managers
		physicWorld = new PhysicWorldManager();
		physicWorld.init();
		texturesManager = new TexturesManager();
		texturesManager.init();
		//levelManager = new LevelManager();
		//levelManager.init();
		currentLevel = new LevelArena01();
		currentLevel.init();
		
		cameraManager = new CameraManager(window.getView());
		cameraManager.init();
		entityManager = new EntityManager();
		entityManager.init();
		drawaUnityManager = new DrawableUnityManager();
		drawaUnityManager.init();
		rect = new RectSelected();
		rect.init();
		eventManager = new EventManager();
		eventManager.init();
		bloodManager = new BloodManager();
		bloodManager.init();
		guiManager = new GuiRavageManager();
		guiManager.init();
		fogManager = new FogManager(new Vector2i(currentLevel.getModel().getM_sizeX(),currentLevel.getModel().getM_sizeY()),entityManager);
		fogManager.init();
		foregroundEffectManager = new ForegroundEffectManager(new Vector2i(currentLevel.getModel().getM_sizeX(),currentLevel.getModel().getM_sizeY()),entityManager);
		foregroundEffectManager.init();
		soundsManager = new SoundsManager();
		soundsManager.init();
		projectilManager = new ProjectilManager();
		projectilManager.init();
	
		// création des guis tests
		PanelInfoGold infoGold = new PanelInfoGold(0.5f,(1f / window.getSize().y) * 24f,new Vector2f(256f,48f));
		guiManager.addPanel(infoGold);
		PanelInfoBuild infoBuild = new PanelInfoBuild(1f - (1f / window.getSize().x) * 48f,0.5f,new Vector2f(96f,512f));
		guiManager.addPanel(infoBuild);
		
		
		// attachement au call back
		RectSelected.attachCallBack(entityManager);
		eventManager.addCallBack(cameraManager);
		eventManager.addCallBack(entityManager);
		eventManager.addCallBack(rect);
		eventManager.addInterfaceCallBack(guiManager);
		
		// Chargement du niveau
		//currentLevel  = levelManager.loadLevel("testlevel01.json");
		// creatin du astarmanager
		astarManager = new AstarManager();
		astarManager.init();
		
		// crÃ©ation d'une premiÃ¨re render texture
		renderTexture = new RenderTexture();
		renderTexture.create(window.getSize().x, window.getSize().y);
		spriteBackground = new Sprite(renderTexture.getTexture());
		// création du renderforeground
		renderForeground = new RenderTexture();
		renderForeground.create(window.getSize().x, window.getSize().y);
		spriteForeground = new Sprite(renderForeground.getTexture());
		
		// création de la texture pour le render gui
		renderGui = new RenderTexture();
		renderGui.create(window.getSize().x, window.getSize().y);
		spriteGui = new Sprite(renderGui.getTexture());
	
		
		
	}

	public void run()
	{
		while(window.isOpen())
		{
			
			// Pool des evenements
			for(Event event : window.pollEvents())
			{
				// catch des events
			//	if(guiManager.catchEvent(event) != true)  // si un evenement est attrapé par le guiManager, on ne passe pas l'evenement au Manager d'eventment du jeu
					eventManager.catchEvent(event);

				if(event.type == Event.Type.CLOSED) 
				{
					this.destroyFrameWork();
				}
				
				if(event.type == Event.Type.KEY_PRESSED)
				{
					if(event.asKeyEvent().key == Keyboard.Key.ESCAPE)
					{
						this.destroyFrameWork();
					}
				
					
				}

			}
			
			// CrÃ©atin du deltaTime
			Time deltaTime = frameClock.restart();
			fpsTime = Time.add(fpsTime, deltaTime);
			
			/*if(fpsTime.asSeconds() > 1.0f)
			{
				System.out.println("fps : " + String.valueOf(fps));
				fps=0;
				fpsTime = Time.ZERO;
			}
			
			fps++;*/
			
			// Updates de composants
			physicWorld.update(deltaTime);
			currentLevel.update(deltaTime);
			cameraManager.update(deltaTime);
			entityManager.update(deltaTime);
			astarManager.update(deltaTime);
			rect.update(deltaTime);
			eventManager.update(deltaTime);
			netManager.update(deltaTime);
			bloodManager.update(deltaTime);
			guiManager.update(deltaTime);
			messageManager.update(deltaTime);
			projectilManager.update(deltaTime);
			
			
			// Draw des composants
			renderTexture.clear();
			renderTexture.setView(cameraManager.getView());
			// Draw du level
			currentLevel.getView().drawBackground(renderTexture, null); // affichage du background du level
			// Draw des unity
			drawaUnityManager.draw(renderTexture, null);
			// Draw des projectils
			projectilManager.draw(renderTexture, null);
			// Draw du shadow 
			currentLevel.getView().drawShadowground(renderTexture, null);
			
			
			// draw du level foregrounds
			renderForeground.clear(Color.TRANSPARENT);
			renderForeground.setView(cameraManager.getView());
			currentLevel.getView().drawForeground(renderForeground, null); // affichage du foreground du level (arbre et toi
			
			// draw du lightmap
			
			
			// application de l'effect foreground
			foregroundEffectManager.draw(renderForeground, null);
			renderForeground.display();
			
			currentLevel.getView().drawLightground(renderForeground, null);
			
	
			
			// draw du fog
			fogManager.draw(renderTexture, null);
			renderTexture.display();
			
			// draw du guiManager
			renderGui.clear(Color.TRANSPARENT);
			guiManager.draw(renderGui, null);
			renderGui.display();
			
			// draw du rect
			rect.draw(renderTexture, null);
			renderTexture.display();
			// draw final
			window.clear();
			window.draw(spriteBackground);
			window.draw(spriteForeground);
			window.draw(spriteGui);
			window.display();
			
			
		}
		
	
	}

	public void destroyFrameWork()
	{
		// destruction du thread
		if(astarManager != null)
			astarManager.interrupt();
		// destruction du thread fogmanager
		if(fogManager != null)
			fogManager.interrupt();
		
		// destruction du thread foregroundEffectManager
		if(foregroundEffectManager != null)
			foregroundEffectManager.interrupt();
		// fermeture de la connection UDP et du theadd
		if(netManager != null)
			netManager.close();
		
		if(soundsManager != null)
			soundsManager.closeThread();
		// fermeture de la fenetre
		window.close();
	}

	/**
	 * @return the window
	 */
	public static RenderWindow getWindow() {
		return window;
	}

	/**
	 * @param window the window to set
	 */
	public static void setWindow(RenderWindow window) {
		FrameWork.window = window;
	}
	
	
}

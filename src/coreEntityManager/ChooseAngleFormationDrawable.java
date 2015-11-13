package coreEntityManager;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import CoreTexturesManager.TexturesManager;
import coreDrawable.DrawableUnityManager;
import coreDrawable.DrawableUnityManager.LAYERS;
import coreEvent.EventManager;
import coreEvent.IEventCallBack;
import corePhysic.PhysicWorldManager;
import ravage.FrameWork;
import ravage.IBaseRavage;

public class ChooseAngleFormationDrawable implements IBaseRavage, Drawable, IEventCallBack 
{

	// variable buffer
	private VertexArray buffer;
	// variable sprite pointe de fleche
	private Sprite arrowPoint;
	
	// variable de début et fin de ligne
	private Vector2f posStart;
	private Vector2f posEnd;
	
	
	// Button pressed
	private Mouse.Button buttonPressed;
	
	public ChooseAngleFormationDrawable(Vector2f start,Vector2f end)
	{
		// instance du buffer
		buffer = new VertexArray(PrimitiveType.LINE_STRIP);
		
		// initialisation
		posStart=start;
		posEnd = end;
		
		// ajout dans le drawablecallback
		DrawableUnityManager.AddDrawable(this,LAYERS.FRONT);
		// ajout dans le callback EventManager
		EventManager.addCallBack(this);
		
		// chargement du sprite
		arrowPoint = new Sprite(TexturesManager.GetTextureByName("arrowPoint.png"));
		arrowPoint.setOrigin(new Vector2f(8,8));
		
	
	}
	
	
	
	public Vector2f getPosStart() {
		return posStart;
	}



	public void setPosStart(Vector2f posStart) {
		this.posStart = posStart;
	}



	public Vector2f getPosEnd() {
		return posEnd;
	}



	public void setPosEnd(Vector2f posEnd) {
		this.posEnd = posEnd;
	}



	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// TODO Auto-generated method stub
		buffer.clear();
		// création de la ligne
		Vertex v1 = new Vertex(posStart);
		Vertex v2 = new Vertex(posEnd);
		// création du vecteur de direction
		Vec2 a = new Vec2(posEnd.x / PhysicWorldManager.getRatioPixelMeter(),posEnd.y/PhysicWorldManager.getRatioPixelMeter());
		Vec2 dir = a.sub(new Vec2(posStart.x/PhysicWorldManager.getRatioPixelMeter(),posStart.y/PhysicWorldManager.getRatioPixelMeter()));
		dir.normalize();
		// coordonnée du sprite arrowPoint
		arrowPoint.setPosition(posEnd);
		// on détermine l'angle du sprite
		Rot r = new Rot();
		r.s = dir.y;
		r.c = dir.x;
		float angle = r.getAngle(); 
		// on spécifie la roation
		arrowPoint.setRotation((float)((angle * 180f) / Math.PI) % 360f);
		// ajout dans le buffer
		buffer.add(v1);
		buffer.add(v2);

		// affichage
		arg0.draw(buffer);
		arg0.draw(arrowPoint);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Time deltaTime)
	{
	
		// on défini la position de fin
		this.posEnd = FrameWork.getWindow().mapPixelToCoords(Mouse.getPosition(FrameWork.getWindow()));
		
		
	}

	@Override
	public void destroy() 
	{
		// suppression de l'objet du drawableunitymanager
		DrawableUnityManager.RemoveDrawable(this);
		EventManager.removeCallBack(this);
		
		
	}




	@Override
	public boolean onMousePressed(MouseButtonEvent event) 
	{
		// TODO Auto-generated method stub
		buttonPressed = event.button;
		return true;
		
	}





	public Vec2 getVectorDirectionFormation()
	{
		// retourne le vecteur de direction
		Vec2 ps = new Vec2(this.posStart.x / PhysicWorldManager.getRatioPixelMeter(),this.posStart.y / PhysicWorldManager.getRatioPixelMeter());
		Vec2 pe = new Vec2(this.posEnd.x / PhysicWorldManager.getRatioPixelMeter(),this.posEnd.y / PhysicWorldManager.getRatioPixelMeter());
		
		Vec2 angle = pe.sub(ps);
		angle.normalize();
		
		return angle;
	}



	@Override
	public boolean onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onMouseMove(MouseEvent event) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onMouseReleased(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		return false;
	}



}

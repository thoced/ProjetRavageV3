package coreEntity;

import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import corePhysic.PhysicWorldManager;

public  class UnityBaseView implements Drawable
{
	protected Sprite sprite;
	
	protected UnityBaseController controller;
	
	protected UnityBaseModel model;
	
	protected  float elapsedAnimationTime; 				// temps écoulé pour les animations
	
	public enum TYPE_ANIMATION  {NON,WALK,STRIKE};	// type d'animatino possible
	
	protected TYPE_ANIMATION currentTypeAnimation;				// animation en cours

		
	
	
	public UnityBaseView(UnityBaseModel model, UnityBaseController controller) {
		super();
		this.controller = controller;
		this.model = model;
		
		// appel à la méthode prépare animation pour instancier le rectAnimations
		//this.getModel().setAnimations(new Animations());
		this.prepareAnimations(this.getController().getModel().getAnimations());
	}
	
	public  void prepareAnimations(Animations animations)
	{
		
	}
	
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		// on modifie le sprite grace au model
		Vec2 pos = this.getController().getModel().getPosition();
		this.sprite.setPosition(this.toPixelVector2f(pos));
		this.sprite.setRotation((float)((this.getController().getModel().getBody().getAngle() * 180f) / Math.PI) % 360f);
		
		// animation
		switch(this.currentTypeAnimation)
		{
			case NON: this.noneAnimation();break;
		
			case WALK: this.walkAnimation();break;
			
			case STRIKE : this.strikeAnimation();break;
		}
		
		// affichage
		arg0.draw(sprite);
	}
	
	private void walkAnimation()
	{
		// on calcul l'animation
		int ind = (int) (model.NB_FRAME_BY_SECOND * this.elapsedAnimationTime);
		if(ind > model.MAX_IND_FOR_WALK)
		{
			ind = model.MIN_IND_FOR_WALK;
			this.elapsedAnimationTime = 0f;
		}
		
		System.out.println("ind : " + ind);
		this.sprite.setTextureRect(this.getController().getModel().getAnimations().getInd(ind));
	}
	
	private void strikeAnimation()
	{
		// on calcul l'animation
		int ind = (int) (model.NB_FRAME_BY_SECOND * this.elapsedAnimationTime) + model.MIN_IND_FOR_STRIKE; // 5 étant l'offset
		if(ind > model.MAX_IND_FOR_STRIKE)
		{
			ind = model.MIN_IND_FOR_STRIKE;
			this.playAnimation(TYPE_ANIMATION.NON);
			
		}
		System.out.println("indice: "+ ind);
		
		this.sprite.setTextureRect(this.getController().getModel().getAnimations().getInd(ind));
		
	}
	
	private void noneAnimation()
	{
		this.sprite.setTextureRect(this.getController().getModel().getAnimations().getInd(0));
	}

	
	public UnityBaseController getController() {
		return controller;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	

	public TYPE_ANIMATION getCurrentTypeAnimation() {
		return currentTypeAnimation;
	}

	public void playAnimation(TYPE_ANIMATION currentTypeAnimation) 
	{
		this.currentTypeAnimation = currentTypeAnimation; // spécifie le type d'animation.
						
		if(this.currentTypeAnimation != TYPE_ANIMATION.WALK) // si l'animation est de marcher, on ne place pas le temps à 0 car l'animation se joue en boucle
			this.elapsedAnimationTime = 0f;
	}

	protected Vec2 toMeterVec2(Vector2f v)
	{
		return new Vec2(v.x / PhysicWorldManager.getRatioPixelMeter(),v.y / PhysicWorldManager.getRatioPixelMeter());
	}
	
	protected Vector2f toPixelVector2f(Vec2 v)
	{
		return new Vector2f(v.x * PhysicWorldManager.getRatioPixelMeter(), v.y * PhysicWorldManager.getRatioPixelMeter());
	}

	
	
	
	
}

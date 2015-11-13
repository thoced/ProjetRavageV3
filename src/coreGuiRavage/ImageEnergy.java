package coreGuiRavage;

import org.jbox2d.common.Vec3;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3i;

import coreEntity.UnityBaseController;

public class ImageEnergy extends Image 
{

	// parent unity
	private UnityBaseController m_unity;
	
	// Panel container
	private Panel m_container;
	
	public ImageEnergy(UnityBaseController unity,Vector2f position,Texture texture,Panel container) 
	{
		super((Texture)unity.getView().getSprite().getTexture(),unity.getModel().getAnimations().getInd(0), position);
		// parent container
		m_container = container;
		// TODO Auto-generated constructor stub
		// création du intrect en fonction de l'unité à afficher
		int indIntRect = unity.getModel().getIdType().ordinal();
		IntRect intRect = new IntRect(indIntRect * 32,0,32,32);
		this.m_view = new ImageEnergyView((Texture)texture,intRect,position);
		m_unity = unity;
	
		
	}
	
	

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		super.update(deltaTime);
		// mise à jour de l'énergie
		// modification des couleurs de l'energie du vert au rouge
		int g = (255 / m_unity.getModel().getEnergyMax()) *  m_unity.getModel().getEnergy();
		int r = 255 - g;
		// placementd de la couleur
		((ImageEnergyView)this.m_view).m_shape.setFillColor(new Color(r,g,0));
		// positionnement de la grandeur de l'energie
		((ImageEnergyView)this.m_view).m_shape.setSize(new Vector2f((24f / m_unity.getModel().getEnergyMax()) * m_unity.getModel().getEnergy(),2f));	
		
		if(m_unity.getModel().getEnergy() <= 0)
			m_container.removeWidget(this);
		
	}




	class ImageEnergyView extends ImageView
	{
		// shape de l'energy
		public RectangleShape m_shape;
		
				
		public ImageEnergyView(Texture texture,IntRect textureRect,Vector2f position) 
		{
			super(texture, textureRect, position);
			// instance du shape
			m_shape = new RectangleShape();
			m_shape.setPosition(Vector2f.sub(ImageEnergy.this.m_model.m_position,new Vector2f(16f,16f)));
			m_shape.setSize(new Vector2f(24f,2f));
			m_shape.setOrigin(0f,0f);

		}

		public void setSize(Vector2f size)
		{
			m_shape.setSize(size);
		}


		@Override
		public void draw(RenderTarget render, RenderStates state) 
		{
			super.draw(render, state);
			// affichage
			render.draw(m_shape);
			
		
			
		}
		
	}
	
	

}

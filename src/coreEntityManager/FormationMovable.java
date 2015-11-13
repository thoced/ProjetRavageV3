package coreEntityManager;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Rot;
import org.jbox2d.common.Vec2;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;

import coreEntity.UnityBaseController;
import coreLevel.LevelManager;
import ravage.IBaseRavage;

// -------------------------------------
//
// Class FormationMovable (Controller)
//
// -------------------------------------

public class FormationMovable implements IBaseRavage
{
	// Model 
	private FormationMovableModel m_model;
	// Vue
	private FormationMovableView m_view;
	// valeur de PI / 180;
	private static final float PI_DIV_180 = 0.0174533f;  // PI / 180
		
	
	
	public FormationMovable()
	{
		super();
		
		// instance du model
		m_model = new FormationMovableModel();
		// instance de la vue
		m_view = new FormationMovableView();
		// création du point d'ancrage
		
	}
	
	public static float degreeToRadian(float degree)
	{
		// retourn la conversation vers des radians
		return PI_DIV_180 * degree;
	}
	
	public void moveFormation(List<UnityBaseController> unityMovables, Vec2 positionDestination,Vec2 dirFormation)
	{
		
		
		// on regarde si l'emplacement de destination n'est pas sur un node noire
		if(!LevelManager.getLevel().getModel().isNodeObstacle((int)positionDestination.x,(int) positionDestination.y))
		{
			// on positionne l'ancre
			m_model.getAnchor().m_position = positionDestination.add(new Vec2(0.5f,0.5f)); // ajout des 0.5 pour arriver au centre du node
			
			// ce n'est pas un obstacle, on détermine la forme (pattern) de la formation en fonction du nombre d'unité et de la spécificité du terrain
			int nbUnity = unityMovables.size();
			//int nbCouche = (nbUnity - 1) / 8; 
			// utilisation d'un cercle concentrique , le 1er cercle fait 45 ° de pas
			float pasQuartier = 45f;
			// création du Rot avec valeur du vecteur de départ à x = 1, y = 0
			Rot rot = new Rot();
			rot.s = 1f; // x
			rot.c = 0f; // y
			
			
			// compteur unité
			int cptUnity = 0;
			// rayon
			float rayon = 1.1f;
			// angleQuartier
			float angleQuartier = 0f;
			
			// tant que le compteur d'unité est plus petit que le nombre total d'unité à ajouter
			
			do
			{
			
				while((angleQuartier < 360) && cptUnity < nbUnity)
				{
					// on spécifie l'angle en radian
					rot.set(degreeToRadian(angleQuartier));
					// on récupère un nouveau vec2
					Vec2 v = new Vec2(rot.s,rot.c);
					// on normalize
					v.normalize();
					// on multiplie le v par le rayon
					Vec2 pRel = v.mul(rayon);
					// créationd de la position relative à une position absolue dans la map
					Vec2 pAbs = m_model.getAnchor().m_position.add(pRel);
					// on regarde si la position trouvé est libre
					if(!LevelManager.getLevel().getModel().isNodeObstacle((int)pAbs.x,(int)pAbs.y))
					{
						// il n'y a pas d'obstacle alors on va créer un slotFormation
						SlotFormation slot = new SlotFormation(m_model.m_anchor,pRel);
						slot.attributeUnity(unityMovables.get(cptUnity));
						// ajout du slot dans le vecteur de slot si la position du slot n'existe déja pas
						if(!m_model.geSlots().contains(slot))
						{
							m_model.geSlots().add(slot);
							cptUnity++;
						}
					}
					
					// ajout du pas quartier
					angleQuartier += pasQuartier;
					
				}
				
				// adition du rayon
				rayon += 1.1f;
				// on replace la pas à 0
				angleQuartier = 0f;
				pasQuartier = pasQuartier / 2f;
				
				
				
			}while(cptUnity < nbUnity);
			
			
			
			
		}
		
		// on compute la destination
		for(SlotFormation slot : m_model.m_slots)
		{
			Vec2 posNode = new Vec2((int)slot.getPositionAbsolue().x,(int)slot.getPositionAbsolue().y);
			EntityManager.computeDestination(slot.getUnityMovable(), posNode, posNode, dirFormation);
		}
	}
	
	

	public FormationMovableModel getMmodel() {
		return m_model;
	}

	public FormationMovableView getView() {
		return m_view;
	}

	public void setModel(FormationMovableModel m_model) {
		this.m_model = m_model;
	}

	public void setView(FormationMovableView m_view) {
		this.m_view = m_view;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	// -------------------------------------
	//
	// Class Model
	//
	// -------------------------------------
	
	public class FormationMovableModel
	{
		// ancre
		private Anchor m_anchor;
		// List des Slots
		private List<SlotFormation> m_slots;
		
		public FormationMovableModel()
		{
			// instance de la liste des slots
			m_slots = new ArrayList<SlotFormation>();
			// instance de l'ancre
			m_anchor = new Anchor();
		}

		public Anchor getAnchor() {
			return m_anchor;
		}

		public List<SlotFormation> geSlots() {
			return m_slots;
		}
		
		
		
		
	}
	
	
	
	// -------------------------------------
	//
	// Class View
	//
	// -------------------------------------
	
	public class FormationMovableView implements Drawable
	{

		@Override
		public void draw(RenderTarget render, RenderStates state) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	// -------------------------------------
	//
	// Class SlotFormation
	//
	// -------------------------------------
	
	public class SlotFormation
	{
		// position relative à l'ancre
		private Vec2 m_positionRelative; 
		
		// position absolue dans l'espace (map)
		private Vec2 m_positionAbsolue;
		
		// parent ancre
		private Anchor m_parentAnchor;
		
		// unité attribuée
		private UnityBaseController m_unityMovable;
		
		public SlotFormation(Anchor parentAnchor,Vec2 positionRelative)
		{
			// parent Anchor
			m_parentAnchor = parentAnchor;
			// position relative à l'ancre
			m_positionRelative = positionRelative;
			// calcul de la position absolue
			m_positionAbsolue = m_positionRelative.add(m_parentAnchor.m_position);
		}
		
		public void attributeUnity(UnityBaseController unityMovable)
		{
			m_unityMovable = unityMovable;
		}
		
		

		public UnityBaseController getUnityMovable() {
			return m_unityMovable;
		}

		public Vec2 getPositionRelative() {
			return m_positionRelative;
		}

		public Vec2 getPositionAbsolue() {
			return m_positionAbsolue;
		}

		public boolean equals(SlotFormation obj) 
		{
			if(obj.getPositionRelative().equals(this.getPositionRelative()))
				return true;
			else
				return false;
		}
		
		
		
	}
	
		
	// -------------------------------------
	//
	// Class Anchor
	//
	// -------------------------------------
	
	public class Anchor
	{
		// position de l'ancre
		public Vec2 m_position;
	}
	
	

}

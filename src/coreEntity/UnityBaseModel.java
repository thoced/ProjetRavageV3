package coreEntity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.newdawn.slick.util.pathfinding.Path;

import coreEntity.UnityBaseController.TYPEUNITY;
import coreEntityManager.EntityManager;
import coreEntityManager.NodeReserved;
import coreEntityManager.EntityManager.CAMP;
import corePhysic.PhysicWorldManager;

public class UnityBaseModel implements Externalizable
{
	protected transient UnityBaseController controller;
	// appartenance à un camp
	protected EntityManager.CAMP myCamp = EntityManager.CAMP.YELLOW;
	
	protected boolean isPlayer;  // est-on le player ?
	//protected type d'unité
	protected TYPEUNITY idType;
	
	protected Vec2 position ; // en mètre - est utilisé car pas de sérialisation de l'objet Body
	
	protected Vec2 positionNode ; // position en node;
	
	protected Vec2 positionlFinal ; // position final de l'unité
	
	protected Vec2 positionNodeFinal ; // coordonnée de position node
	
	protected float rotation;
	
	protected float speed; 			// vitesse;
	
	protected boolean isSelected;   // est-on sélectionné
	
	protected Vec2 dirFormation ; // direction de formation que doit prendre l'unité
	
	protected int id;			  // id de l'unité
	
	//protected transient UnityNetController enemy;
	
	protected int idEnemy = -1;	 // ide de l'enemy
	
	protected boolean isKnocking = false; // variable passé à true quand l'unité frappe.
	
	protected int streightStrike;
	
	protected transient  Body body; // le body n'est pas sérializable -> transient
	
	protected transient  Object lock;
	
	protected Path paths = null;
	protected  int  indicePaths = 0;

	//est on mort ?
	protected boolean isKilled = false;
	
	
	// STAT//////////////////////
	// energie
	protected int energy;
	// max energie 
	protected int energyMax;
	// bouclier
	protected int armor;
	// pouvoir de penetration
	protected int powerPenetration;
	// agilité
	protected int agility;
	// dextérité
	protected int dexterity;
	// l'unité frappe t il avec un coup critique ?
	protected boolean isCriticalStrike = false;
	// fréquence de frappe
	protected float frequencyStrike = 1f;
	
	
	/////////////////////////////////
	// information concernant l'origine du sprite
	protected Animations animations;
	// information sur l'origne du sprite
	protected Vector2f origineSprite;
	
	protected NodeReserved nodeReserved = null; // node réservé par une unité alliée
	
	// test
	protected boolean isOneContact = false;
	// move to enemy
	protected boolean isMoveToEnemy = false;
	
	// information sur les frames
	protected  int NB_FRAME_BY_SECOND;
	protected  int NB_TOTAL_FRAME ;
	protected  int MIN_IND_FOR_WALK;
	protected  int MAX_IND_FOR_WALK ;  // 4
	protected  int MIN_IND_FOR_STRIKE ;
	protected  int MAX_IND_FOR_STRIKE;
	
	protected int WIDTH_FRAME ;
	protected int HEIGHT_FRAME ;
	
	
	
	public UnityBaseModel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public UnityBaseModel(UnityBaseController controller) {
		super();
		
		this.controller = controller;
		lock = new Object();
		animations = new Animations(); // instance de l'objet animations
		
	}
	
	
	
	
	
	@Override
	public UnityBaseModel clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		UnityBaseModel clone = new UnityBaseModel(this.controller);
		clone.setMyCamp(this.getMyCamp());
		clone.setPlayer(this.isPlayer());
		clone.setIdType(this.getIdType());
		clone.setId(this.getId());
		clone.setPosition(this.getPosition());
		clone.setPositionNode(this.getPositionNode());
		clone.setPositionlFinal(this.getPositionlFinal());
		clone.setRotation(this.getRotation());
		clone.setSpeed(this.getSpeed());
		clone.setSelected(this.isSelected());
		clone.setDirFormation(this.getDirFormation());
		clone.setIdEnemy(this.getIdEnemy());
		clone.setKnocking(this.isKnocking());
		clone.setPaths(this.getPaths());
		clone.setIndicePaths(this.getIndicePaths());
		clone.setKilled(this.isKilled());
		clone.setAnimations(this.getAnimations());
		clone.setOrigineSprite(this.getOrigineSprite());
		clone.setEnergy(this.getEnergy());
		clone.setEnergyMax(this.getEnergyMax());
		clone.setArmor(this.getArmor());
		clone.setPowerPenetration(this.getPowerPenetration());
		clone.setStreightStrike(this.getStreightStrike());
		clone.setDexterity(this.getDexterity());
		clone.setAgility(this.getAgility());
		clone.setCriticalStrike(this.isCriticalStrike());
		clone.setFrequencyStrike(this.getFrequencyStrike());
		clone.setOneContact(this.isOneContact());
		clone.setMoveToEnemy(this.isMoveToEnemy());
		clone.setNB_FRAME_BY_SECOND(this.NB_FRAME_BY_SECOND);
		clone.setWIDTH_FRAME(this.WIDTH_FRAME);
		clone.setHEIGHT_FRAME(this.HEIGHT_FRAME);
		clone.setMIN_IND_FOR_WALK(this.MIN_IND_FOR_WALK);
		clone.setMAX_IND_FOR_WALK(this.MAX_IND_FOR_WALK);
		clone.setMIN_IND_FOR_STRIKE(this.MIN_IND_FOR_STRIKE);
		clone.setMAX_IND_FOR_STRIKE(this.MAX_IND_FOR_STRIKE);
		
		return clone;
	}





	public void initModel(UnityBaseController controller)
	{
		this.controller = controller;
		// création du body
				// initialisation du body
				BodyDef bdef = new BodyDef();
				bdef.active = true;
				bdef.bullet = false;
				bdef.type = BodyType.DYNAMIC;
				bdef.fixedRotation = false;
				bdef.userData = controller;
				bdef.linearDamping = 1.0f;
				// creation du body
				this.setBody(PhysicWorldManager.getWorld().createBody(bdef));	
				Shape shape = new CircleShape();
				shape.m_radius = 0.45f;
				FixtureDef fDef = new FixtureDef();
				fDef.shape = shape;
				fDef.density = 0f;
				fDef.friction = 0.0f;
				fDef.restitution = 0.0f;
			
				Fixture fix = this.getBody().createFixture(fDef);
				// creation du body
				this.body.setTransform(this.position, this.rotation);
				// création du lock
				lock = new Object();
				
			
				// on replace le path et l'indice du path à 0 pour éviter les téléportation réseau
				//this.setIndicePaths(0); // positionnement à 0 
				//this.setPosition(new Vec2(this.getPaths().getStep(0).getX(),this.getPaths().getStep(0).getY())); // positionnement de la position à 0
				
	}
	
	

	public float getFrequencyStrike() {
		return frequencyStrike;
	}


	public void setFrequencyStrike(float frequencyStrike) {
		this.frequencyStrike = frequencyStrike;
	}


	public boolean isCriticalStrike() {
		return isCriticalStrike;
	}


	public void setCriticalStrike(boolean isCriticalStrike) {
		this.isCriticalStrike = isCriticalStrike;
	}


	public int getAgility() {
		return agility;
	}


	public int getDexterity() {
		return dexterity;
	}


	public void setAgility(int agility) {
		this.agility = agility;
	}


	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}


	public int getArmor() {
		return armor;
	}


	public int getPowerPenetration() {
		return powerPenetration;
	}


	public void setArmor(int armor) {
		this.armor = armor;
	}


	public void setPowerPenetration(int powerPenetration) {
		this.powerPenetration = powerPenetration;
	}


	public int getNB_FRAME_BY_SECOND() {
		return NB_FRAME_BY_SECOND;
	}


	public int getNB_TOTAL_FRAME() {
		return NB_TOTAL_FRAME;
	}


	public int getMIN_IND_FOR_WALK() {
		return MIN_IND_FOR_WALK;
	}


	public int getMAX_IND_FOR_WALK() {
		return MAX_IND_FOR_WALK;
	}


	public int getMIN_IND_FOR_STRIKE() {
		return MIN_IND_FOR_STRIKE;
	}


	public int getMAX_IND_FOR_STRIKE() {
		return MAX_IND_FOR_STRIKE;
	}


	public int getWIDTH_FRAME() {
		return WIDTH_FRAME;
	}


	public int getHEIGHT_FRAME() {
		return HEIGHT_FRAME;
	}


	public void setNB_FRAME_BY_SECOND(int nB_FRAME_BY_SECOND) {
		NB_FRAME_BY_SECOND = nB_FRAME_BY_SECOND;
	}


	public void setNB_TOTAL_FRAME(int nB_TOTAL_FRAME) {
		NB_TOTAL_FRAME = nB_TOTAL_FRAME;
	}


	public void setMIN_IND_FOR_WALK(int mIN_IND_FOR_WALK) {
		MIN_IND_FOR_WALK = mIN_IND_FOR_WALK;
	}


	public void setMAX_IND_FOR_WALK(int mAX_IND_FOR_WALK) {
		MAX_IND_FOR_WALK = mAX_IND_FOR_WALK;
	}


	public void setMIN_IND_FOR_STRIKE(int mIN_IND_FOR_STRIKE) {
		MIN_IND_FOR_STRIKE = mIN_IND_FOR_STRIKE;
	}


	public void setMAX_IND_FOR_STRIKE(int mAX_IND_FOR_STRIKE) {
		MAX_IND_FOR_STRIKE = mAX_IND_FOR_STRIKE;
	}


	public void setWIDTH_FRAME(int wIDTH_FRAME) {
		WIDTH_FRAME = wIDTH_FRAME;
	}


	public void setHEIGHT_FRAME(int hEIGHT_FRAME) {
		HEIGHT_FRAME = hEIGHT_FRAME;
	}


	public boolean isMoveToEnemy() {
		return isMoveToEnemy;
	}


	public void setMoveToEnemy(boolean isMoveToEnemy) {
		this.isMoveToEnemy = isMoveToEnemy;
	}


	public boolean isOneContact() {
		return isOneContact;
	}


	public void setOneContact(boolean isOneContact) {
		this.isOneContact = isOneContact;
	}


	public int getEnergyMax() {
		return energyMax;
	}


	public void setEnergyMax(int energyMax) {
		this.energyMax = energyMax;
	}


	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return isPlayer;
	}


	/**
	 * @param isPlayer the isPlayer to set
	 */
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}


	public NodeReserved getNodeReserved() {
		return nodeReserved;
	}


	public void setNodeReserved(NodeReserved nodeReserved) {
		this.nodeReserved = nodeReserved;
	}


	public Vector2f getOrigineSprite() {
		return origineSprite;
	}

	public void setOrigineSprite(Vector2f origineSprite) {
		this.origineSprite = origineSprite;
	}

	public int getIdEnemy() {
		return idEnemy;
	}

	public Animations getAnimations() {
		return animations;
	}

	public void setIdEnemy(int idEnemy) {
		this.idEnemy = idEnemy;
	}

	public void setAnimations(Animations animations) {
		this.animations = animations;
	}

	public EntityManager.CAMP getMyCamp() {
		return myCamp;
	}

	public TYPEUNITY getIdType() {
		return idType;
	}
	
	

	public void setBody(Body body) {
		this.body = body;
	}

	public Body getBody() {
		return body;
	}

	

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Vec2 getPositionNodeFinal() {
		return positionNodeFinal;
	}

	

	public Vec2 getPositionlFinal() {
		return positionlFinal;
	}

	public void setPositionlFinal(Vec2 positionlFinal) {
		this.positionlFinal = positionlFinal;
	}

	public void setPositionNodeFinal(Vec2 positionNodeFinal) 
	{
		this.positionNodeFinal = positionNodeFinal;
		
		
	}
	
	public Vec2 getPositionNode() 
	{
		positionNode = this.getPosition().clone();
		positionNode.x = (int)positionNode.x;
		positionNode.y = (int)positionNode.y;
		return positionNode;
	}

	public void setPositionNode(Vec2 positionNode) {
		this.positionNode = positionNode;
	}

	public Vec2 getDirFormation() {
		return dirFormation;
	}

	public void setDirFormation(Vec2 dirFormation) {
		this.dirFormation = dirFormation;
	}

	public Vec2 getPosition() 
	{
			if(body!=null)
				this.position = body.getPosition();
		return this.position;
	}

	public void setPosition(Vec2 position) 
	{
		//this.position = position.add(new Vec2(0.5f,0.5f));
		this.position = position;
		if(body!=null)
			body.setTransform(this.position, rotation);
	}

	public float getRotation() 
	{
		if(body != null)
			rotation = body.getAngle();
		return rotation;
	}

	public int getId() {
		return id;
	}

	public boolean isKilled() {
		return isKilled;
	}

	public void setMyCamp(EntityManager.CAMP myCamp) {
		this.myCamp = myCamp;
	}

	public void setIdType(TYPEUNITY idType) {
		this.idType = idType;
	}


	public void setRotation(float rotation) 
	{
		this.rotation = rotation;
		if(body != null)
			body.setTransform(this.position, this.rotation);
		
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}

	/*public UnityNetController getEnemy() 
	{
		// recherche de l'enemy
		enemy = EntityManager.getVectorUnityNet().get(this.getIdEnemy());
		return enemy;
	}

	public void setEnemy(UnityNetController enemy) 
	{
		this.enemy = enemy;
		// récupération de l'id enemy
		this.setIdEnemy(this.enemy.getModel().getId());
	}*/

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	

	public int getStreightStrike() {
		return streightStrike;
	}


	public void setStreightStrike(int streightStrike) {
		this.streightStrike = streightStrike;
	}


	public boolean isKnocking() {
		return isKnocking;
	}

	public void setKnocking(boolean isKnocking) {
		this.isKnocking = isKnocking;
	}

	public Path getPaths() 
	{
		synchronized(lock)
		{
			return paths;
		}
		
	}

	public void setPaths(Path paths) 
	{
		synchronized(lock)
		{
			this.paths = paths;
			// on replace l'indice à 00
			this.setIndicePaths(0);
	
		}
	}

	public int getIndicePathsAndIncrement()
	{
		synchronized(lock)
		{
			return indicePaths++; // incrémente autamatiquement l'indice de chemin et commence par le 0
		}
	
	}
	
	public void DecrementIndice()
	{
		synchronized(lock)
		{
			if(indicePaths > 0)
			 indicePaths--; // incrémente autamatiquement l'indice de chemin et commence par le 0
		}
	}

	public void setIndicePaths(int indicePaths)
	{
		synchronized(lock)
		{
		this.indicePaths = indicePaths;
		}
	}

	public int getIndicePaths() 
	{	synchronized(lock)
		{
		return indicePaths;
		}
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id : " + this.getId() + " position : " + this.getPosition();
	}





	public int getEnergy() {
		return energy;
	}


	public void setEnergy(int energy) {
		this.energy = energy;
	}


	@Override
	public void readExternal(ObjectInput arg0) throws IOException,
			ClassNotFoundException 
	{
		// Camp 
		this.myCamp = CAMP.values()[arg0.readInt()];
		// Is Player ?
		this.isPlayer = arg0.readBoolean();
		// id type
		this.idType = TYPEUNITY.values()[arg0.readInt()];
		// Position
		this.position = new Vec2(arg0.readFloat(),arg0.readFloat());
		// Position Node
		this.positionNode = new Vec2(arg0.readFloat(),arg0.readFloat());
		// Position Final
		this.positionlFinal = new Vec2(arg0.readFloat(),arg0.readFloat());
		// Position Node Final
		this.positionNodeFinal = new Vec2(arg0.readFloat(),arg0.readFloat());
		// Rotaton
		this.rotation = arg0.readFloat();
		// Speed
		this.speed = arg0.readFloat();
		// Is Selected
		this.isSelected = arg0.readBoolean();
		// Dir Formation
		this.dirFormation  = new Vec2(arg0.readFloat(),arg0.readFloat());
		// id
		this.id = arg0.readInt();
		// Id Ennemy
		this.idEnemy = arg0.readInt();
		// Knocking
		this.isKnocking = arg0.readBoolean();
		// StreightStrike (force de frappe)
		this.streightStrike = arg0.readInt();
		
		// Paths
		int l = arg0.readInt();
		if(l > 0)
		{
			this.paths = new Path();
			for(int i=0;i<l;i++)
				this.paths.appendStep(arg0.readInt(), arg0.readInt());
		}
		else
			this.paths = null;
		// Indice path
		this.indicePaths = arg0.readInt();
		// Is Killed
		this.isKilled = arg0.readBoolean();
		// Energy
		this.energy = arg0.readInt();
		// Energy max
		this.energyMax = arg0.readInt();
		// armor
		this.armor = arg0.readInt();
		// power penetration
		this.powerPenetration = arg0.readInt();
		// dextirity
		this.dexterity = arg0.readInt();
		// agility
		this.agility = arg0.readInt();
		// coup critique ?
		this.isCriticalStrike = arg0.readBoolean();
		// frequence de frappe entre deux frappes
		this.frequencyStrike = arg0.readFloat();
		
		
		// animation
	//	this.animations = (Animations)arg0.readObject();
		
		int size = arg0.readInt();
		this.animations = new Animations();
		this.animations.makeAnimation(size); // création de l'animation
		
		for(int j=0;j<size;j++)
		{
			IntRect r = new IntRect(arg0.readInt(),arg0.readInt(),arg0.readInt(),arg0.readInt());
			this.animations.append(j, r);
		}
		
		// Position
		this.origineSprite = new Vector2f(arg0.readFloat(),arg0.readFloat());
		// one contact ?
		this.isOneContact = arg0.readBoolean();
		// mote to enemy ?
		this.isMoveToEnemy = arg0.readBoolean();
		
		// nombre de frame
		this.NB_FRAME_BY_SECOND = arg0.readInt();
		// taille hauteur et largeur
		this.WIDTH_FRAME = arg0.readInt();
		this.HEIGHT_FRAME = arg0.readInt();
		// min walk
		this.MIN_IND_FOR_WALK = arg0.readInt();
		// max walk
		this.MAX_IND_FOR_WALK = arg0.readInt();
		// min strike
		this.MIN_IND_FOR_STRIKE = arg0.readInt();
		// max strike
		this.MAX_IND_FOR_STRIKE = arg0.readInt();
	}





	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		// Camp et Id Type
		if(this.myCamp != null)
		out.writeInt(this.myCamp.ordinal());
		else
			out.writeInt(0);
		
		// is player ?
		out.writeBoolean(this.isPlayer);
		
		if(this.idType != null)
		out.writeInt(this.idType.ordinal());
		else
			out.writeInt(0);
		// Position
		if(this.position != null)
		{
		out.writeFloat(this.position.x);
		out.writeFloat(this.position.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		// Position Node
		if(this.positionNode != null)
		{
		out.writeFloat(this.positionNode.x);
		out.writeFloat(this.positionNode.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		// Position Final
		if(this.positionlFinal != null)
		{
		out.writeFloat(this.positionlFinal.x);
		out.writeFloat(this.positionlFinal.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		// Position Node Final
		if(this.positionNodeFinal != null)
		{
		out.writeFloat(this.positionNodeFinal.x);
		out.writeFloat(this.positionNodeFinal.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		// Rotaton
		out.writeFloat(this.rotation);
		// Speed
		out.writeFloat(this.speed);
		// Is Selected
		out.writeBoolean(this.isSelected);
		// Dir Formation
		if(this.dirFormation != null)
		{
		out.writeFloat(this.dirFormation.x);
		out.writeFloat(this.dirFormation.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		// id
		out.writeInt(this.id);
		// Id Ennemy
		out.writeInt(this.idEnemy);
		// Knocking
		out.writeBoolean(this.isKnocking);
		// StreightStrike
		out.writeInt(this.streightStrike);
		// Paths
		if(this.paths != null)
		{
			out.writeInt(this.paths.getLength());
			for(int i=0;i<this.paths.getLength();i++)
			{
				out.writeInt(this.paths.getX(i));
				out.writeInt(this.paths.getY(i));
			}
		}
		else
		{
			out.writeInt(0);
		}
		// Indice path
		out.writeInt(this.indicePaths);
		// Is Killed
		out.writeBoolean(this.isKilled);
		// Energy
		out.writeInt(this.energy);
		// Energy max
		out.writeInt(this.energyMax);
		// armor
		out.writeInt(this.armor);
		// power penetration
		out.writeInt(this.powerPenetration);
		// dexterity
		out.writeInt(this.dexterity);
		// agility
		out.writeInt(this.agility);
		// is critical strike ?
		out.writeBoolean(this.isCriticalStrike);
		// frequence entre deux frappes
		out.writeFloat(this.frequencyStrike);
		// animation
		//out.writeObject(this.animations);
		
		if(this.animations != null && this.animations.getAnimations() != null )
		{
			
			int size = this.animations.getAnimations().length;
			out.writeInt(size);
			for(int j=0;j<size;j++)
			{
				IntRect r = this.animations.getAnimations()[j];
				out.writeInt(r.left);
				out.writeInt(r.top);
				out.writeInt(r.width);
				out.writeInt(r.height); 
			}
		}
		else
		{
			out.writeInt(0);
		}
		
		// Position
		if(this.origineSprite != null)
		{
		out.writeFloat(this.origineSprite.x);
		out.writeFloat(this.origineSprite.y);
		}
		else
		{
			out.writeFloat(0f);
			out.writeFloat(0f);
		}
		
		// one contact ?
		out.writeBoolean(this.isOneContact);
		
		// is mote to enemy
		out.writeBoolean(this.isMoveToEnemy);
		
		// nombre de frame
		out.writeInt(this.NB_FRAME_BY_SECOND);
		// taille hauteur et largeur
		out.writeInt(this.WIDTH_FRAME);
		out.writeInt(this.HEIGHT_FRAME);
		// min walk
		out.writeInt(this.MIN_IND_FOR_WALK);
		// max walk
		out.writeInt(this.MAX_IND_FOR_WALK);
		// min strike
		out.writeInt(this.MIN_IND_FOR_STRIKE);
		// max strike
		out.writeInt(this.MAX_IND_FOR_STRIKE);
		
		
	}

	
	
}

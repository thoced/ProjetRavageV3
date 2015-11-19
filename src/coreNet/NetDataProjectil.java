package coreNet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class NetDataProjectil extends NetBase 
{
	private int idProjectil;
	private int idUnityTouch;
	private int damage;
	
	
	

	

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getIdProjectil() {
		return idProjectil;
	}

	public int getIdUnityTouch() {
		return idUnityTouch;
	}

	public void setIdProjectil(int idProjectil) {
		this.idProjectil = idProjectil;
	}

	public void setIdUnityTouch(int idUnityTouch) {
		this.idUnityTouch = idUnityTouch;
	}

	@Override
	
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		super.readExternal(in);
		
		// lecture de l'id du projectil
		idProjectil = in.readInt();
		// lecture de l'id de l'unité touché
		idUnityTouch = in.readInt();
		
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		super.writeExternal(out);
		
		// write de l'id du projectil
		out.writeInt(idProjectil);
		// write de l'id de l'unité touché
		out.writeInt(idUnityTouch);
		// write de la force de damage
		out.writeInt(damage);
	}
	
	
	
}

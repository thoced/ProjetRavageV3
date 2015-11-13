package coreNet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class NetStrike extends NetBase implements Externalizable
{
	private int idStriker;
	
	private int idHit;
	
	private int strenght;

	public NetStrike()
	{
		
	}
	
	public int getIdStriker() {
		return idStriker;
	}

	public int getIdHit() {
		return idHit;
	}

	public int getStrenght() {
		return strenght;
	}

	public void setIdStriker(int idStriker) {
		this.idStriker = idStriker;
	}

	public void setIdHit(int idHit) {
		this.idHit = idHit;
	}

	public void setStrenght(int strenght) {
		this.strenght = strenght;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException 
	{
		// TODO Auto-generated method stub
		super.readExternal(in);
		//striker
		this.idStriker = in.readInt();
		//hit
		this.idHit = in.readInt();
		//strenght
		this.strenght = in.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		// TODO Auto-generated method stub
		super.writeExternal(out);
		
		//striker
		out.writeInt(this.idStriker);
		//hit
		out.writeInt(this.idHit);
		//strenght
		out.writeInt(this.strenght);
		
	}
	
	
}

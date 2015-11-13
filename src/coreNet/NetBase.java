package coreNet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;


public class NetBase implements Externalizable
{
	public static enum TYPE {HELLO,CREATE,UPDATE,DELETE,SYNCHRONISED};
	// type de message
	protected TYPE typeMessage;
	
	
	public NetBase()
	{
		
	}
	
	
	public TYPE getTypeMessage() {
		return typeMessage;
	}
	public void setTypeMessage(TYPE typeMessage) {
		this.typeMessage = typeMessage;
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException 
	{
		// TYPE
		typeMessage = TYPE.values()[in.readInt()];
		
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		// TYPE
		if(typeMessage != null)
			out.writeInt(typeMessage.ordinal());
		else
			out.writeInt(0);
		
		
	} 
	
	
}

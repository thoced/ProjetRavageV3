package coreNet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class NetHello extends NetBase implements Externalizable
{
	private String nickName;
	
	private String message;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException 
	{
		// TODO Auto-generated method stub
		super.readExternal(in);
		
		//nickmane
		nickName = in.readUTF();
		// message
		message = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		// TODO Auto-generated method stub
		super.writeExternal(out);
		
		// nickName
		if(nickName != null)
			out.writeUTF(nickName);
		else
			out.writeUTF("");
		// message
		if(message != null)
			out.writeUTF(message);
		else
			out.writeUTF("");
	}
	
	
}

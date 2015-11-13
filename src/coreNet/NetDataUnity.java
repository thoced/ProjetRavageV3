package coreNet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import coreEntity.UnityBaseModel;
import coreNet.NetHeader.TYPE;

public class NetDataUnity extends NetBase implements Externalizable
{
	private UnityBaseModel model;
	
	
	public UnityBaseModel getModel() {
		return model;
	}

	public void setModel(UnityBaseModel model) {
		this.model = model;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return model.toString();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException 
	{
		// TODO Auto-generated method stub
		super.readExternal(in);
		
		//model
		model = (UnityBaseModel) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		super.writeExternal(out);
		
		//model
		out.writeObject(model);
	}

	
}

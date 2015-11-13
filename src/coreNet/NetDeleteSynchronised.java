package coreNet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class NetDeleteSynchronised extends NetBase implements Externalizable
{
	public Integer[] m_arrayId;

	public NetDeleteSynchronised(Integer[] objs)
	{
		m_arrayId = objs;
	}
	
	

	public NetDeleteSynchronised() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		// TODO Auto-generated method stub
		super.writeExternal(out);
		
		if(m_arrayId != null)
		{
			// écriture du nombre d'objets
			out.writeInt(m_arrayId.length);
			// écriture des objets
			for(int obj : m_arrayId)
			{
				out.writeInt(obj);
			}
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException 
	{
		// TODO Auto-generated method stub
		super.readExternal(in);
		// lecture du nombre d'objet
		int nb = in.readInt();
		// lecture du nombre d'objet
		m_arrayId = new Integer[nb];
		for(int i=0;i<nb;i++)
		{
			m_arrayId[i] = in.readInt();
		}
		
		
	}
	
	
}

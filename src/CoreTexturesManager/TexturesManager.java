package CoreTexturesManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import ravage.IBaseRavage;


public class TexturesManager implements IBaseRavage
{

	// hash des textures
	private static Hashtable<String,Texture> hashTextures;
	// hash des sprites
	private static Hashtable<String, Sprite> hashSprites;
	// Texture blanck pour éviter le plantage
	private static Texture blankTexture = null;
	
	// manager - permet a la methode static d'appeller une methode de l'objet
	private static TexturesManager manager;
	
	
	public TexturesManager()
	{
		// instance du hashtextures
		hashTextures = new Hashtable<String,Texture>();
		hashSprites = new Hashtable<String,Sprite>();
		
		manager = this;
	}
	
	// methode static de récupération d'un objet texture sur base du nom
	public static Texture GetTextureByName(String name)
	{
		// le nom de la texture n'existe pas, on la charge
		if(!hashTextures.containsKey(name))
			manager.LoadTexture(name);
		
		// si la texture n'existe toujours pas on retourne la texture blank
		if(!hashTextures.containsKey(name))
			return blankTexture;
		
		return hashTextures.get(name);
	}
	
	public static Sprite GetSpriteByName(String name)
	{
		// le nom du sprite n'existe pas, on le cr�e
		if(!hashSprites.containsKey(name))
		{
			manager.LoadTexture(name);
			Sprite sprite = new Sprite(GetTextureByName(name));
			hashSprites.put(name, sprite);
			return sprite;
		}
		else
			return hashSprites.get(name);
			

	}
	
	
	
	private void LoadTexture(String name) 
	{
		// TODO Auto-generated method stub
		try 
		{
			Texture text = new Texture();
			text.loadFromStream(new FileInputStream("./bin/Textures/" + name));
			
			//text.loadFromStream(TexturesManager.class.getResourceAsStream("/Textures/" + name));
			//String userdir = System.getProperty("user.dir");
			//File f = new File(userdir + "/bin/Textures/" + name);
			//JOptionPane.showMessageDialog(null, userdir);
			//text.loadFromFile(f.toPath());
			hashTextures.put(name, text);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	

	

	

}

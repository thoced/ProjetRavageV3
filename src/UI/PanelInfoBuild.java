package UI;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import CoreTexturesManager.TexturesManager;
import coreEntityManager.EntityManager;
import coreEntityManager.EntityManager.CAMP;
import coreGuiRavage.Button;
import coreGuiRavage.IButtonListener;
import coreGuiRavage.Image;
import coreGuiRavage.Label;
import coreGuiRavage.Panel;
import coreGuiRavage.ProgressBar;
import coreGuiRavage.ProgressBar.IProgressBarListener;
import coreMessageManager.MessageManager;
import coreMessageManager.MessageRavage;

public class PanelInfoBuild extends Panel implements IButtonListener, IProgressBarListener
{
	private enum TYPE_ACTION_POLL {CREATE_PIQUIER,CREATE_KNIGHT,CREATE_DUELLISTE,CREATE_HACHEUR};
	// file d'attnte de construction
	private ArrayBlockingQueue<TYPE_ACTION_POLL> m_pollCreatePiquier; 
	private ArrayBlockingQueue<TYPE_ACTION_POLL> m_pollCreateKnight; 
	private ArrayBlockingQueue<TYPE_ACTION_POLL> m_pollCreateDuelliste;
	private ArrayBlockingQueue<TYPE_ACTION_POLL> m_pollCreateHacheur; 
	// bar de progression pour les piquier
	private ProgressBar m_barPiquier;
	private Label       m_labelFilePiquier;
	// progression knight
	private ProgressBar m_barKnight;
	private Label		m_labelFileKnight;
	// progression Duelliste
	private ProgressBar m_barDuelliste;
	private Label		m_labelFileDuelliste;
	// progression Hacheur
	private ProgressBar m_barHacheur;
	private Label		m_labelFileHacheur;
	

	public PanelInfoBuild(float x, float y, Vector2f size)
			throws TextureCreationException, IOException 
	{
		super(x, y, size);
		
		// instance de la file d'attente
		m_pollCreatePiquier = new ArrayBlockingQueue<TYPE_ACTION_POLL>(256);
		m_pollCreateKnight = new ArrayBlockingQueue<TYPE_ACTION_POLL>(256);
		m_pollCreateDuelliste = new ArrayBlockingQueue<TYPE_ACTION_POLL>(256);
		m_pollCreateHacheur =  new ArrayBlockingQueue<TYPE_ACTION_POLL>(256);
		// création des boutons
		// Bouton piquier
		Button button01 = new Button(new Vector2f(16f,10f),new Vector2f(64f,64f));
		if(EntityManager.campSelected == CAMP.BLUE)
			button01.setTexture(TexturesManager.GetTextureByName("ButtonPiquierBlue.png"));
		else
			button01.setTexture(TexturesManager.GetTextureByName("ButtonPiquierYellow.png"));
		// ajout du widget au panel
		this.addWidget(button01);
		button01.setAction("CREATE_PIQUIER"); // creation de l'action
		button01.addListener(this); // ajout du listener
		// creation du progress bar piquier
		m_barPiquier = new ProgressBar(new Vector2f(16f,76f),new Vector2f(64f,4f),1f,this);
		this.addWidget(m_barPiquier);
		// création du label de file d'attente pour le piquier
		m_labelFilePiquier = new Label(new Vector2f(20f,12f));
		m_labelFilePiquier.setColor(new Color(128,128,128,256));
		m_labelFilePiquier.setText("");
		this.addWidget(m_labelFilePiquier);
		
		// création des boutons knight
		Button buttonKnight = new Button(new Vector2f(16f,84f),new Vector2f(64f,64f));
		if(EntityManager.campSelected == CAMP.BLUE)
			buttonKnight.setTexture(TexturesManager.GetTextureByName("ButtonKnightBlue.png"));
		else
			buttonKnight.setTexture(TexturesManager.GetTextureByName("ButtonKnightYellow.png"));
		// ajout du widget au panel
		this.addWidget(buttonKnight);
		buttonKnight.setAction("CREATE_KNIGHT"); // creation de l'action
		buttonKnight.addListener(this); // ajout du listener
		// creation du progress bar piquier
		m_barKnight = new ProgressBar(new Vector2f(16f,150f),new Vector2f(64f,4f),1f,this);
		this.addWidget(m_barKnight);
		// création du label de file d'attente pour le piquier
		m_labelFileKnight = new Label(new Vector2f(20f,86f));
		m_labelFileKnight.setColor(new Color(128,128,128,256));
		m_labelFileKnight.setText("");
		this.addWidget(m_labelFileKnight);
		
		// création des boutons duelliste
		
		Button buttonDuelliste = new Button(new Vector2f(16f,158f),new Vector2f(64f,64f));
		if(EntityManager.campSelected == CAMP.BLUE)
			buttonDuelliste.setTexture(TexturesManager.GetTextureByName("ButtonDuellisteBlue.png"));
		else
			buttonDuelliste.setTexture(TexturesManager.GetTextureByName("ButtonDuellisteYellow.png"));
		// ajout du widget au panel
		this.addWidget(buttonDuelliste);
		buttonDuelliste.setAction("CREATE_DUELLISTE"); // creation de l'action
		buttonDuelliste.addListener(this); // ajout du listener
		// creation du progress bar piquier
		m_barDuelliste = new ProgressBar(new Vector2f(16f,224f),new Vector2f(64f,4f),1f,this);
		this.addWidget(m_barDuelliste);
		// création du label de file d'attente pour le piquier
		m_labelFileDuelliste = new Label(new Vector2f(20f,160f));
		m_labelFileDuelliste.setColor(new Color(128,128,128,256));
		m_labelFileDuelliste.setText("");
		this.addWidget(m_labelFileDuelliste);
		
		// création des boutons Hacheur
		
		Button buttonHacheur = new Button(new Vector2f(16f,232f),new Vector2f(64f,64f));
		if(EntityManager.campSelected == CAMP.BLUE)
			buttonHacheur.setTexture(TexturesManager.GetTextureByName("ButtonHacheurBlue.png"));
		else
			buttonHacheur.setTexture(TexturesManager.GetTextureByName("ButtonHacheurYellow.png"));
		// ajout du widget au panel
		this.addWidget(buttonHacheur);
		buttonHacheur.setAction("CREATE_HACHEUR"); // creation de l'action
		buttonHacheur.addListener(this); // ajout du listener
		// creation du progress bar piquier
		m_barHacheur = new ProgressBar(new Vector2f(16f,298f),new Vector2f(64f,4f),1f,this);
		this.addWidget(m_barHacheur);
		// création du label de file d'attente pour le piquier
		m_labelFileHacheur = new Label(new Vector2f(20f,234f));
		m_labelFileHacheur.setColor(new Color(128,128,128,256));
		m_labelFileHacheur.setText("");
		this.addWidget(m_labelFileHacheur);
				
	}
	
	
	private void pollProgress(ArrayBlockingQueue<TYPE_ACTION_POLL> queue,ProgressBar bar)
	{
		if(bar != null && !bar.isInAction())
		{
			if((queue.poll()) != null)
			{
				bar.startProgressBar();
			}
		}
	}

	@Override
	public void actionListener(String action, Object source)
	{
		switch(action)
		{
			case "CREATE_PIQUIER" :
			{
				// ajout du progress bas
					 // position,size,temps max, owner pour le call back
				     // dans la file d'attente
									
					m_pollCreatePiquier.add(TYPE_ACTION_POLL.CREATE_PIQUIER);
					pollProgress(m_pollCreatePiquier,m_barPiquier); // poll
					if(m_pollCreatePiquier.size() > 0) 
						m_labelFilePiquier.setText(String.valueOf(m_pollCreatePiquier.size()));
					else
						m_labelFilePiquier.setText("");
					break;
				
				
			}
			
			case "CREATE_KNIGHT":
				
				m_pollCreateKnight.add(TYPE_ACTION_POLL.CREATE_KNIGHT);
				pollProgress(m_pollCreateKnight,m_barKnight); // poll
				if(m_pollCreateKnight.size() > 0) 
					m_labelFileKnight.setText(String.valueOf(m_pollCreateKnight.size()));
				else
					m_labelFileKnight.setText("");
				
				break;

		
			case "CREATE_DUELLISTE":
				m_pollCreateDuelliste.add(TYPE_ACTION_POLL.CREATE_DUELLISTE);
				pollProgress(m_pollCreateDuelliste,m_barDuelliste); // poll
				if(m_pollCreateDuelliste.size() > 0) 
					m_labelFileDuelliste.setText(String.valueOf(m_pollCreateDuelliste.size()));
				else
					m_labelFileDuelliste.setText("");
				
				break;
				
			case "CREATE_HACHEUR":
				m_pollCreateHacheur.add(TYPE_ACTION_POLL.CREATE_HACHEUR);
				pollProgress(m_pollCreateHacheur,m_barHacheur); // poll
				if(m_pollCreateHacheur.size() > 0) 
					m_labelFileHacheur.setText(String.valueOf(m_pollCreateHacheur.size()));
				else
					m_labelFileHacheur.setText("");
				
				break;
			
		
		}
		
		
	}

	@Override
	public void onActionProgressBar(ProgressBar owner)
	{
		// TODO Auto-generated method stub
		if(owner == m_barPiquier)
		{
			if(EntityManager.getGamePlayModel().pay(10)) // on pay
			{
					// création du piquer
					EntityManager.createPiquier();
				
				// on regarde si il existe d'autres actions dans la file d'attente
				if((m_pollCreatePiquier.poll()) != null)
				{
					// déclenchement du progress
					m_barPiquier.startProgressBar();
					
					// mise à jour du label de construction
					if(m_pollCreatePiquier.size() > 0) 
						m_labelFilePiquier.setText(String.valueOf(m_pollCreatePiquier.size()));
					else
						m_labelFilePiquier.setText("");
				}
				else
					m_labelFilePiquier.setText("");
			}
		}
		
		if(owner == m_barKnight)
		{
			if(EntityManager.getGamePlayModel().pay(10)) // on pay
			{
					// création du knight
					EntityManager.createKnight();
				
				// on regarde si il existe d'autres actions dans la file d'attente
				if((m_pollCreateKnight.poll()) != null)
				{
					// déclenchement du progress
					m_barKnight.startProgressBar();
					
					// mise à jour du label de construction
					if(m_pollCreateKnight.size() > 0) 
						m_labelFileKnight.setText(String.valueOf(m_pollCreateKnight.size()));
					else
						m_labelFileKnight.setText("");
				}
				else
					m_labelFileKnight.setText("");
			}
		}
		
		if(owner == m_barDuelliste)
		{
			if(EntityManager.getGamePlayModel().pay(10)) // on pay
			{
					// création du knight
					EntityManager.createDuelliste();
				
				// on regarde si il existe d'autres actions dans la file d'attente
				if((m_pollCreateDuelliste.poll()) != null)
				{
					// déclenchement du progress
					m_barDuelliste.startProgressBar();
					
					// mise à jour du label de construction
					if(m_pollCreateDuelliste.size() > 0) 
						m_labelFileDuelliste.setText(String.valueOf(m_pollCreateDuelliste.size()));
					else
						m_labelFileDuelliste.setText("");
				}
				else
					m_labelFileDuelliste.setText("");
			}
		}
		
		if(owner == m_barHacheur)
		{
			if(EntityManager.getGamePlayModel().pay(10)) // on pay
			{
					// création du knight
					EntityManager.createHacheur();
				
				// on regarde si il existe d'autres actions dans la file d'attente
				if((m_pollCreateHacheur.poll()) != null)
				{
					// déclenchement du progress
					m_barHacheur.startProgressBar();
					
					// mise à jour du label de construction
					if(m_pollCreateHacheur.size() > 0) 
						m_labelFileHacheur.setText(String.valueOf(m_pollCreateHacheur.size()));
					else
						m_labelFileHacheur.setText("");
				}
				else
					m_labelFileHacheur.setText("");
			}
		}
	}

}

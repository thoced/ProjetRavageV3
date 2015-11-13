package coreGUISwing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import coreEntityManager.EntityManager;
import coreEntityManager.EntityManager.CAMP;
import coreNet.INetManagerCallBack;
import coreNet.NetBase.TYPE;
import coreNet.NetDataUnity;
import coreNet.NetDeleteSynchronised;
import coreNet.NetHello;
import coreNet.NetManager;
import coreNet.NetSendThread;


import coreNet.NetStrike;

import java.awt.Color;

import javax.swing.UIManager;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.jsfml.graphics.Texture;
import org.newdawn.slick.Graphics;

import java.awt.BufferCapabilities;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;

import javax.swing.JRadioButton;

public class menuDialogRavage extends JDialog implements ActionListener, INetManagerCallBack
{
	private JTextField tNickName;
	private JTextField tIp;
	private JButton bLaunch;
	private JTextArea editorConsole;
	private JButton bHelloWorld;
	
	private StringBuilder builderString;
	private JComboBox cFlags;
	
	private NetManager netManager;
	
	private menuThread mt;
	private JRadioButton rNon;
	private JRadioButton rOui;
	private JComboBox comboResolution;
	private JComboBox cCamp;
	private JComboBox cConfigPort;
	
	
	
	public menuDialogRavage(JFrame frame,String titre,boolean modal,NetManager netmanager)
	{
		super(frame,titre,modal);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		
		netManager = netmanager;
	
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setBackground(Color.GRAY);
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// ajout dans le managernet
		NetManager.attachCallBack(this);
		
		this.setSize(640,480);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Projet Ravage !!!");
		lblNewLabel.setBounds(10, 24, 186, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nick Name:\r\n");
		lblNewLabel_1.setBounds(10, 49, 133, 14);
		getContentPane().add(lblNewLabel_1);
		
		tNickName = new JTextField();
		tNickName.setBounds(201, 46, 153, 20);
		getContentPane().add(tNickName);
		tNickName.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(10, 91, 46, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Ip adresse du joueur adverse:");
		lblNewLabel_3.setBounds(10, 74, 186, 14);
		getContentPane().add(lblNewLabel_3);
		
		tIp = new JTextField();
		tIp.setBounds(201, 71, 153, 20);
		getContentPane().add(tIp);
		tIp.setColumns(10);
		
		editorConsole = new JTextArea();
		editorConsole.setLineWrap(true);
		editorConsole.setDoubleBuffered(true);
		editorConsole.setAlignmentX(Component.LEFT_ALIGNMENT);
		JScrollPane scrollPane = new JScrollPane(editorConsole);
		scrollPane.setBounds(10, 222, 604, 166);
		getContentPane().add(scrollPane);
		
		bHelloWorld = new JButton("Hello !!!");
		bHelloWorld.setBorderPainted(false);
		bHelloWorld.setBorder(UIManager.getBorder("Button.border"));
		bHelloWorld.setForeground(Color.BLACK);
		bHelloWorld.setBackground(Color.GRAY);
		bHelloWorld.setActionCommand("HELLO");
		bHelloWorld.addActionListener(this);
		bHelloWorld.setBounds(10, 142, 89, 23);
		getContentPane().add(bHelloWorld);
		
		bLaunch = new JButton("Lancer le Jeu !!!");
		bLaunch.setBackground(Color.GRAY);
		bLaunch.addActionListener(this);
		bLaunch.setActionCommand("LAUNCH");
		bLaunch.setBounds(481, 408, 133, 23);
		getContentPane().add(bLaunch);
		
		JLabel lblNewLabel_4 = new JLabel("Flags");
		lblNewLabel_4.setBounds(364, 49, 68, 14);
		getContentPane().add(lblNewLabel_4);
		
		cFlags = new JComboBox();
		cFlags.setModel(new DefaultComboBoxModel(new String[] {"NORD-EST", "NORD-OUEST", "SUD-EST", "SUD-OUEST"}));
		cFlags.setBounds(404, 46, 168, 20);
		getContentPane().add(cFlags);
		
		comboResolution = new JComboBox();
		comboResolution.setModel(new DefaultComboBoxModel(new String[] {"1024 / 768", "1152 / 864", "1280 / 720", "1280 / 768", "1280 / 800", "1280 / 960", "1280 / 1024", "1360 / 768", "1366 / 768", "1400 / 1050", "1440 / 900", "1600 / 900", "1600 / 1024", "1680 / 1050", "1920 / 1080"}));
		comboResolution.setBounds(93, 399, 228, 20);
		getContentPane().add(comboResolution);
		
		JLabel lblNewLabel_5 = new JLabel("R\u00E9solution");
		lblNewLabel_5.setBounds(10, 399, 73, 20);
		getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("FullScreen");
		lblNewLabel_6.setBounds(10, 434, 79, 14);
		getContentPane().add(lblNewLabel_6);
		
		ButtonGroup group = new ButtonGroup();
		
		rOui = new JRadioButton("Oui");
		rOui.setBounds(126, 430, 109, 23);
		getContentPane().add(rOui);
		
		rNon = new JRadioButton("Non");
		rNon.setSelected(true);
		rNon.setBounds(237, 430, 109, 23);
		getContentPane().add(rNon);
		
		group.add(rOui);
		group.add(rNon);
		
		JLabel labelCamp = new JLabel("Camp");
		labelCamp.setBounds(364, 91, 33, 14);
		getContentPane().add(labelCamp);
		
		cCamp = new JComboBox();
		cCamp.setModel(new DefaultComboBoxModel(new String[] {"YELLOW", "BLUE"}));
		cCamp.setBounds(404, 85, 168, 20);
		getContentPane().add(cCamp);
		
		JLabel lblNewLabel_7 = new JLabel("Config test Port:\r\n");
		lblNewLabel_7.setBounds(10, 104, 153, 14);
		getContentPane().add(lblNewLabel_7);
		
		cConfigPort = new JComboBox();
		cConfigPort.setModel(new DefaultComboBoxModel(new String[] {"Config 1 : App (1234) -> App (4321)", "Config 2 : App (4321) -> App (1234)"}));
		cConfigPort.setBounds(126, 101, 228, 20);
		getContentPane().add(cConfigPort);
		
		builderString = new StringBuilder();
		
		 mt = new menuThread(netManager);
		 mt.start();
		 
		

	}
	
	public String getConfig()
	{
		return this.cConfigPort.getSelectedItem().toString();
	}
	
	public boolean isFullScreen()
	{
		if(rOui.isSelected())
			return true;
		else
			return false;
	}
	
	public int[] getResolutionScreenXY()
	{
		String resolution = (String)comboResolution.getSelectedItem();
		resolution.trim();
		String[] xy = resolution.split("/");
		int x = 1024,y = 768;
		if(xy != null && xy.length == 2)
		{
			 x = Integer.parseInt(xy[0].trim());
			 y = Integer.parseInt(xy[1].trim());
		}
		
		int[] rxy = new int[2];
		rxy[0] = x;
		rxy[1] = y;
		return rxy;
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		
		mt.interrupt();
		
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
		
		
		if(e.getActionCommand().equals("LAUNCH"))
		{
			// on positionne le flag de départ
			switch(cFlags.getSelectedIndex())
			{
			case 0 :  	NetManager.setPosxStartFlag(90);  // 340
						NetManager.setPosyStartFlag(12);   // 33
						break;
			case 1 :	NetManager.setPosxStartFlag(100);  // 36
						NetManager.setPosyStartFlag(12);    // 8
						break;
			case 2 : 	NetManager.setPosxStartFlag(90);  // 363
						NetManager.setPosyStartFlag(188);  // 214
						break;
			case 3 : 	NetManager.setPosxStartFlag(110);   // 27 
						NetManager.setPosyStartFlag(188);  //218
						break;
						
			
			}
			
			// on précise le camp sélectionné
			switch((String)cCamp.getSelectedItem())
			{
				case "YELLOW" : EntityManager.setCampSelected(CAMP.YELLOW);
				break;
				
				case "BLUE": EntityManager.setCampSelected(CAMP.BLUE);
				break;		
			}
			
			
			this.setVisible(false);
			this.dispose();
		}
		if(e.getActionCommand().equals("HELLO"))
		{
			try
			{
				// on configure l'adresse ip du joueur aProjetRdverse
				NetSendThread.configureIp(tIp.getText());
				// création du message Hello
				NetHello hello = new NetHello();
				hello.setMessage("Bien le bonjour, je serai positionné en " + cFlags.getSelectedItem().toString() );
				hello.setTypeMessage(TYPE.HELLO);
				// emission
				NetSendThread.push(hello);
				//NetManager.SendMessage(header);
					
			} catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			
		}
		
	}
	@Override
	public void onHello(NetHello hello)
	{
		// un message hello arrive, je vais l'afficher dans la console
		
		builderString.append(hello.getNickName() + " dit : Hello !!! : " + hello.getMessage());
		editorConsole.setText(builderString.toString() + System.getProperty("line.separator"));
		
	}

	@Override
	public void onCreateUnity(NetDataUnity unity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateUnity(NetDataUnity unity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSynchronised(NetDeleteSynchronised sync) {
		// TODO Auto-generated method stub
		
	}

	

}

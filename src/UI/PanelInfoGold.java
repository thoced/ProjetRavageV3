package UI;

import java.io.IOException;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;

import coreGuiRavage.Label;
import coreGuiRavage.Panel;

public class PanelInfoGold extends Panel 
{
	private static Label m_labelGoldCoin;
	private static Label m_labelNbTroops;
	
	public PanelInfoGold(float x, float y, Vector2f size)
			throws TextureCreationException, IOException {
		super(x, y, size);
		
		// placement du label du gold coin
		m_labelGoldCoin = new Label(new Vector2f(10f,18f));
		m_labelGoldCoin.setText("0 pièces d'or");
		m_labelGoldCoin.setColor(Color.YELLOW);
		this.addWidget(m_labelGoldCoin);
		// placement du label du nombre d'unité
		m_labelNbTroops = new Label(new Vector2f(160f,18f));
		m_labelNbTroops.setText("0 unité(s)");
		m_labelNbTroops.setColor(Color.WHITE);
		this.addWidget(m_labelNbTroops);
		
	}

	/**
	 * @param m_labelGoldCoin the m_labelGoldCoin to set
	 */
	public static void setM_labelGoldCoin(int gold) {
		PanelInfoGold.m_labelGoldCoin.setText(String.valueOf(gold) + " pièces d'or");
	}

	/**
	 * @param m_labelNbTroops the m_labelNbTroops to set
	 */
	public static void setM_labelNbTroops(int nbTroops) {
		PanelInfoGold.m_labelNbTroops.setText(String.valueOf(nbTroops) + " unités");
	}

	
	

}

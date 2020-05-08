package cards.main;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameTableFrame extends XMLFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container playArea;
	CardSet cardsForPlayer;
	
	public GameTableFrame (String aFrameName) {
		super (aFrameName);
		JLabel tPlayerName = new JLabel ("Player Name: " + "Fred");
		
		JPanel panel = new JPanel (new BorderLayout ());
		playArea = Box.createHorizontalBox ();
		
		panel.add (tPlayerName,  BorderLayout.NORTH);
		panel.add (playArea, BorderLayout.CENTER);
		
		add (panel);
		setSize (500, 500);
		setVisible (true);
	}

	private void showACard (Card aCard) {
		JLabel cardImage;
		cardImage = new JLabel ();
		try {
			cardImage.setIcon (aCard.getImage ());
		} catch (Exception tException) {
			System.err.println ("oops missing Image for the Card " + aCard.getFullName ());
			tException.printStackTrace ();
		}
		playArea.add (Box.createHorizontalStrut (10));
		playArea.add (cardImage);
		revalidate ();
	}

	public GameTableFrame (String aFrameName, String aGameName) {
		super (aFrameName, aGameName);
		// TODO Auto-generated constructor stub
	}
	
	public void setCardsForPlayer (CardSet aDeck) {
		cardsForPlayer = aDeck;
	}
	
	public void addToPlayArea () {
		int tCardIndex, tCardCount;
		
		tCardCount = cardsForPlayer.getCount ();
		if (tCardCount > 0) {
			Card tCard;
			for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
				tCard = cardsForPlayer.get (tCardIndex);
				showACard (tCard);
			}
		}
	}
}

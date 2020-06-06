package cards.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.utilities.OverlapLayout;

public class GameTableFrame extends XMLFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	Component strutHoriz10 = Box.createHorizontalStrut (20);
	JPanel playerInfoPanel;
	JPanel cardPanel;
	JPanel buttonsPanel;
	JButton passCards;
	JButton playCard;
	Container playArea;
	JLabel playerName;
	Hand playerHand;
	int playerIndex;
	PlayerHands playerHands;
	OverlapLayout layout;
	
	public GameTableFrame (String aFrameName) {
		super (aFrameName);
		
		setupPlayerInfoPanel ();
		setupCardPanel ();
		setupButtonsPanel ();
		setupActionListeners ();
		JPanel panel = new JPanel (new BorderLayout ());
		
		panel.add (playerInfoPanel,  BorderLayout.NORTH);
		panel.add (cardPanel, BorderLayout.CENTER);
		panel.add (buttonsPanel, BorderLayout.SOUTH);
		
		add (panel);
		setSize (700, 300);
		setVisible (true);
	}

	public void setupPlayerInfoPanel () {
		playerInfoPanel = new JPanel ();
		playerName = new JLabel ("Player Name: ");
		playerInfoPanel.add (playerName);
	}
	
	public void setupCardPanel () {
		layout = new OverlapLayout (new Point (40, 0));
		layout.setPopupInsets (new Insets (20, 0, 0, 0));
		cardPanel = new JPanel (layout);
	}

	public void setupButtonsPanel () {
		buttonsPanel = new JPanel ();
		
		passCards = new JButton ("PassCards");
		playCard = new JButton ("PlayCard");
		buttonsPanel.add (strutHoriz10);
		buttonsPanel.add (passCards, BorderLayout.SOUTH);
		buttonsPanel.add (strutHoriz10);
		buttonsPanel.add (playCard, BorderLayout.SOUTH);
		buttonsPanel.add (strutHoriz10);
	}
	
	private void setupActionListeners () {
		passCards.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				passCards ();
			}
		});
		
		playCard.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				playCard ();
			}
		});
	}
	
	public void passCards () {
		CardSet tSelectedCards;
		
		System.out.println ("Pass Cards for " + playerName.getText () + " to ");
		
		tSelectedCards = getSelectedCards ();
		System.out.println ("Selected Card Count is " + tSelectedCards.getCount ());
	}
	
	public void playCard () {
		System.out.println ("Play Card");
	}
	
	public CardSet getSelectedCards () {
		CardSet tSelectedCards;
		Card tCard;
		JLabel tCardLabel;
		int tCardCount;
		
		tSelectedCards = new CardSet ();
		tCardCount = playerHands.getPlayerAt (playerIndex).getCount ();
		for (int aCardIndex = 0; aCardIndex < tCardCount; aCardIndex++) {
			tCard = playerHands.get (playerIndex).get (aCardIndex);
			tCardLabel = tCard.getCardLabel ();
			if (! cardDown (tCardLabel)) {
				tSelectedCards.add (tCard);
				System.out.println ("Selected Card is " + tCard.getFullName ());
			}
		}
		
		return tSelectedCards;
	}
	
	public void setPlayerName (String aName) {
		playerName.setText ("Player Name: " + aName);
	}
	
	private void showACard (Card aCard) {
		JLabel cardLabel;
		
		cardLabel = new JLabel ();
		try {
			cardLabel.setIcon (aCard.getImage ());
		} catch (Exception tException) {
			System.err.println ("oops missing Image for the Card " + aCard.getFullName ());
			tException.printStackTrace ();
		}
		cardLabel.addMouseListener (this);
		aCard.setCardLabel(cardLabel);
		cardPanel.add (cardLabel);
		revalidate ();
	}

	public GameTableFrame (String aFrameName, String aGameName) {
		super (aFrameName, aGameName);
	}
	
//	public void savePlayerHand (Hand aHand) {
//		playerHand = aHand;
//		setPlayerName (playerHand.getPlayerName ());
//	}
	
	public void setPlayerHands (PlayerHands aPlayerHands) {
		playerHands = aPlayerHands;
	}
	
	public void setPlayerIndex (int aPlayerIndex) {
		playerIndex = aPlayerIndex;
	}
	
	public int getPlayerIndex () {
		return playerIndex;
	}
	
	public void addToPlayArea (int aPlayerIndex) {
		int tCardIndex, tCardCount;
		Hand tPlayerHand;
		
		setPlayerIndex (aPlayerIndex);
		tPlayerHand = playerHands.get (aPlayerIndex);
		setPlayerName (tPlayerHand.getPlayerName ());
		tCardCount = tPlayerHand.getCount ();
		if (tCardCount > 0) {
			Card tCard;
			for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
				tCard = tPlayerHand.get (tCardIndex);
				showACard (tCard);
			}
		}
	}

	public void sortCardsForPlayers () {
		Hand tPlayerHand;
		
		for (int tPlayerIndex = 0; tPlayerIndex < playerHands.size (); tPlayerIndex++) {
			tPlayerHand = playerHands.get (tPlayerIndex);
			tPlayerHand.sort ();
		}
	}
	
	public boolean cardDown (Component aCardComponent) {
		boolean tCardDown = false;
	    Boolean constraint = layout.getConstraints (aCardComponent);
		
	    if (constraint == null || constraint == OverlapLayout.POP_DOWN) {
	    	tCardDown = true;
	    }
	    
	    return tCardDown;
	}
	
	public void mousePressed (MouseEvent aMouseEvent) {
	    Component cardComponent = aMouseEvent.getComponent ();

	    if (cardDown (cardComponent)) {
	        layout.addLayoutComponent (cardComponent, OverlapLayout.POP_UP);
	    } else {
	        layout.addLayoutComponent (cardComponent, OverlapLayout.POP_DOWN);
	    }
	    
	    cardComponent.getParent ().invalidate ();
	    cardComponent.getParent ().validate ();
	}

	@Override
	public void mouseClicked (MouseEvent e) {
	}

	@Override
	public void mouseReleased (MouseEvent e) {
	}

	@Override
	public void mouseEntered (MouseEvent e) {
	}

	@Override
	public void mouseExited (MouseEvent e) {
	}

}

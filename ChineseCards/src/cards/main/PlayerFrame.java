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

public class PlayerFrame extends XMLFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	Component strutHoriz20 = Box.createHorizontalStrut (20);
	JPanel playerInfoPanel;
	JPanel cardPanel;
	JPanel buttonsPanel;
	JButton passCards;
	JButton playCard;
	Container playArea;
	JLabel nameLabel;
	JLabel scoreLabel;
	int playerIndex;
	OverlapLayout layout;
	Player player;
	
	public PlayerFrame (String aFrameName, Player aPlayer) {
		super (aFrameName);
		
		setPlayer (aPlayer);
		setupPlayerInfoPanel ();
		setupCardPanel ();
		setupButtonsPanel ();
		setupActionListeners ();
		JPanel panel = new JPanel (new BorderLayout ());
		
		panel.add (playerInfoPanel,  BorderLayout.NORTH);
		panel.add (strutHoriz20, BorderLayout.WEST);
		panel.add (cardPanel, BorderLayout.CENTER);
		panel.add (buttonsPanel, BorderLayout.SOUTH);
		
		add (panel);
		setSize (700, 300);
		setVisible (true);
	}

	public void setPlayer (Player aPlayer) {
		player = aPlayer;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public void setupPlayerInfoPanel () {
		playerInfoPanel = new JPanel ();
		nameLabel = new JLabel ("Name: " + player.getName ());
		playerInfoPanel.add (nameLabel);
		playerInfoPanel.add (strutHoriz20);
		scoreLabel = new JLabel ("Score: " + player.getScore ());
		playerInfoPanel.add (scoreLabel);
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
		buttonsPanel.add (strutHoriz20);
		buttonsPanel.add (passCards, BorderLayout.SOUTH);
		buttonsPanel.add (strutHoriz20);
		buttonsPanel.add (playCard, BorderLayout.SOUTH);
		buttonsPanel.add (strutHoriz20);
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
		Player tPassToPlayer;
		
		tPassToPlayer = player.getPlayerToPassTo ();
		System.out.println ("Pass Cards for " + nameLabel.getText () + " to " + tPassToPlayer.getName ());
		
		tSelectedCards = removeSelectedCards ();
		System.out.println ("Selected Card Count is " + tSelectedCards.getCount ());
		revalidate ();
		player.setPassed (true);
		disablePassCardsButton ();
	}
	
	public void playCard () {
		System.out.println ("Play Card");
	}
	
	public int getSelectedCount () {
		CardSet tSelectedCards;
		
		tSelectedCards = getSelectedCards ();
		
		return tSelectedCards.getCount ();
	}
	
	public CardSet getSelectedCards () {
		CardSet tSelectedCards;
		Card tCard;
		JLabel tCardLabel;
		int tCardCount;
		Hand tHand;
		
		tSelectedCards = new CardSet ();
		tHand = player.getHand ();
		tCardCount = tHand.getCount ();
		for (int aCardIndex = tCardCount - 1; aCardIndex >= 0; aCardIndex--) {
			tCard = tHand.get (aCardIndex);
			tCardLabel = tCard.getCardLabel ();
			if (! cardDown (tCardLabel)) {
				tSelectedCards.add (tCard);
			}
		}
		
		return tSelectedCards;
	}

	public CardSet removeSelectedCards () {
		CardSet tSelectedCards;
		Card tCard;
		JLabel tCardLabel;
		int tCardCount;
		Hand tHand;
		
		tSelectedCards = new CardSet ();
		tHand = player.getHand ();
		tCardCount = tHand.getCount ();
		for (int aCardIndex = tCardCount - 1; aCardIndex >= 0; aCardIndex--) {
			tCard = tHand.get (aCardIndex);
			tCardLabel = tCard.getCardLabel ();
			if (! cardDown (tCardLabel)) {
				tSelectedCards.add (tCard);
				removeCardFromFrame (tCard, tHand, aCardIndex);
			}
		}
		
		return tSelectedCards;
	}

	public void removeCardFromFrame (Card aCard, Hand aHand, int aCardIndex) {
		JLabel aCardLabel;
		
		aCardLabel = aCard.cardLabel;
		aHand.remove (aCardIndex);
		pushDown (aCardLabel);
		updateFrame (aCardLabel);
		cardPanel.remove (aCardLabel);
	}
	
	public void setPlayerName (String aName) {
		nameLabel.setText ("Name: " + aName);
	}
	
	public void updateScore (int aScore) {
		scoreLabel.setText ("Score: " + aScore);
	}
	
	private void showACard (Card aCard) {
		JLabel cardLabel;
		
		cardLabel = aCard.getCardLabel ();
		try {
			cardLabel.setIcon (aCard.getImage ());
		} catch (Exception tException) {
			System.err.println ("oops missing Image for the Card " + aCard.getFullName ());
			tException.printStackTrace ();
		}
		cardLabel.addMouseListener (this);
		cardPanel.add (cardLabel);
		revalidate ();
	}

	public PlayerFrame (String aFrameName, String aGameName) {
		super (aFrameName, aGameName);
	}
	
	public void setPlayerIndex (int aPlayerIndex) {
		playerIndex = aPlayerIndex;
	}
	
	public int getPlayerIndex () {
		return playerIndex;
	}
	
	public void showAllCardsInFrame () {
		int tCardIndex, tCardCount;
		Hand tPlayerHand;
		
		tPlayerHand = player.getHand ();
		setPlayerName (tPlayerHand.getPlayerName ());
		tCardCount = tPlayerHand.getCount ();
		if (tCardCount > 0) {
			Card tCard;
			for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
				tCard = tPlayerHand.get (tCardIndex);
				showACard (tCard);
			}
		}
		
		disablePassCardsButton ();
		disablePlayCardButton ();
		updatePassButtonText ();
	}

	private void disablePlayCardButton () {
		playCard.setEnabled (false);
	}
	
	private void enablePlayCardButton () {
		playCard.setEnabled (true);
	}

	private void disablePassCardsButton () {
		passCards.setEnabled (false);
	}
	
	private void enablePassCardsButton () {
		passCards.setEnabled (true);
	}

	public void updatePassButtonText () {
		int tPassIncrement;
		String tPassLabel;
		Player tPlayerToPassTo;
		String tPlayerName;
		
		tPlayerToPassTo = player.getPlayerToPassTo ();
		tPlayerName = tPlayerToPassTo.getName ();
		tPassIncrement = player.players.getPassIncrement ();
		tPassLabel = "Pass " + player.getPassCount () + " Cards ";
		if (tPassIncrement > 0) {
			if (tPassIncrement > 1) {
				tPassLabel += tPassIncrement + " ";
			}
			tPassLabel += "Left to " + tPlayerName;
		} else if (tPassIncrement < 0) {
			if (tPassIncrement > 1) {
				tPassLabel += tPassIncrement + " ";
			}
			tPassLabel += "Right to " + tPlayerName;
		} else {
			tPassLabel = "HOLD";
		}
		
		passCards.setText (tPassLabel);
	}
	
	public void sortCardsForPlayer () {
		player.sortCards ();
	}
	
	public boolean cardDown (Component aCardComponent) {
		boolean tCardDown = false;
	    Boolean constraint = layout.getConstraints (aCardComponent);
		
	    if (constraint == null || constraint == OverlapLayout.POP_DOWN) {
	    	tCardDown = true;
	    }
	    
	    return tCardDown;
	}
	
	public void pushUp (Component aCardComponent) {
        layout.addLayoutComponent (aCardComponent, OverlapLayout.POP_UP);
	}
	
	public void pushDown (Component aCardComponent) {
        layout.addLayoutComponent (aCardComponent, OverlapLayout.POP_DOWN);
	}

	public void updateFrame (Component aCardComponent) {
		aCardComponent.getParent ().invalidate ();
	    aCardComponent.getParent ().validate ();
	}
	
	public void mousePressed (MouseEvent aMouseEvent) {
	    Component cardComponent = aMouseEvent.getComponent ();
	    
	    if (cardDown (cardComponent)) {
	        pushUp (cardComponent);
	    } else {
	        pushDown (cardComponent);
	    }
	    
	    updateFrame (cardComponent);
	    updateButtons ();
	}

	public void updateButtons () {
		int tSelectedCount;
		int tPlayCount;
		
		tPlayCount = player.getPlayCount ();
		tSelectedCount = getSelectedCount ();
	    disablePassCardsButton ();
	    disablePlayCardButton ();
	    if (player.isNotHoldHand ()) {
		    if (player.hasNotPassed ()) {
			    if (tSelectedCount == player.getPassCount ()) {
			    	enablePassCardsButton ();
			    }
		    } else if (tSelectedCount == tPlayCount) {
		    	enablePlayCardButton ();
		    }
	    } else if (tSelectedCount == tPlayCount) {
	    	enablePlayCardButton ();
	    }
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

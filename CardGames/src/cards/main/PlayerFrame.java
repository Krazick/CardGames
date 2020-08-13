package cards.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.actions.PassCardsAction;
import cards.utilities.OverlapLayout;

public class PlayerFrame extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	Component strutHoriz20 = Box.createHorizontalStrut (20);
	JPanel playerInfoPanel;
	JPanel cardPanel;
	JPanel cardInfoPanel;
	JPanel trickInfoPanel;
	JPanel buttonsPanel;
	JPanel centerPanel;
	JPanel bottomPanel;
	JPanel panel;
	JButton pushCardsDown;
	JButton passCards;
	JButton playCard;
	Container playArea;
	JLabel nameLabel;
	JLabel scoreLabel;
	JLabel leadLabel;
	JLabel trickInfoLabel;
	int playerIndex;
	OverlapLayout layout;
	Player player;
	String gfLayoutPosition;
	
	public PlayerFrame (String aFrameName, Player aPlayer) {
		super ();
		
		centerPanel = new JPanel ();
		bottomPanel = new JPanel ();
		setPlayer (aPlayer);
		setupPlayerInfoPanel ();
		setupCardInfoPanel ();
		setupTrickInfoPanel ();
		setupButtonsPanel ();
		setupActionListeners ();
		panel = new JPanel ();
		BoxLayout tLayout = new BoxLayout (panel, BoxLayout.PAGE_AXIS);
		panel.setLayout (tLayout);
		
		add (panel);
		if (isClientPlayer ()) {
			setSize (400, 300);
		} else {
			setSize (300, 200);
		}
		setVisible (true);
	}

	private void fillPanel () {
		panel.add (playerInfoPanel);
		centerPanel.add (cardInfoPanel);
		panel.add (centerPanel);
		if (gfLayoutPosition.equals (BorderLayout.SOUTH) || gfLayoutPosition.equals (BorderLayout.NORTH)) {
			centerPanel.add (cardInfoPanel);
			centerPanel.add (trickInfoPanel);
		} else {
			bottomPanel.add (trickInfoPanel);
			panel.add (bottomPanel);
		}
		
		if (isClientPlayer ()) {
			panel.add (buttonsPanel);
		}
	}

	public void setGFLayoutPosition (String aGFLayoutPosition) {
		gfLayoutPosition = aGFLayoutPosition;
		fillPanel ();
	}
	
	public String getGFLayoutPosition () {
		return gfLayoutPosition;
	}
	
	public void setPlayer (Player aPlayer) {
		player = aPlayer;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public void setButtonsPanelInvisible () {
		buttonsPanel.setVisible (false);
	}
	
	public void setupPlayerInfoPanel () {
		playerInfoPanel = new JPanel ();
		BoxLayout tLayout = new BoxLayout (playerInfoPanel, BoxLayout.PAGE_AXIS);
		
		playerInfoPanel.setLayout (tLayout);
		
		nameLabel = new JLabel ("Name: " + player.getName ());
		playerInfoPanel.add (nameLabel);
		playerInfoPanel.add (strutHoriz20);
		scoreLabel = new JLabel ("Score: " + player.getScore ());
		playerInfoPanel.add (scoreLabel);
		leadLabel = new JLabel ("Must Lead");
		playerInfoPanel.add (leadLabel);
		playerInfoPanel.add(Box.createRigidArea (new Dimension (10, 10)));
		playerInfoPanel.setAlignmentX (CENTER_ALIGNMENT);
	}
	
	public void setupCardInfoPanel () {
		cardInfoPanel = new JPanel ();
		BoxLayout tLayout = new BoxLayout (cardInfoPanel, BoxLayout.PAGE_AXIS);
		
		cardInfoPanel.setLayout (tLayout);
		setupCardPanel ();
		cardInfoPanel.add (cardPanel);
		cardInfoPanel.add(Box.createRigidArea (new Dimension (10, 10)));
		cardInfoPanel.setAlignmentX (CENTER_ALIGNMENT);
	}
	
	public void setupTrickInfoPanel () {
		trickInfoPanel = new JPanel ();
		
		trickInfoLabel = new JLabel ("TRICK INFO PANEL");
		updateTrickInfoLabel ();
		trickInfoPanel.add (trickInfoLabel);
		trickInfoPanel.setAlignmentX (CENTER_ALIGNMENT);
	}
	
	public void updateTrickInfoLabel () {
		String tTrickInfo = "";
		int tTrickCount;
		
		tTrickCount = player.getTrickCount ();
		if (tTrickCount == 0) {
			tTrickInfo = "Tricks: NONE<br>";
		} else {
			tTrickInfo = "Trick Count: " + tTrickCount + "<br>";
		} 
		if (player.willLead ()) {
			tTrickInfo += player.getLastTrickInfo () + "<br>";
		}
		tTrickInfo += "Trick Points: " + player.getLastTrickPoints () + "<br>";
		if (isClientPlayer ()) {
			tTrickInfo += "All Points: " + player.getAllTricksPoints ();
		}
		tTrickInfo = "<html>" + tTrickInfo + "</html>";
		trickInfoLabel.setText (tTrickInfo);
	}
	
	public void updateLeadLabel () {
		if (player.willLead ()) {
			leadLabel.setVisible (true);
		} else {
			leadLabel.setVisible (false);
		}
	}
	
	public void setupCardPanel () {
		Card tCard = new Card (Card.Ranks.ACE, Card.Suits.SPADES);
		
		if (isClientPlayer ()) {
			layout = new OverlapLayout (new Point (tCard.getCardOverlapUp (), 0));
			layout.setPopupInsets (new Insets (tCard.getCardOverlapUp (), 0, 0, 0));
		} else {
			layout = new OverlapLayout (new Point (tCard.getCardOverlapDown (), 0));
			layout.setPopupInsets (new Insets (tCard.getCardOverlapDown (), 0, 0, 0));
		}
		cardPanel = new JPanel (layout);
	}

	public void setupButtonsPanel () {
		FlowLayout layout = new FlowLayout (FlowLayout.CENTER, 20, 10);
		
		buttonsPanel = new JPanel ();
		buttonsPanel.setLayout (layout);
		pushCardsDown = new JButton ("All Down");
		passCards = new JButton ("Pass Cards");
		playCard = new JButton ("Play Card");
		buttonsPanel.add (pushCardsDown, BorderLayout.SOUTH);
		buttonsPanel.add (passCards, BorderLayout.SOUTH);
		buttonsPanel.add (playCard, BorderLayout.SOUTH);
	}
	
	private void setupActionListeners () {
		pushCardsDown.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				pushAllCardsDown ();
			}
		});
		
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
	
	public void pushAllCardsDown () {
		int tCardIndex, tCardCount;
		Card tCard;
		
		tCardCount = player.getCardCount ();
		for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
			tCard = player.get (tCardIndex);
			pushDown (tCard);
		}
		updateButtons ();
		revalidate ();
	}
	
	public void updatePushDownButton () {
		if (anyCardIsUp ()) {
			pushCardsDown.setEnabled (true);
			pushCardsDown.setToolTipText ("");
		} else {
			pushCardsDown.setEnabled (false);
			pushCardsDown.setToolTipText ("No Cards are up to move down");
		}
	}
	
	public void passCards () {
		CardSet tSelectedCards;
		Player tPassToPlayer;
		int tCardCount, tCardIndex;
		Card tCard;
		PassCardsAction tPassCardsAction;
		
		disablePassCardsButton ();
		tPassToPlayer = player.getPlayerToPassTo ();
		tSelectedCards = removeSelectedCards ();
		tCardCount = tSelectedCards.getCount ();
		tPassCardsAction = new PassCardsAction (player);
		
		for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
			tCard = tSelectedCards.get (tCardIndex);
			tPassCardsAction.addPassACardEffect (player, tPassToPlayer, tCard);
			passACard (tCard, tPassToPlayer);
		}
		passedCards (tPassToPlayer);
		tPassCardsAction.addPassedCardsEffect (player, tPassToPlayer);
		player.addAction (tPassCardsAction);
	}

	public void passedCards (Player aPassToPlayer) {
		aPassToPlayer.setReceived (true);
		updateCardsInFrame (aPassToPlayer);
		player.setPassed (true);
		if (player.receivedPass ()) {
			updateCardsInFrame (player);
		}
		updateButtons ();
		revalidate ();
		
		if (player.allCardsPassed ()) {
			setStartingLead ();
		}
	}

	public void passACard (Card aCard, Player aToPlayer) {
		
		aCard.getCardLabel ().removeMouseListener (this);
		aCard.setFaceUp (false);
		removeCard (aCard);
		aToPlayer.add (aCard);
	}
	
	public void setStartingLead () {
		Player tPlayer;
		int tCurrentPlayerIndex;
		
		tPlayer = player.findLeadingPlayer ();
		tPlayer.setWillLead (true);
		tCurrentPlayerIndex = tPlayer.getIndexFor (tPlayer);
		tPlayer.setCurrentPlayer (tCurrentPlayerIndex);

		tPlayer.setWillLead (true);
	}
	
	public void updateCardsInFrame (Player aPlayer) {
		aPlayer.sortCards ();
		aPlayer.showAllCardsInFrame ();
		updateButtons ();
	}
	
	public void playCard () {
		Card tCard;
		
		if (getSelectedCount () == 1) {
			tCard = getSelectedCard ();
			player.playCard (tCard);
		}
	}
	
	public int getSelectedCount () {
		CardSet tSelectedCards;
		int tSelectedCount;
		
		if (player.getCardCount () == 1) {
			tSelectedCount = 1;
		} else {
			tSelectedCards = getSelectedCards ();
			tSelectedCount = tSelectedCards.getCount ();
		}
		
		return tSelectedCount;
	}
	
	public Card getSelectedCard () {
		CardSet tCardSet;
		Card tCard;
		int tCardCount;
		
		tCardCount = player.getCardCount ();
		if (tCardCount > 1) {
			tCardSet = getSelectedCards ();
			tCard = tCardSet.get (0);
		} else if (tCardCount == 1){
			tCard = player.get (0);
		} else {
			tCard = Card.NO_CARD;
		}
		
		return tCard;
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

	public void removeCard (Card aCard) {
		Hand tHand;
		int tCardCount;
		Card tCard;
		
		tHand = player.getHand ();
		tCardCount = tHand.getCount ();
		for (int aCardIndex = tCardCount - 1; aCardIndex >= 0; aCardIndex--) {
			tCard = tHand.get (aCardIndex);
			if (tCard.getAbbrev ().equals (aCard.getAbbrev ())) {
				removeCardFromFrame (tCard, tHand, aCardIndex);
			}
		}
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

	public void removeCardFromCardPanel (Card aCard) {
		JLabel aCardLabel;
		
		aCardLabel = aCard.cardLabel;
		cardPanel.remove (aCardLabel);
	}
	
	public void removeCardFromFrame (Card aCard, Hand aHand, int aCardIndex) {
		JLabel aCardLabel;
		
		aCardLabel = aCard.cardLabel;
		aHand.remove (aCardIndex);
		pushDown (aCardLabel);
		updateFrame (aCardLabel);
		removeCardFromCardPanel (aCard);
	}
	
	public void setPlayerName (String aName) {
		nameLabel.setText ("Name: " + aName);
	}
	
	public void updateScore (int aScore) {
		scoreLabel.setText ("Score: " + aScore);
	}
	
	public void setPlayerIndex (int aPlayerIndex) {
		playerIndex = aPlayerIndex;
	}
	
	public int getPlayerIndex () {
		return playerIndex;
	}
	
	public boolean isClientPlayer () {
		boolean tIsClientPlayer = false;
		
		tIsClientPlayer = player.isClientPlayer ();
		
		return tIsClientPlayer;
	}
	
	public void showAllCardsInFrame () {
		int tCardIndex, tCardCount;
		Hand tPlayerHand;
		boolean tWasFaceDown;
		
		tPlayerHand = player.getHand ();
		setPlayerName (tPlayerHand.getPlayerName ());
		tCardCount = tPlayerHand.getCount ();
		if (tCardCount > 0) {
			Card tCard;
			for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
				tCard = tPlayerHand.get (tCardIndex);
				if (player.hasNotPassed ()) {
					if (tCard.isFaceUp ()) {
						showACard (tCard);
					}
				} else {
					tWasFaceDown = ! tCard.isFaceUp ();
					tCard.setFaceUp (true);
					showACard (tCard);
					if (tWasFaceDown) {
						pushUp (tCard.getCardLabel ());
					}
				}
			}		
			updateButtons ();
		}
	}
	
	private void showACard (Card aCard) {
		JLabel tCardLabel;
		
		tCardLabel = aCard.getCardLabel ();
		removeAllMouseListners (tCardLabel);
		if (isClientPlayer ()) {
			addMouseListener (tCardLabel);
			aCard.setFaceUp (true);
		} else {
			aCard.setFaceUp (false);
		}
		cardPanel.add (tCardLabel);
		revalidate ();
	}

	public void addMouseListener (JLabel aCardLabel) {
		aCardLabel.addMouseListener (this);
	}

	public void removeAllMouseListners (JLabel aCardLabel) {
		MouseListener [] tAllMouseListeners;
		
		tAllMouseListeners = aCardLabel.getMouseListeners ();
		if (tAllMouseListeners.length > 0) {
			for (MouseListener tMouseListener : tAllMouseListeners) {
				aCardLabel.removeMouseListener (tMouseListener);
			}
		}
	}

	private void disablePlayCardButton () {
		playCard.setEnabled (false);
	}
	
	private void enablePlayCardButton () {
		playCard.setEnabled (true);
		playCard.setToolTipText ("");
	}

	private void disablePassCardsButton () {
		passCards.setEnabled (false);
	}
	
	private void enablePassCardsButton () {
		passCards.setEnabled (true);
		passCards.setToolTipText ("");
	}

	public void updatePassButtonText () {
		int tPassIncrement;
		String tPassLabel;
		Player tPlayerToPassTo;
		String tPlayerName;
		
		tPlayerToPassTo = player.getPlayerToPassTo ();
		tPlayerName = tPlayerToPassTo.getName ();
		tPassIncrement = player.players.getPassIncrement ();
		if (player.hasPassed ()) {
			tPassLabel = "Passed ";
		} else {
			tPassLabel = "Pass ";
		}
		tPassLabel += player.getPassCount () + " Cards ";
		if (tPassIncrement > 0) {
			tPassLabel += "Left ";
			if (tPassIncrement > 1) {
				tPassLabel += tPassIncrement + " ";
			}
			tPassLabel += "to " + tPlayerName;
		} else if (tPassIncrement < 0) {
			tPassLabel += "Right ";
			if (tPassIncrement < -1) {
				tPassLabel += (- tPassIncrement) + " ";
			}
			tPassLabel += "to " + tPlayerName;
		} else {
			tPassLabel = "HOLD";
		}
		
		passCards.setText (tPassLabel);
	}
	
	public void sortCardsForPlayer () {
		player.sortCards ();
	}
	
	public boolean anyCardIsUp () {
		boolean tAnyCardIsUp = false;
		Card tCard;
		
		for (int tCardIndex = 0; ((tCardIndex < player.getCardCount ()) && ! tAnyCardIsUp); tCardIndex++) {
			tCard = player.get (tCardIndex);
			if (! cardDown (tCard)) {
				tAnyCardIsUp = true;
			}
		}
		
		return tAnyCardIsUp;
	}
	
	public boolean cardDown (Card aCard) {
		return cardDown (aCard.getCardLabel ());
	}
	
	public boolean cardDown (Component aCardComponent) {
		boolean tCardDown = false;
	    Boolean constraint = layout.getConstraints (aCardComponent);
		
	    if (constraint == null || constraint == OverlapLayout.POP_DOWN) {
	    	tCardDown = true;
	    }
	    
	    return tCardDown;
	}
	
	public void pushUp (Card aCard) {
		pushUp (aCard.getCardLabel ());
		updateFrame (aCard.getCardLabel ());
	}
	
	public void pushDown (Card aCard) {
		pushDown (aCard.getCardLabel ());
		updateFrame (aCard.getCardLabel ());
	}
	
	public void pushUp (Component aCardComponent) {
		if (cardDown (aCardComponent)) {
			if (aCardComponent.getParent () != null) {
				layout.addLayoutComponent (aCardComponent, OverlapLayout.POP_UP);
			}
		}
	}
	
	public void pushDown (Component aCardComponent) {
		if (! cardDown (aCardComponent)) {
			if (aCardComponent.getParent () != null) {
				layout.addLayoutComponent (aCardComponent, OverlapLayout.POP_DOWN);
			}
		}
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

	public void updatePlayCardButton () {
		int tSelectedCount;
		int tPlayCount;
		
		tPlayCount = player.getPlayCount ();
		tSelectedCount = getSelectedCount ();
	    if (player.isNotHoldHand ()) {
		    if (player.hasNotPassed ()) {
		    	playCard.setToolTipText ("Must pass " + player.getPassCount () + " Cards first");
		    } else if (tSelectedCount == tPlayCount) {
		    	if (player.readyToPlay ()) {
		    		if (player.validCardToPlay (getSelectedCard ())) {
		    			enablePlayCardButton ();
		    		} else {
		    			disablePlayCardButton ();
		    			playCard.setToolTipText ("Selected Card does not match Suit Led");
		    		}
		    	} else {
		    		playCard.setToolTipText ("Not your turn to play a Card");
		    	}
		    } else {
		    	playCard.setToolTipText ("No Card selected to play");
		    }
	    } else if (tSelectedCount == tPlayCount) {
	    	if (player.readyToPlay ()) {
	    		if (player.validCardToPlay (getSelectedCard ())) {
	    			enablePlayCardButton ();
	    		} else {
	    			disablePlayCardButton ();
	    			playCard.setToolTipText ("Selected Card does not match Suit Led");
	    		}
	    	} else {
	    		playCard.setToolTipText ("Not your turn to play a Card");
	    	}
	    } else {
	    	playCard.setToolTipText ("No Card selected to play");
	    }

	}
	
	public void updatePassCardsButton () {
		int tSelectedCount;

		updatePassButtonText ();
		tSelectedCount = getSelectedCount ();
	    if (player.isNotHoldHand ()) {
		    if (player.hasNotPassed ()) {
			    if (tSelectedCount == player.getPassCount ()) {
			    	enablePassCardsButton ();
			    } else {
			    	passCards.setToolTipText ("Must select " + player.getPassCount () + " Cards to pass");
			    }
		    } else {
		    	passCards.setToolTipText ("Already Passed Cards");
		    }
	    } else {
	    	passCards.setToolTipText ("This is a Hold Hand - No Cards to Pass");
	    }

	}
	
	public void updateButtons () {
	    disablePassCardsButton ();
	    disablePlayCardButton ();
	    updatePushDownButton ();
    	updatePlayCardButton ();
    	updatePassCardsButton ();
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

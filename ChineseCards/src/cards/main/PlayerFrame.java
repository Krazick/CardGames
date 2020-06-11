package cards.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
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

public class PlayerFrame extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	Component strutHoriz20 = Box.createHorizontalStrut (20);
	JPanel playerInfoPanel;
	JPanel cardPanel;
	JPanel buttonsPanel;
	JButton pushCardsDown;
	JButton passCards;
	JButton playCard;
	Container playArea;
	JLabel nameLabel;
	JLabel scoreLabel;
	JLabel leadLabel;
	int playerIndex;
	OverlapLayout layout;
	Player player;
	
	public PlayerFrame (String aFrameName, Player aPlayer) {
		super ();
		
		setPlayer (aPlayer);
		setupPlayerInfoPanel ();
		setupCardPanel ();
		setupButtonsPanel ();
		setupActionListeners ();
		JPanel panel = new JPanel (new BorderLayout ());
		
		panel.add (playerInfoPanel,  BorderLayout.NORTH);
		panel.add (strutHoriz20, BorderLayout.WEST);
		panel.add (cardPanel, BorderLayout.CENTER);
		panel.add (strutHoriz20, BorderLayout.EAST);
		panel.add (buttonsPanel, BorderLayout.SOUTH);
		
		add (panel);
		setSize (650, 300);
		setVisible (true);
	}

	public void setPlayer (Player aPlayer) {
		player = aPlayer;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public void setupPlayerInfoPanel () {
		FlowLayout layout = new FlowLayout (FlowLayout.CENTER, 20, 10);
		
		playerInfoPanel = new JPanel ();
		playerInfoPanel.setLayout (layout);
		
		nameLabel = new JLabel ("Name: " + player.getName ());
		playerInfoPanel.add (nameLabel);
		playerInfoPanel.add (strutHoriz20);
		scoreLabel = new JLabel ("Score: " + player.getScore ());
		playerInfoPanel.add (scoreLabel);
		leadLabel = new JLabel ("Must Lead");
		playerInfoPanel.add (leadLabel);
	}
	
	public void updateLeadLabel () {
		if (player.willLead ()) {
			leadLabel.setVisible (true);
		} else {
			leadLabel.setVisible (false);
		}
	}
	
	public void setupCardPanel () {
		layout = new OverlapLayout (new Point (40, 0));
		layout.setPopupInsets (new Insets (15, 0, 0, 0));
		cardPanel = new JPanel (layout);
	}

	public void setupButtonsPanel () {
		FlowLayout layout = new FlowLayout (FlowLayout.CENTER, 20, 10);
		
		buttonsPanel = new JPanel ();
		buttonsPanel.setLayout (layout);
		pushCardsDown = new JButton ("Push All Cards Down");
		passCards = new JButton ("Pass Cards");
		playCard = new JButton ("Play Card");
		buttonsPanel.add (pushCardsDown, BorderLayout.SOUTH);
		buttonsPanel.add (passCards, BorderLayout.SOUTH);
		buttonsPanel.add (playCard, BorderLayout.SOUTH);
		pushCardsDown.setEnabled (false);
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
		} else {
			pushCardsDown.setEnabled (false);
		}
	}
	
	public void passCards () {
		CardSet tSelectedCards;
		Player tPassToPlayer;
		int tCardCount, tCardIndex;
		Card tCard;
		
		disablePassCardsButton ();
		tPassToPlayer = player.getPlayerToPassTo ();
		tSelectedCards = removeSelectedCards ();
		tCardCount = tSelectedCards.getCount ();
		
		for (tCardIndex = 0; tCardIndex < tCardCount; tCardIndex++) {
			tCard = tSelectedCards.get (tCardIndex);
			tCard.getCardLabel ().removeMouseListener (this);
			tCard.setFaceUp (false);
			tPassToPlayer.add (tCard);
		}
		tPassToPlayer.setReceived (true);
		updateCardsInFrame (tPassToPlayer);
		
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

	public void setStartingLead () {
		Player tPlayer;
		
		tPlayer = player.findLeadingPlayer ();
		tPlayer.setWillLead (true);
	}
	
	public void updateCardsInFrame (Player aPlayer) {
		aPlayer.sortCards ();
		aPlayer.showAllCardsInFrame ();
	}
	
	public void playCard () {
		Card tCard;
		
		System.out.println ("Play Card");
		if (getSelectedCount () == 1) {
			tCard = getSelectedCard ();
			player.playCard (tCard);
		}
	}
	
	public int getSelectedCount () {
		CardSet tSelectedCards;
		
		tSelectedCards = getSelectedCards ();
		
		return tSelectedCards.getCount ();
	}
	
	public Card getSelectedCard () {
		CardSet tCardSet;
		Card tCard;
		
		tCardSet = getSelectedCards ();
		tCard = tCardSet.get (0);
		
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
	
	public void setPlayerIndex (int aPlayerIndex) {
		playerIndex = aPlayerIndex;
	}
	
	public int getPlayerIndex () {
		return playerIndex;
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
		}
		
		disablePassCardsButton ();
		disablePlayCardButton ();
		updatePassButtonText ();
    	updatePushDownButton ();
	}
	
	private void showACard (Card aCard) {
		JLabel tCardLabel;
	
		tCardLabel = aCard.getCardLabel ();
		addMouseListener (tCardLabel);
		cardPanel.add (tCardLabel);
		revalidate ();
	}

	public void addMouseListener (JLabel aCardLabel) {
		MouseListener[] allMouseListeners;
		
		allMouseListeners = aCardLabel.getMouseListeners ();
		if (allMouseListeners.length == 0) {
			aCardLabel.addMouseListener (this);
		}
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
			tPassLabel += "Left ";
			if (tPassIncrement > 1) {
				tPassLabel += tPassIncrement + " ";
			}
			tPassLabel += "to " + tPlayerName;
		} else if (tPassIncrement < 0) {
			tPassLabel += "Right ";
			if (tPassIncrement > 1) {
				tPassLabel += tPassIncrement + " ";
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
    	updatePushDownButton ();
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

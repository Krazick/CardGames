package cards.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import cards.actions.StartNewRoundAction;

public class TableTop extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	Trick cardsOnTable;
	GameFrame gameFrame;
	GameManager gameManager;
	Player playerWhoLed;
	Player playerWhoWillWin;
	Card cardLed;
	Card winningCard;
	CardImage blankCard;
	ImageIcon blankIconImage;
	Player nextPlayer;
	JButton startNextRound;
	JPanel leftBox, centerBox, rightBox;
	CardImage westCard, northCard, eastCard, southCard;
	
	public TableTop (GameManager aGameManager, GameFrame aGameFrame) {
		super ();
		setGameManager (aGameManager);
		setupBlankCard ();
		setupTableTopContents();
		
		gameFrame = aGameFrame;
		startNextRound = new JButton ("Start Next Round");
		startNextRound.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				startNewRound ();
				hideStartNextRound ();
				gameFrame.revalidateRepaint ();
			}
		});
   		startNewTrick ();
	}

	private void setupBlankCard () {
		CardImages tCardImages;
		
		tCardImages = gameManager.getCardImages ();
		blankCard = tCardImages.getCardImage ("cardblank");
		blankIconImage = blankCard.getImage ();
	}
	
	private void setupTableTopContents() {
		Component tRigidVertical;
		
		westCard = new CardImage ();
		westCard.setText ("WCard");
		northCard = new CardImage ();
		northCard.setText ("NCard");
		eastCard = new CardImage ();
		eastCard.setText ("ECard");
		southCard = new CardImage ();
		southCard.setText ("SCard");
		
		setBackground (Color.cyan);
		tRigidVertical = createRigidVertical ();
		leftBox = setupEastWestBox (westCard, Color.WHITE, tRigidVertical);
		
		centerBox = new JPanel ();
		centerBox.setBackground (Color.orange);
		centerBox.setLayout (new BoxLayout (centerBox, BoxLayout.PAGE_AXIS));
		centerBox.add (northCard.getCardLabel ());
		centerBox.add (southCard.getCardLabel ());
		
		rightBox = setupEastWestBox (eastCard, Color.YELLOW, tRigidVertical);
		
		add (leftBox);
		add (Box.createHorizontalGlue());
		add (centerBox);
		add (Box.createHorizontalGlue());
		add (rightBox);
	}
	
	private JPanel setupEastWestBox (CardImage aCardImage, Color aBackgroundColor, Component aRigidArea) {
		JPanel tEastWestBox;
		
		tEastWestBox = new JPanel ();
		tEastWestBox.setBackground (aBackgroundColor);
		tEastWestBox.setLayout (new BoxLayout (tEastWestBox, BoxLayout.PAGE_AXIS));
//		tEastWestBox.add (aRigidArea);
//		tEastWestBox.add (Box.createVerticalGlue ());
		tEastWestBox.add (aCardImage.getCardLabel ());
//		tEastWestBox.add (Box.createVerticalGlue ());
//		tEastWestBox.add (aRigidArea);
		
		return tEastWestBox;
	}
	
	private Component createRigidVertical () {
		int tRigidHeight, tRigidWidth;
		Component tRigidVertical;
		
		//Box.createRigidArea(new Dimension(5,0))
		tRigidHeight = blankCard.getCardImageHeight () + 10;
		tRigidWidth = blankCard.getCardImageWidth () + 10;
		tRigidVertical = Box.createRigidArea (new Dimension (tRigidHeight, tRigidWidth));
		
		return tRigidVertical;
	}
	
	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}
	
	public GameManager getGameManager () {
		return gameManager;
	}
	
	public void startNewTrick () {
		cardsOnTable = new Trick ();
		setCardLed (Card.NO_CARD);
	}
	
	public boolean firstCard () {
		return cardsOnTable.getCount () == 0;
	}
	
	public boolean allPlayersPlayed () {
		boolean tAllPlayersPlayed = false;
		
		if (cardsOnTable.getCount () == gameManager.getPlayers ().getPlayerCount ()) {
			tAllPlayersPlayed = true;
		}
		
		return tAllPlayersPlayed;
	}
	
	public void setCardLed (Card aCardLed) {
		cardLed = aCardLed;
	}
	
	public Card getCardLed () {
		return cardLed;
	}
	
	public void playCard (Card aCard, Player aPlayer) {
		handleFirstCardPlayed (aCard, aPlayer);
		
		aCard.setFaceUp (true);
		cardsOnTable.add (aCard, aPlayer);
		aPlayer.updateButtons ();
		
		handleWinnerUpdate (aCard, aPlayer);
		
		showACard (aCard, aPlayer);
		
		if (allPlayersPlayed ()) {
			resolveTrick ();
		} else {
			moveToNextPlayer(aPlayer);
		}
		gameFrame.revalidateRepaint ();
	}

	private void moveToNextPlayer (Player aPlayer) {
		int tNextPlayerIndex;
		Player tNextPlayer;
		
		aPlayer.setReadyToPlay (false);
		tNextPlayerIndex = gameFrame.getNextPlayerIndex ();
		tNextPlayer = gameFrame.getPlayer (tNextPlayerIndex);
		gameFrame.setCurrentPlayer (tNextPlayerIndex);
		tNextPlayer.setReadyToPlay (true);
	}

	private void handleWinnerUpdate (Card aCard, Player aPlayer) {
		if (aCard.getSuit ().equals (cardLed.getSuit ())) {
			if (aCard.getRankValue () > winningCard.getRankValue ()) {
				playerWhoWillWin = aPlayer;
				winningCard = aCard;
			}
		}
	}

	private void handleFirstCardPlayed (Card aCard, Player aPlayer) {
		if (firstCard ()) {
			playerWhoLed = aPlayer;
			cardLed = aCard;
			playerWhoWillWin = aPlayer;
			winningCard = cardLed;
		}
	}
	
	public void resolveTrick () {
		int tNewCurrentPlayerIndex, tLastPlayerIndex;
		Player tLastPlayer;
		
		playerWhoLed.setWillLead (false);
		tLastPlayerIndex = gameFrame.getCurrentPlayerIndex ();
		tLastPlayer  = gameFrame.getPlayer (tLastPlayerIndex);
		tLastPlayer.setReadyToPlay (false);
		tNewCurrentPlayerIndex = gameFrame.getPlayerIndex (playerWhoWillWin);
		gameFrame.setCurrentPlayer (tNewCurrentPlayerIndex);
		
		playerWhoWillWin.addTrick (cardsOnTable);
		playerWhoWillWin.setWillLead (true);
		playerWhoWillWin.setReadyToPlay (true);
		playerWhoWillWin.updateTrickInfoLabel ();
		if (playerWhoWillWin != playerWhoLed) {
			playerWhoLed.updateTrickInfoLabel ();
		}
		removeCardsFromTable ();
	}

	
	public void removeCardsFromTable () {
		southCard.resetIconImage ();
		northCard.resetIconImage ();
		eastCard.resetIconImage ();
		westCard.resetIconImage ();
		
		gameFrame.revalidateRepaint ();
		startNewTrick ();
		if (playerWhoWillWin.getCardCount () == 0) {
			gameManager.updateAllScores ();
			if (gameManager.gameOver ()) {
				gameManager.handleGameWon ();
			} else {
				showStartNextRound ();
			}
		}
	}
	
	public void showStartNextRound () {
		add (startNextRound);
		startNextRound.setVisible (true);
	}
	
	public void hideStartNextRound () {
		startNextRound.setVisible (false);
		remove (startNextRound);
	}
	
	public void startNewRound () {
		Players tPlayers;
		Player tPlayer;
		StartNewRoundAction tStartNewRoundAction;
		Long tNewShuffleSeed;
		
		gameManager.finishRound ();
		hideStartNextRound ();
		tPlayers = gameManager.getPlayers ();
		tPlayer = tPlayers.getPlayer (0);
		tStartNewRoundAction = new StartNewRoundAction (tPlayer);
		gameManager.setNewShuffleSeed ();
		tNewShuffleSeed = gameManager.getShuffleSeed ();
		tStartNewRoundAction.addNewShuffleSeedEffect (tPlayer, tNewShuffleSeed);
		tStartNewRoundAction.addStartNewRoundEffect (tPlayer);
		tPlayer.addAction (tStartNewRoundAction);
		gameFrame.startNewRound (tNewShuffleSeed);
	}
	
	private void showACard (Card aCard, Player aPlayer) {
		String tPlayerPosition;
		ImageIcon tPlayedIconImage;
		
		tPlayerPosition = aPlayer.getGFLayoutPosition ();
		System.out.println ("Border Layout for " + aPlayer.getName () + " is " + tPlayerPosition + " Card " + aCard.getAbbrev ());

		tPlayedIconImage = aCard.getIconImage ();
		if (tPlayerPosition.equals (BorderLayout.SOUTH)) {
			southCard.setIconImage (tPlayedIconImage);
			southCard.repaint ();
			southCard.setText ("SPlayed");
		} else if (tPlayerPosition.equals (BorderLayout.NORTH)) {
			northCard.setIconImage (tPlayedIconImage);
			northCard.repaint ();
			northCard.setText ("NPlayed");
		} else if (tPlayerPosition.equals (BorderLayout.EAST)) {
			eastCard.setIconImage (tPlayedIconImage);
			eastCard.repaint ();
			eastCard.setText ("EPlayed");
		} else if (tPlayerPosition.equals (BorderLayout.WEST)) {
			westCard.setIconImage (tPlayedIconImage);
			westCard.repaint ();
			westCard.setText ("WPlayed");
		} else {
			System.err.println ("Don't know where to place for " + tPlayerPosition);
		}
	}
	
	public boolean trickIsDone () {
		boolean tTrickIsDone = false;
		
		if (cardsOnTable.getCount () == 0) {
			tTrickIsDone = true;
		}
		
		return tTrickIsDone;
	}

	@Override
	public void mouseClicked (MouseEvent e) {
		
	}

	@Override
	public void mousePressed (MouseEvent e) {
		
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

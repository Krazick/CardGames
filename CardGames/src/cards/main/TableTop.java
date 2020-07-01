package cards.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	Player nextPlayer;
	JButton startNextRound;

	public TableTop (GameManager aGameManager, GameFrame aGameFrame) {
		super ();
		this.setBackground (Color.cyan);
		setGameManager (aGameManager);
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
		int tNextPlayerIndex;
		Player tNextPlayer;
		
		if (firstCard ()) {
			playerWhoLed = aPlayer;
			cardLed = aCard;
			playerWhoWillWin = aPlayer;
			winningCard = cardLed;
		}
		cardsOnTable.add (aCard, aPlayer);
		aPlayer.updateButtons ();
		if (aCard.getSuit ().equals (cardLed.getSuit ())) {
			if (aCard.getRankValue () > winningCard.getRankValue ()) {
				playerWhoWillWin = aPlayer;
				winningCard = aCard;
			}
		}
		showACard (aCard);
		if (allPlayersPlayed ()) {
			resolveTrick ();
		} else {
			aPlayer.setReadyToPlay (false);
			tNextPlayerIndex = gameFrame.getNextPlayerIndex ();
			tNextPlayer = gameFrame.getPlayer (tNextPlayerIndex);
			gameFrame.setCurrentPlayer (tNextPlayerIndex);
			tNextPlayer.setReadyToPlay (true);
		}
		gameFrame.revalidateRepaint ();
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
		removeAll ();
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
	
	private void showACard (Card aCard) {
		JLabel tCardLabel;
	
		tCardLabel = aCard.getCardLabel ();
		add (tCardLabel);
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

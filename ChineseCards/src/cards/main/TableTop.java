package cards.main;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TableTop extends JPanel {
	private static final long serialVersionUID = 1L;
	Trick cardsOnTable;
	GameFrame gameFrame;
	GameManager gameManager;
	Player playerWhoLed;
	Player playerWhoWillWin;
	Card cardLed;
	Card winningCard;
	Player nextPlayer;
	
	public TableTop (GameManager aGameManager, GameFrame aGameFrame) {
		super ();
		setGameManager (aGameManager);
		gameFrame = aGameFrame;
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
	
	public void playCard (Card aCard, Player aPlayer) {
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
			gameFrame.revalidate ();
		}
	}
	
	public void resolveTrick () {
		playerWhoLed.setWillLead (false);
		playerWhoWillWin.addTrick (cardsOnTable);
		playerWhoWillWin.setWillLead (true);
		playerWhoWillWin.updateTrickInfoLabel ();
		if (playerWhoWillWin != playerWhoLed) {
			playerWhoLed.updateTrickInfoLabel ();
		}
		removeCardsFromTable ();
	}

	public void removeCardsFromTable () {
		removeAll ();
		validate ();
		repaint ();
		startNewTrick ();
		if (playerWhoWillWin.getCardCount () == 0) {
			startNewRound ();
		}
	}
	
	public void startNewRound () {
		Deck tGameDeck;
		Players tPlayers;
		
		tGameDeck = gameFrame.getGameDeck ();
		tPlayers = gameManager.getPlayers ();
		tPlayers.mergeTricks (tGameDeck);
		System.out.println ("Game Deck now has " + tGameDeck.getCount ());
	}
	
	private void showACard (Card aCard) {
		JLabel tCardLabel;
	
		tCardLabel = aCard.getCardLabel ();
		add (tCardLabel);
		revalidate ();
	}

	public boolean trickIsDone () {
		boolean tTrickIsDone = false;
		
		if (cardsOnTable.getCount () == 0) {
			tTrickIsDone = true;
		}
		
		return tTrickIsDone;
	}
}

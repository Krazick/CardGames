package cards.main;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TableTop extends JPanel {
	private static final long serialVersionUID = 1L;
	CardSet cardsOnTable;
	GameManager gameManager;
	Player playerWhoLed;
	Player playerWhoWillWin;
	Card cardLed;
	Card winningCard;
	Player nextPlayer;
	
	public TableTop (GameManager aGameManager) {
		super ();
		setGameManager (aGameManager);
		cardsOnTable = new CardSet ();
	}
	
	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}
	
	public GameManager getGameManager () {
		return gameManager;
	}
	
	public void clearCardsOnTable () {
		cardsOnTable.removeAll ();
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
		cardsOnTable.add (aCard);
		if (aCard.getSuit ().equals (cardLed.getSuit ())) {
			if (aCard.getRankValue () > winningCard.getRankValue ()) {
				playerWhoWillWin = aPlayer;
				winningCard = aCard;
			}
		}
		showACard (aCard);
		if (allPlayersPlayed ()) {
			resolveTrick ();
		}
	}
	
	public void resolveTrick () {
		System.out.println ("Time to resolve Trick = Won by " + playerWhoWillWin.getName ());
		playerWhoLed.setWillLead (false);
		playerWhoWillWin.addTrick (cardsOnTable);
		playerWhoWillWin.setWillLead (true);
		removeCardsFromTable ();
	}
	
	public void removeCardsFromTable () {
		cardsOnTable.removeAll ();
		removeAll ();
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

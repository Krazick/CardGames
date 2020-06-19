package cards.main;

import java.util.ArrayList;

public class Players {
	ArrayList<Player> players;
	public static final Player NO_PLAYER = null;
	private int passIncrement;
	private int passCount;
	private int playCount;
	private boolean passing;
	GameFrame gameFrame;
	
	public Players () {
		players = new ArrayList<Player> ();
		setRoundStart ();
	}

	public void setGameFrame (GameFrame aGameFrame) {
		gameFrame = aGameFrame;
	}
	
	public GameFrame getGameFrame () {
		return gameFrame;
	}
	
	public void addNewPlayer (String aName) {
		Player tPlayer;
		int tPlayerIndex;
		
		tPlayerIndex = players.size ();
		tPlayer = new Player (aName, tPlayerIndex);
		tPlayer.setPlayers (this);
		tPlayer.setGameFrame (gameFrame);
		players.add (tPlayer);
	}
	
	public void setRoundStart () {
		if (passIncrement != 0) {
			setPassing (true); 
		} else {
			setPassing (false);
		}
		for (Player tPlayer : players) {
			tPlayer.setPassed (false);
			tPlayer.setReceived (false);
			tPlayer.setReadyToPlay (false);
			tPlayer.setWillLead (false);
		}
	}
	
	public void setPassing (boolean aPassing) {
		passing = aPassing;
	}
	
	public boolean getPassing () {
		return passing;
	}
	
	public int getPlayerCount () {
		return players.size ();
	}
	
	public Player getPlayer (String aPlayerName) {
		Player tFoundPlayer;
		
		tFoundPlayer = NO_PLAYER;
		for (Player tPlayer : players) {
			if (tPlayer.getName ().equals (aPlayerName)) {
				tFoundPlayer = tPlayer;
			}
		}
			
		return tFoundPlayer;
	}
	
	public Player getPlayer (int aPlayerIndex) {
		return players.get (aPlayerIndex);
	}
	
	public void addCardTo (int aPlayerIndex, Card aCard) {
		getPlayer (aPlayerIndex).add (aCard);
	}
	
	public Card getCardFrom (int aPlayerIndex, int aCardIndex) {
		return getPlayer (aPlayerIndex).get (aCardIndex);
	}

	public void setPassIncrement (int aPassIncrement) {
		passIncrement = aPassIncrement;
	}
	
	public int getPassIncrement () {
		return passIncrement;
	}
	
	public Player getPlayerToPassTo (int aCurrentPlayerIndex) {
		Player tPlayer;
		int tPlayerToPassIndex;
		
		tPlayerToPassIndex = (aCurrentPlayerIndex + passIncrement + players.size ()) % players.size ();
		tPlayer = players.get (tPlayerToPassIndex);
		
		return tPlayer;
	}
	
	public void setPassCount (int aPassCount) {
		passCount = aPassCount;
	}
	
	public int getPassCount () {
		return passCount;
	}
	
	public void setPlayCount (int aPlayCount) {
		playCount = aPlayCount;
	}
	
	public int getPlayCount () {
		return playCount;
	}
	
	public boolean allCardsPassed () {
		boolean tAllCardsPassed = true;
		int tPlayerIndex;
		
		if (passIncrement != 0) {
			for (tPlayerIndex = 0; tPlayerIndex < getPlayerCount (); tPlayerIndex++) {
				tAllCardsPassed = tAllCardsPassed && players.get (tPlayerIndex).hasPassed ();
			}
		}
		
		return tAllCardsPassed;
	}

	public Player findLeadingPlayer () {
		Player tLeadingPlayer, tPlayer;
		int tPlayerIndex;
		Card tLowestClub, tCard;
		
		tLeadingPlayer = NO_PLAYER;
		tLowestClub = Card.NO_CARD;
		
		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			tCard = tPlayer.getLowestInSuit (Card.Suits.CLUBS);
			if (tLowestClub == Card.NO_CARD) {
				tLowestClub = tCard;
				tLeadingPlayer = tPlayer;
			} else {
				if (tCard != Card.NO_CARD) { 
					if (tCard.getRankValue () < tLowestClub.getRankValue ()) {
						tLowestClub = tCard;
						tLeadingPlayer = tPlayer;				
					}
				}
			}
		}
		
		if (tLeadingPlayer != NO_PLAYER) {
			tLeadingPlayer.setWillLead (true);
			tLeadingPlayer.setReadyToPlay (true);
		}
		
		return tLeadingPlayer;
	}

	public void setStartingLead () {
		Player tLeadingPlayer;
		int tCurrentPlayerIndex = gameFrame.NO_CURRENT_PLAYER;
		
		if (allCardsPassed ()) {
			tLeadingPlayer = findLeadingPlayer ();
			tLeadingPlayer.setWillLead (true);
			tCurrentPlayerIndex = getIndexFor (tLeadingPlayer);
		}
		setCurrentPlayer (tCurrentPlayerIndex);
	}
	
	public void setCurrentPlayer (int aCurrentPlayerIndex) {
		gameFrame.setCurrentPlayer (aCurrentPlayerIndex);		
	}
	
	public int getIndexFor (String aPlayerName) {
		int tPlayerIndex, tFoundPlayerIndex;
		Player tPlayer;

		tFoundPlayerIndex = gameFrame.NO_CURRENT_PLAYER;
		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			if (tPlayer.getName ().equals (aPlayerName)) {
				tFoundPlayerIndex = tPlayerIndex;
			}
		}
		
		return tFoundPlayerIndex;
	}
	
	public int getIndexFor (Player aPlayer) {
		int tPlayerIndex, tFoundPlayerIndex;
		Player tPlayer;

		tFoundPlayerIndex = gameFrame.NO_CURRENT_PLAYER;
		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			if (tPlayer.equals (aPlayer)) {
				tFoundPlayerIndex = tPlayerIndex;
			}
		}
		
		return tFoundPlayerIndex;
	}

	
	public void mergeTricks (Deck aGameDeck) {
		int tPlayerIndex;
		Player tPlayer;

		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			tPlayer.mergeTricks (aGameDeck);
		}
	}

	public void updateAllScores () {
		int tPlayerIndex;
		Player tPlayer;

		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			tPlayer.updateScore ();
		}
	}
	
	public void cyclePassIncrement () {
		if (passIncrement == 0) {
			passIncrement = 1;
		} else if (passIncrement == 1) {
			passIncrement = -1;
		} else if (passIncrement == -1) {
			passIncrement = 2;
		} else {
			passIncrement = 0;
		}
	}

	public boolean hasPlayer (String aPlayerName) {
		int tPlayerIndex;
		Player tPlayer;
		boolean tHasPlayer = false;

		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			if (tPlayer.getName ().equals (aPlayerName)) {
				tHasPlayer = true;
			}
		}
		
		return tHasPlayer;
	}
	
	public void printAllPlayers () {
		int tPlayerIndex;
		Player tPlayer;

		System.out.println ("Player Count " + players.size ());
		for (tPlayerIndex = 0; tPlayerIndex < players.size (); tPlayerIndex++) {
			tPlayer = players.get (tPlayerIndex);
			System.out.println ("Player " + tPlayerIndex + " is " + tPlayer.getName ());
		}
	}

	public void removeAll () {
		players.removeAll (players);
	}

	public void removePlayer (String aPlayerName) {
		int tPlayerIndex;
		
		tPlayerIndex = getIndexFor (aPlayerName);
		if (tPlayerIndex != gameFrame.NO_CURRENT_PLAYER) {
			players.remove (tPlayerIndex);
		}
	}
}

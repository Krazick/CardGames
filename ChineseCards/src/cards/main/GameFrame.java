package cards.main;

import cards.main.Deck.Types;

public class GameFrame extends XMLFrame {
	private static final long serialVersionUID = 1L;
	GameManager gameManager;
	Players players;
	
	public GameFrame (String aFrameName, GameManager aGameManager) {
		super (aFrameName);
		gameManager = aGameManager;
		players = gameManager.getPlayers ();
		setupGameFrame ();
	}

	public void setupGameFrame () {
		System.out.println ("Card Game Frame Setting Up");
		Deck gameDeck;
		Player tPlayer;
		int tPassIncrement;
		
		players = new Players ();
		players.addNewPlayer ("Mary Kay");
		players.addNewPlayer ("Mark");
		players.addNewPlayer ("Jacob");
		players.addNewPlayer ("Michelle");
		players.setPassCount (3);
		players.setPlayCount (1);
		
		gameDeck = new Deck (Types.STANDARD);
		gameDeck.shuffle ();
		gameDeck.printCards ();
		
		gameDeck.dealAllCards (players);
		tPassIncrement = 1;
		players.setPassIncrement (tPassIncrement);
		
		for (int tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayer.sortCards ();
			tPlayer.showAllCardsInFrame ();
		}
	}

	public GameFrame (String aFrameName, String aGameName) {
		super (aFrameName, aGameName);
	}

}

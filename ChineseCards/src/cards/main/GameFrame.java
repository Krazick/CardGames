package cards.main;

import java.awt.BorderLayout;
import cards.main.Deck.Types;

public class GameFrame extends XMLFrame {
	private static final long serialVersionUID = 1L;
	GameManager gameManager;
	Players players;
	Deck gameDeck;
	TableTop tableTop;
	
	public GameFrame (String aFrameName, GameManager aGameManager) {
		super (aFrameName);
		int tPassIncrement;

		gameManager = aGameManager;
		players = gameManager.getPlayers ();
		
		// Temporary to allow for 4 players created to test basic GameFrame
		players.addNewPlayer ("Mary Kay");
		players.addNewPlayer ("Jacob");
		players.addNewPlayer ("Michelle");
		
		setGameFrameToPlayers ();
		
		tPassIncrement = 1;
		players.setPassIncrement (tPassIncrement);
		players.setPassCount (3);
		players.setPlayCount (1);

		gameDeck = new Deck (Types.STANDARD);
		
		setupGameFrame ();
		
		shuffleAndDeal ();
		showHands ();
		players.setStartingLead ();
	}

	public void setGameFrameToPlayers () {
		int tPlayerIndex;
		Player tPlayer;
		
		players.setGameFrame (this);
		
		for (tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayer.setGameFrame (this);
		}
	}
	
	public void setupGameFrame () {
		Player tPlayer;
		PlayerFrame tPlayerFrame;
		String tBorderLayout;
		
		System.out.println ("Player Count " + players.getPlayerCount ());
		tableTop = new TableTop (gameManager);
		add (tableTop, BorderLayout.CENTER);
		tBorderLayout = BorderLayout.SOUTH;
		for (int tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayerFrame = tPlayer.getFrame ();
			add (tPlayerFrame, tBorderLayout);
			tBorderLayout = cycleBorderLayout (tBorderLayout);
		}
		setSize (1550, 850);
		setVisible (true);
	}
	
	public void showHands () {
		Player tPlayer;

		for (int tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayer.sortCards ();
			tPlayer.showAllCardsInFrame ();
		}
	}
	
	public void shuffleAndDeal () {
		gameDeck.shuffle ();
		gameDeck.dealAllCards (players);
	}

	public String cycleBorderLayout (String aBorderLayout) {
		if (aBorderLayout.equals (BorderLayout.SOUTH)) {
			aBorderLayout = BorderLayout.WEST;
		} else if (aBorderLayout.equals (BorderLayout.WEST)) {
			aBorderLayout = BorderLayout.NORTH;
		} else if (aBorderLayout.equals (BorderLayout.NORTH)) {
			aBorderLayout = BorderLayout.EAST;
		} else {
			aBorderLayout = BorderLayout.SOUTH;
		}
		return aBorderLayout;
	}

	public GameFrame (String aFrameName, String aGameName) {
		super (aFrameName, aGameName);
	}

	public void playCard (Card aCard, Player aPlayer) {
		tableTop.playCard (aCard, aPlayer);
		if (tableTop.trickIsDone ()) {
			revalidate ();
		}
	}
}

package cards.main;

import java.awt.BorderLayout;
import cards.main.Deck.Types;

public class GameFrame extends XMLFrame {
	private static final long serialVersionUID = 1L;
	public final static int NO_CURRENT_PLAYER = -1;
	GameManager gameManager;
	Players players;
	Deck gameDeck;
	TableTop tableTop;
	int currentPlayer;
	
	public GameFrame (String aFrameName, GameManager aGameManager) {
		super (aFrameName);
		int tPassIncrement;

		setGameManager (aGameManager);
		players = gameManager.getPlayers ();
		
		// Temporary to allow for 4 players created to test basic GameFrame
		players.addNewPlayer ("Fred");
		players.addNewPlayer ("Anna");
		players.addNewPlayer ("Harriet");
		
		setGameFrameToPlayers ();
		
		tPassIncrement = 0;
		players.setPassIncrement (tPassIncrement);
		players.setPassCount (3);
		players.setPlayCount (1);

		setGameDeck (new Deck (Types.STANDARD));
		
		setupGameFrame ();
		
		startNewRound ();
	}

	public Card getCardLed () {
		return tableTop.getCardLed ();
	}
	
	public int getPlayerIndex (Player tPlayer) {
		return players.getIndexFor (tPlayer); 
	}
	
	public void setCurrentPlayer (int aCurrentPlayer) {
		currentPlayer = aCurrentPlayer;
	}
	
	public int getCurrentPlayerIndex () {
		return currentPlayer;
	}
	
	public int getNextPlayerIndex () {
		int tNextPlayer;
		
		tNextPlayer = (currentPlayer + 1) % players.getPlayerCount ();
		
		return tNextPlayer;
	}
	
	public Player getPlayer (int aPlayerIndex) {
		return players.getPlayer (aPlayerIndex);
	}
	
	public void setGameDeck (Deck aGameDeck) {
		gameDeck = aGameDeck;
	}
	
	public Deck getGameDeck () {
		return gameDeck;
	}
	
	public void startNewRound () {
		players.cyclePassIncrement ();
		players.setRoundStart ();
		shuffleAndDeal ();
		showHands ();
		players.setStartingLead ();
		revalidateRepaint ();
	}

	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}

	public GameManager getGameManager () {
		return gameManager;
	}
	
	public Player getClientPlayer () {
		return gameManager.getClientPlayer ();
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
		tableTop = new TableTop (gameManager, this);
		add (tableTop, BorderLayout.CENTER);
		tBorderLayout = BorderLayout.SOUTH;
		for (int tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayerFrame = tPlayer.getFrame ();
			add (tPlayerFrame, tBorderLayout);
			tBorderLayout = cycleBorderLayout (tBorderLayout);
		}
		setSize (1024, 775);
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
			revalidateRepaint ();
		}
	}
	
	public void revalidateRepaint () {
		revalidate ();
		repaint ();
	}
}

package cards.main;

import java.awt.BorderLayout;

import cards.actions.Action;
import cards.main.Deck.Types;

public class GameFrame extends XMLFrame {
	private static final long serialVersionUID = 1L;
	public static final String CLIENT_POSITION = BorderLayout.SOUTH;
	public final static int NO_CURRENT_PLAYER = -1;
	GameManager gameManager;
	Players players;
	Deck gameDeck;
	TableTop tableTop;
	int currentPlayer;
	int passIncrement;
	int frameWidth = 550;
	int frameHeight = 775;
	
	public GameFrame (String aFrameName, GameManager aGameManager) {
		super (aFrameName);

		CardImages tCardImages;
		
		setGameManager (aGameManager);
		passIncrement = 0;
		tCardImages = gameManager.getCardImages ();
		setGameDeck (new Deck (Types.STANDARD, tCardImages));
	}

	public void setPlayers (Players aPlayers) {
		players = aPlayers;
		players.setPassIncrement (passIncrement);
		players.setPassCount (3);
		players.setPlayCount (1);
		setGameFrameToPlayers ();
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

	public void setGameFrameName (String aFrameName) {
		setTitle (aFrameName);
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
	
	public void finishRound () {
		Deck tGameDeck;
		Players tPlayers;
		
		tGameDeck = getGameDeck ();
		tPlayers = gameManager.getPlayers ();
		tPlayers.mergeTricks (tGameDeck);
		tableTop.hideStartNextRound ();
	}
	
	public void startNewGame () {
		
		tableTop.startNewRound ();
	}
	
	public void startNewRound (Long aShuffleSeed) {
		players.cyclePassIncrement ();
		players.setRoundStart ();
		shuffleAndDeal (aShuffleSeed);
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
		
	public void setupGameFrame () {
		Player tPlayer;
		PlayerFrame tPlayerFrame;
		String tBorderLayout;
		int tClientUserIndex, tPlayerIndex, tPlayerCount;
		
		tableTop = new TableTop (gameManager, this);
		add (tableTop, BorderLayout.CENTER);
		tClientUserIndex = players.getIndexFor (getClientPlayer());
		tBorderLayout = BorderLayout.SOUTH;
		tPlayerCount = players.getPlayerCount ();
		if (tClientUserIndex > 0) {
			for (tPlayerIndex = 0; tPlayerIndex < (tPlayerCount - tClientUserIndex); tPlayerIndex++) {
				tBorderLayout = cycleBorderLayout (tBorderLayout);
			}
		}
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayerFrame = tPlayer.getFrame ();
			if (tBorderLayout != BorderLayout.SOUTH) {
				tPlayerFrame.setButtonsPanelInvisible ();
			}
			tPlayerFrame.setGFLayoutPosition (tBorderLayout);
			add (tPlayerFrame, tBorderLayout);
			tBorderLayout = cycleBorderLayout (tBorderLayout);
		}
		setSize (frameWidth, frameHeight);
	}
	
	public void showHands () {
		Player tPlayer;

		setVisible (true);
		for (int tPlayerIndex = 0; tPlayerIndex < players.getPlayerCount (); tPlayerIndex++) {
			tPlayer = players.getPlayer (tPlayerIndex);
			tPlayer.sortCards ();
			tPlayer.showAllCardsInFrame ();
		}
	}
	
	public void shuffleAndDeal (long aShuffleSeed) {
		gameDeck.shuffle (aShuffleSeed);
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
	
	public void addAction (Action aAction) {
		gameManager.addAction (aAction);
	}
	
	public boolean isClientPlayer (String aPlayerName) {
		return gameManager.isClientPlayer (aPlayerName);
	}
}

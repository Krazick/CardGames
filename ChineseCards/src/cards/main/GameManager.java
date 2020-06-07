package cards.main;

import java.awt.Component;

import cards.config.GameFrameConfig;
import cards.main.Deck.Types;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;

public class GameManager extends Component implements NetworkGameSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	public static PlayerFrame playerFrame;
	public static Players players;
	
	public GameManager () {
	}

	public static void main (String[] args) {
		System.out.println ("Card Game Manager");
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

	public GameFrameConfig getGameFrameConfig () {
		return null;
	}

	@Override
	public void updatePlayerCountLabel () {
	}

	@Override
	public void addNetworkPlayer (String aPlayerName) {
	}

	@Override
	public void removeNetworkPlayer (String aPlayerName) {
	}

	@Override
	public void removeAllNetworkPlayers () {
	}

	@Override
	public void handleGameActivity (String aGameActivity) {
	}

	@Override
	public JGameClient getNetworkJGameClient () {
		return null;
	}

	@Override
	public int getSelectedGameIndex () {
		return 0;
	}

	@Override
	public void setSelectedGameIndex (int aGameIndex) {
	}

	@Override
	public String getPlayersInOrder () {
		return null;
	}

	@Override
	public void randomizePlayerOrder () {
	}

	@Override
	public void initiateNetworkGame () {
	}

	@Override
	public boolean gameStarted () {
		return false;
	}

	@Override
	public void addNewFrame (XMLFrame jGameClient) {
	}
}

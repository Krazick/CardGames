package cards.main;

import java.awt.Component;

import cards.config.GameFrameConfig;
import cards.main.Deck.Types;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;

public class GameManager extends Component implements NetworkGameSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	public static GameTableFrame gameTableFrame;
	
	public GameManager () {
	}

	public static void main (String[] args) {
		System.out.println ("Card Game Manager");
		Deck gameDeck;
		PlayerHands playerHands;
		
		playerHands = new PlayerHands ();
		playerHands.add ("Mary Kay");
		playerHands.add ("Mark");
		playerHands.add ("Michelle");
		playerHands.add ("Vivian");
		
		gameDeck = new Deck (Types.STANDARD);
		gameDeck.printCards ();
		System.out.println ("\n\n---------SHUFFLE----------\n\n");
		gameDeck.shuffle ();
		gameDeck.printCards ();
		
		gameDeck.dealAllCards (playerHands);
		
		for (int tPlayerIndex = 0; tPlayerIndex < playerHands.size (); tPlayerIndex++) {
			gameTableFrame = new GameTableFrame ("Game Table");
			gameTableFrame.setPlayerHands (playerHands);
			gameTableFrame.sortCardsForPlayers ();
			gameTableFrame.addToPlayArea (tPlayerIndex);
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

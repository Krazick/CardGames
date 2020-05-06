package cards.main;

import java.awt.Component;

import cards.config.GameFrameConfig;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;

public class GameManager extends Component implements NetworkGameSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	public static GameTableFrame gameTableFrame;
	
	public GameManager () {
		// TODO Auto-generated constructor stub
	}

	public static void main (String[] args) {
		System.out.println ("Card Game Manager");
		Deck gameDeck;
		Deck playerHand;
		
		gameDeck = new Deck ("Standard");
		gameDeck.printCards ();
		System.out.println ("\n\n---------SHUFFLE----------\n\n");
		gameDeck.shuffle ();
		gameDeck.printCards ();
		playerHand = new Deck ();
		
		for (int i = 0; i < 13; i++) {
			gameDeck.dealACard (playerHand);
		}
		
		gameTableFrame = new GameTableFrame ("Game Table");
		gameTableFrame.setCardsForPlayer (playerHand);
		gameTableFrame.addToPlayArea ();
	}

	public GameFrameConfig getGameFrameConfig () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlayerCountLabel () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNetworkPlayer (String aPlayerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNetworkPlayer (String aPlayerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllNetworkPlayers () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleGameActivity (String aGameActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JGameClient getNetworkJGameClient () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSelectedGameIndex () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSelectedGameIndex (int aGameIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPlayersInOrder () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void randomizePlayerOrder () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateNetworkGame () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean gameStarted () {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addNewFrame (XMLFrame jGameClient) {
		// TODO Auto-generated method stub
		
	}

}

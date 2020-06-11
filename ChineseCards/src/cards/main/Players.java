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
		setPassing (true);
		for (Player tPlayer : players) {
			tPlayer.setPassed (false);
			tPlayer.setReceived (false);
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
}

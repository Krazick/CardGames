package cards.main;

public class Hand extends CardSet {
	String playerName;

	public Hand (String aPlayerName) {
		super ();
		setPlayerName (aPlayerName);
	}
	
	public void setPlayerName (String aPlayerName) {
		playerName = aPlayerName;
	}
	
	public String getPlayerName () {
		return playerName;
	}
}

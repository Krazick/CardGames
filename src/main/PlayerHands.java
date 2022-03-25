package cards.main;

import java.util.ArrayList;

public class PlayerHands {
	ArrayList<Hand> playerHands;
	
	public PlayerHands () {
		playerHands = new ArrayList<Hand> (); 
	}

	public void add (String aName) {
		Hand tNewPlayerHand;
		
		tNewPlayerHand = new Hand (aName);
		playerHands.add (tNewPlayerHand);		
	}
	
	public void addPlayer (String aName) {
		add (aName);
	}
	
	public int size () {
		return playerHands.size ();
	}
	
	public Hand get (int aPlayerIndex) {
		return playerHands.get (aPlayerIndex);
	}
	
	public Hand getPlayerAt (int aPlayerIndex) {
		return get (aPlayerIndex);
	}
	
	public String getPlayerName (int aPlayerIndex) {
		return get (aPlayerIndex).getPlayerName ();
	}
	
	public void remove (int aPlayerIndex) {
		playerHands.remove (aPlayerIndex);
	}
}

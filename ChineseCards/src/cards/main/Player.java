package cards.main;

import java.util.ArrayList;

import cards.main.Card.Suits;
import cards.network.NetworkPlayer;

public class Player {
	PlayerFrame frame;
	Hand hand;
	String name;
	int score;
	int myIndex;
	ArrayList<Trick> tricks;
	NetworkPlayer networkPlayer;
	Players players;
	boolean passed;
	boolean received;
	boolean willLead;
	GameFrame gameFrame;
	
	public Player (String aName, int aMyIndex) {
		setName (aName);
		setNewTricks ();
		frame = new PlayerFrame ("Player Frame", this);
		setScore (0);
		setNetworkPlayer (name);
		setNewHand (name);
		setMyIndex (aMyIndex);
		setWillLead (false);
	}

	public void setWillLead (boolean aWillLead) {
		willLead = aWillLead;
		frame.updateLeadLabel ();
	}
	
	public boolean willLead () {
		return willLead;
	}
	
	public void setGameFrame (GameFrame aGameFrame) {
		gameFrame = aGameFrame;
	}
	
	public GameFrame getGameFrame () {
		return gameFrame;
	}

	public void setPlayers (Players aPlayers) {
		players = aPlayers;
	}
	
	public void setMyIndex (int aMyIndex) {
		myIndex = aMyIndex;
	}
	
	public int getMyIndex () {
		return myIndex;
	}
	
	public void setName (String aName) {
		name = aName;
	}
	
	public void setScore (int aScore) {
		score = aScore;
	}
	
	public void setNewHand (String aName) {
		hand = new Hand (aName);
	}
	
	public void setNewTricks () {
		tricks = new ArrayList<Trick> ();
	}
	
	public void setNetworkPlayer (String aName) {
		networkPlayer = new NetworkPlayer (aName);
	}
	
	public NetworkPlayer getNetworkPlayer () {
		return networkPlayer;
	}
	
	public Hand getHand () {
		return hand;
	}
	
	public String getName () {
		return name;
	}
	
	public int getScore () {
		return score;
	}
	
	public PlayerFrame getFrame () {
		return frame;
	}
	
	public void addToScore (int aPoints) {
		score += aPoints;
	}
	
	public void add (Card aCard) {
		hand.add (aCard);
	}
	
	public int getCardCount () {
		return hand.getCount ();
	}
	
	public Card get (int aCardIndex) {
		return hand.get (aCardIndex);
	}
	
	public void remove (int aCardIndex) {
		hand.remove (aCardIndex);
	}
	
	public void addTrick (Trick aTrick) {
		tricks.add (aTrick);
	}
	
	public void removeAllTricks () {
		tricks.removeAll (tricks);
	}
	
	public int getTrickCount () {
		return tricks.size ();
	}
	
	public String getAllTricksInfo () {
		String tTrickInfo = "";
		int tTrickIndex, tTrickCount;
		
		tTrickCount = getTrickCount ();
		if (tTrickCount > 0) {
			for (tTrickIndex = 0; tTrickIndex < tTrickCount; tTrickIndex++) {
				tTrickInfo += tricks.get (tTrickIndex).getTrickInfo ();
			}
		}
		
		return tTrickInfo;
	}
	
	public int getAllTricksPoints () {
		int tTrickPoints = 0;
		int tTrickIndex, tTrickCount;
		Trick tTrick;
		
		tTrickCount = getTrickCount ();
		if (tTrickCount > 0) {
			System.out.println ("Getting All Tricks Points for " + name + " Count " + tTrickCount);
			for (tTrickIndex = 0; tTrickIndex < tTrickCount; tTrickIndex++) {
				tTrick = tricks.get (tTrickIndex);
				tTrickPoints += tTrick.getTrickPoints ();
				tTrick.printAllTrickInfo ();
				System.out.println ("Points up to " + tTrickPoints);
			}

		}
		
		return tTrickPoints;
	}
	
	public int getLastTrickPoints () {
		int tTrickPoints = 0;
		Trick tTrick;
		
		if (getTrickCount () > 0) {
			tTrick = tricks.get (getTrickCount () - 1);
			tTrickPoints += tTrick.getTrickPoints ();
		}
		
		return tTrickPoints;
	}
	
	public String getLastTrickInfo () {
		String tTrickInfo = "";
		Trick tTrick;
		
		if (getTrickCount () > 0) {
			tTrick = tricks.get (getTrickCount () - 1);
			tTrickInfo += tTrick.getTrickInfo ();
		}
		
		return tTrickInfo;
	}
	
	public void sortCards () {
		hand.sort ();
	}
	
	public void showAllCardsInFrame () {
		frame.showAllCardsInFrame ();
	}
	
	public Player getPlayerToPassTo () {
		Player tPlayer;
		
		tPlayer = players.getPlayerToPassTo (myIndex);
		
		return tPlayer;
	}
	
	public int getPassCount () {
		return players.getPassCount ();
	}
	
	public int getPlayCount () {
		return players.getPlayCount ();
	}
		
	public boolean getPassing () {
		return players.getPassing ();
	}

	public void setPassing (boolean aPassing) {
		players.setPassing (aPassing);
	}
	
	public boolean hasNotPassed () {
		return ( ! passed );
	}

	public boolean hasPassed () {
		return passed;
	}
	
	public boolean allCardsPassed () {
		return players.allCardsPassed ();
	}
	
	public void setPassed (boolean aPassed) {
		passed = aPassed;
	}
	
	public boolean isNotHoldHand () {
		return (players.getPassIncrement () != 0);
	}
	
	public void setReceived (boolean aReceived) {
		received = aReceived;
	}
	
	public boolean getReceived () {
		return received;
	}
	
	public boolean receivedPass () {
		return received;
	}
	
	public void playCard (Card aCard) {
		hand.remove (aCard);
		gameFrame.playCard (aCard, this);
	}

	public Player findLeadingPlayer () {
		return players.findLeadingPlayer ();
	}

	public Card getLowestInSuit (Suits aCardSuit) {
		return hand.getLowestInSuit (aCardSuit);
	}

	public void updateButtons () {
		frame.updateButtons ();
	}
	
	public void updateTrickInfoLabel () {
		frame.updateTrickInfoLabel ();
	}
}

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
	boolean readyToPlay;
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
		setReadyToPlay (false);
	}

	public void setReadyToPlay (boolean aReadyToPlay) {
		readyToPlay = aReadyToPlay;
	}
	
	public boolean readyToPlay () {
		return readyToPlay;
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
			for (tTrickIndex = 0; tTrickIndex < tTrickCount; tTrickIndex++) {
				tTrick = tricks.get (tTrickIndex);
				tTrickPoints += tTrick.getTrickPoints ();
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
		frame.removeCardFromCardPanel (aCard);
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

	public void mergeTricks (Deck aGameDeck) {
		int tTrickIndex;
		Trick tTrick;

		for (tTrickIndex = 0; tTrickIndex < tricks.size (); tTrickIndex++) {
			tTrick = tricks.get (tTrickIndex);
			aGameDeck.add (tTrick);
		}
		tricks.removeAll (tricks);
		updateTrickInfoLabel ();
	}

	public void updateScore () {
		int tAllTrickPoints;
		int tNewScore;
		
		tAllTrickPoints = getAllTricksPoints ();
		if (tAllTrickPoints == 26) {
			// Should ask if deduct from Score, to Add to Other Player's Scores
			tNewScore = score - tAllTrickPoints;
		} else {
			tNewScore = score + tAllTrickPoints;
		}
		setScore (tNewScore);
		frame.updateScore (score);
	}
	
	public void setCurrentPlayer (int aCurrentPlayerIndex) {
		gameFrame.setCurrentPlayer (aCurrentPlayerIndex);		
	}

	public int getIndexFor (Player aPlayer) {
		return players.getIndexFor (aPlayer);
	}

	public boolean validCardToPlay (Card aSelectedCard) {
		Card aCardLed, aLowestCardInSuit;
		boolean aValidCard = false;
		
		aCardLed = gameFrame.getCardLed ();
		if (aCardLed == Card.NO_CARD) {
			aValidCard = true;
		} else {
			if (aSelectedCard.isSameSuit (aCardLed)) {
				aValidCard = true;
			} else {
				aLowestCardInSuit = hand.getLowestInSuit (aCardLed.getTheSuit ());
				if (aLowestCardInSuit == Card.NO_CARD) {
					aValidCard = true;
				}
			}
		}
		
		return aValidCard;
	}
}

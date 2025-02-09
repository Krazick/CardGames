package cards.main;

import java.util.Collections;
import java.util.Random;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public class Deck extends CardSet {
	public enum Types { STANDARD, STANDARD_JOKERS, UNKNOWN };
	
	public Deck () {
		buildEmptyCardSet ();		
	}
	
	public Deck (Types aType, CardImages aCardImages) {
		Card tCard;
		
		buildEmptyCardSet ();
		
		if (aType.equals (Types.STANDARD)) {
			fillStandardDeck (aCardImages);
		} else if (aType.equals (Types.STANDARD_JOKERS)) {
			fillStandardDeck (aCardImages);
			tCard = new Card (Ranks.JOKER, Suits.JOKER_RED, aCardImages);
			cards.add (tCard);
			tCard = new Card (Ranks.JOKER, Suits.JOKER_BLACK, aCardImages);
			cards.add (tCard);
		} else {
			System.err.println ("Not a known Deck Type");
		}
	}
	
	private void fillStandardDeck (CardImages aCardImages) {
		int tRankIndex;
		Card tCard;
		
		for (Suits tSuit : Card.Suits.values ()) {
			if (tSuit.getSymbol ().length () == 1) {
				for (tRankIndex = Card.MIN_RANK_INDEX; tRankIndex <= Card.MAX_RANK_INDEX; tRankIndex++) {
					tCard = new Card (tRankIndex, tSuit, aCardImages);
					cards.add (tCard);
				}
			}
		}
	}

	public void shuffle (Long aShuffleSeed) {
		Collections.shuffle (cards, new Random (aShuffleSeed));
	}
	
	public boolean duplicate (CardSet aDuplicateDeck) {
		boolean tDuplicated = false;
		
		if ((aDuplicateDeck != null) && (aDuplicateDeck.isEmpty ())) {
			if (isEmpty ()) {
				tDuplicated = true;
			} else {
				for (Card tCard : cards) {
					aDuplicateDeck.add (tCard);
				}
				tDuplicated = true;
			}
		} else {
			tDuplicated = false;
		}
		
		return tDuplicated;
	}
	
	// Add the entire Deck/Hand being passed in to the current Deck
	// Equivalent to a "MergeDecks" type of function.
	public boolean add (CardSet aCardSet) {
		Card tCard;
		boolean tGoodMerge = false;
		
		if (aCardSet != null) {
			while (! aCardSet.isEmpty ()) {
				tCard = aCardSet.pullFirstCard ();
				add (tCard);
			}
			tGoodMerge = true;
		}
		
		return tGoodMerge;
	}
	
	public Card getTopCard () {
		return get (0);
	}
	
	public void removeTopCard () {
		remove (0);
	}
	
	public Card pullTopCard () {
		Card tCard;
		
		tCard = getTopCard ();
		removeTopCard ();
		
		return tCard ;
	}
	
	public boolean dealACard (Player aPlayer) {
		boolean tGoodDeal = false;
		CardSet tCardSet;
		
		tCardSet = aPlayer.getHand ();
		tGoodDeal = dealACard (tCardSet);
		
		return tGoodDeal;
	}
	
	public boolean dealACard (CardSet aToCardSet) {
		boolean tGoodDeal = false;
		
		if (aToCardSet != null) {
			if (! isEmpty ()) {
				Card tThisCard;
				
				tThisCard = pullTopCard ();
				aToCardSet.add (tThisCard);
				tGoodDeal = true;
			}
		}
		
		return tGoodDeal;
	}
		
	private boolean goodPlayerHands (PlayerHands aPlayerHands) {
		boolean tGoodPlayerHands = false;
		int tPlayerCount, tPlayerIndex;
		
		if (aPlayerHands != null) {
			tPlayerCount = aPlayerHands.size ();
			if (tPlayerCount > 0) {
				tGoodPlayerHands = true;
				for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
					if (aPlayerHands.get (tPlayerIndex) == null) {
						tGoodPlayerHands = false;
					}
				}
			}
		}
		
		return tGoodPlayerHands;
	}
	
	public boolean dealAllCards (Players aPlayers) {
		boolean tGoodDeal = false;
		int tPlayerIndex, tPlayerCount;
		
		tPlayerCount = aPlayers.getPlayerCount ();
		if (tPlayerCount > 0) {
			tPlayerIndex = 0;
			while (! isEmpty ()) {
				dealACard (aPlayers.getPlayer (tPlayerIndex));
				tPlayerIndex = (tPlayerIndex + 1) % tPlayerCount;
			}
			tGoodDeal = true;
		}
		
		return tGoodDeal;
	}
	
	public boolean dealAllCards (PlayerHands aPlayerHands) {
		int tPlayerIndex, tPlayerCount;
		boolean tGoodPlayerHands;
		boolean tGoodDeal = false;
		
		tGoodPlayerHands = goodPlayerHands (aPlayerHands);
		if (tGoodPlayerHands) {
			tPlayerCount = aPlayerHands.size ();
			tPlayerIndex = 0;
			while (! isEmpty ()) {
				dealACard (aPlayerHands.get (tPlayerIndex));
				tPlayerIndex = (tPlayerIndex + 1) % tPlayerCount;
			}
			tGoodDeal = true;
		}
		
		return tGoodDeal;
	}
	
	public boolean dealXCards (PlayerHands aPlayerHands, int aDealCount) {
		int tPlayerIndex, tPlayerCount, tCardCount;
		boolean tGoodDeal = false;
		boolean tGoodPlayerHands;
		
		tGoodPlayerHands = goodPlayerHands (aPlayerHands);
		if (tGoodPlayerHands) {
			tPlayerCount = aPlayerHands.size ();
			if (aDealCount > 0) {
				if ((tPlayerCount * aDealCount) <= getCount ()) {
					tPlayerIndex = 0;
					tCardCount = 0;
					while (tCardCount < (tPlayerCount * aDealCount)) {
						dealACard (aPlayerHands.get (tPlayerIndex));
						tPlayerIndex = (tPlayerIndex + 1) % tPlayerCount;
						tCardCount++;
					}
					tGoodDeal = true;
				}
			}
		}
		
		return tGoodDeal;
	}
}

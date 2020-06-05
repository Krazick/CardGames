package cards.main;

import java.util.ArrayList;
import java.util.Collections;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public class Deck extends CardSet {
	public enum Types { STANDARD, STANDARD_JOKERS, UNKNOWN };
	
	public Deck () {
		buildEmptyCardSet ();		
	}
	
	public Deck (Types aType) {
		Card tCard;
		
		buildEmptyCardSet ();
		
		if (aType.equals (Types.STANDARD)) {
			fillStandardDeck ();
		} else if (aType.equals (Types.STANDARD_JOKERS)) {
			fillStandardDeck ();
			tCard = new Card (Ranks.JOKER, Suits.JOKER_RED);
			cards.add (tCard);
			tCard = new Card (Ranks.JOKER, Suits.JOKER_BLACK);
			cards.add (tCard);
		} else {
			System.err.println ("Not a known Deck Type");
		}
	}

	private void fillStandardDeck () {
		int tRankIndex;
		Card tCard;
		
		for (Suits tSuit : Card.Suits.values ()) {
			if (tSuit.getSymbol ().length() == 1) {
				for (tRankIndex = Card.MIN_RANK_INDEX; tRankIndex <= Card.MAX_RANK_INDEX; tRankIndex++) {
					tCard = new Card (tRankIndex, tSuit);
					cards.add (tCard);
				}
			}
		}
	}

	public void shuffle () {
		Collections.shuffle (cards);
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
	
	private boolean goodPlayerHands (ArrayList<Hand> aPlayerHands) {
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
	
	public boolean dealAllCards (ArrayList<Hand> aPlayerHands) {
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

	public boolean dealXCards (ArrayList<Hand> aPlayerHands, int aDealCount) {
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

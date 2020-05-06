package cards.main;

import java.util.ArrayList;
import java.util.Collections;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public class Deck {
	ArrayList<Card> cards;
	public enum Types { STANDARD, STANDARD_JOKERS };
	
	public Deck () {
		buildEmptyDeck ();		
	}
	
	public Deck (Types aType) {
		Card tCard;
		
		buildEmptyDeck ();
		
		if (aType.equals (Types.STANDARD)) {
			fillStandardDeck ();
		} else if (aType.equals (Types.STANDARD_JOKERS)) {
			fillStandardDeck ();
			tCard = new Card (Ranks.JOKER, Suits.JOKER_RED);
			cards.add (tCard);
			tCard = new Card (Ranks.JOKER, Suits.JOKER_BLACK);
			cards.add (tCard);
		} else {
			System.err.println ("Not a know Deck Type");
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

	public void buildEmptyDeck () {
		cards = new ArrayList<Card> ();
	}
	
	public void shuffle () {
		Collections.shuffle (cards);
	}
	
	public void printCards () {
		int tIndex = 1;
		
		for (Card tCard : cards) {
			System.out.println (tIndex + ": " + tCard.getFullName ());
			tIndex++;
		}
	}
	
	public boolean duplicate (Deck aDuplicateDeck) {
		boolean tDuplicated;
		
		if (aDuplicateDeck.isEmpty ()) {
			for (Card tCard : cards) {
				aDuplicateDeck.add (tCard);
			}
			tDuplicated = true;
		} else {
			tDuplicated = false;
		}
		
		return tDuplicated;
	}
	
	// Add the entire Deck/Hand being passed in to the current Deck
	// Equivalent to a "MergeDecks" type of function.
	public void add (Deck aDeck) {
		Card tCard;
		
		while (! aDeck.isEmpty ()) {
			tCard = aDeck.pullTopCard ();
			add (tCard);
		}
	}
	
	public void add (Card aCard) {
		cards.add (aCard);
	}
	
	public Card getTopCard () {
		return get (0);
	}
	
	public Card get (int aIndex) {
		return cards.get (aIndex);
	}
	
	public void removeTopCard () {
		remove (0);
	}
	
	public void remove (int aIndex) {
		cards.remove (aIndex);
	}
	
	public boolean isEmpty () {
		return cards.isEmpty ();
	}
	
	public int getCount () {
		return cards.size ();
	}
	
	public Card pullTopCard () {
		Card tCard;
		
		tCard = getTopCard ();
		removeTopCard ();
		
		return tCard ;
	}
	
	public void dealACard (Deck aToDeck) {
		if (! isEmpty ()) {
			Card tThisCard;
			
			tThisCard = pullTopCard ();
			aToDeck.add (tThisCard);
		}
	}
	
	public void dealAllCards (ArrayList<Deck> aPlayerHands) {
		int tPlayerIndex, tPlayerCount;
		
		tPlayerCount = aPlayerHands.size ();
		tPlayerIndex = 0;
		while (! isEmpty ()) {
			dealACard (aPlayerHands.get (tPlayerIndex));
			tPlayerIndex = (tPlayerIndex + 1) % tPlayerCount;
		}
	}
	
	public void dealXCards (ArrayList<Deck> aPlayerHands, int aDealCount) {
		int tPlayerIndex, tPlayerCount, tCardCount;
		
		tPlayerCount = aPlayerHands.size ();
		tPlayerIndex = 0;
		tCardCount = 0;
		while (tCardCount < (tPlayerCount * aDealCount)) {
			dealACard (aPlayerHands.get (tPlayerIndex));
			tPlayerIndex = (tPlayerIndex + 1) % tPlayerCount;
			tCardCount++;
		}
	}
	
	public int getCountOfSuit (Suits aSuitToCount) {
		int tCountOfSuit = 0;
		
		if (! isEmpty()) {
			for (Card tCard : cards) {
				if (tCard.isSuit (aSuitToCount)) {
					tCountOfSuit++;
				}
			}
		}
		
		return tCountOfSuit;
	}
	
	public int getCountOfRank (Ranks aRankToCount) {
		int tCountOfRank = 0;
		
		if (! isEmpty()) {
			for (Card tCard : cards) {
				if (tCard.isRank (aRankToCount)) {
					tCountOfRank++;
				}
			}
		}
		
		return tCountOfRank;
	}
}

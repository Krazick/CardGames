package cards.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public class CardSet {
	protected ArrayList<Card> cards;

	public CardSet () {
		buildEmptyCardSet ();
	}

	public void buildEmptyCardSet () {
		cards = new ArrayList<Card> ();
	}
	
	public void printCards () {
		int tIndex = 1;
		
		for (Card tCard : cards) {
			System.out.println (tIndex + ": " + tCard.getFullName ());
			tIndex++;
		}
	}

	public void add (Card aCard) {
		cards.add (aCard);
	}

	public Card get (int aIndex) {
		return cards.get (aIndex);
	}

	public void remove (int aIndex) {
		cards.remove (aIndex);
	}

	public void removeAll () {
		cards.removeAll (cards);
	}
	
	public void remove (Card aCard) {
		int tCardIndex;
		Card tCard;
		boolean tCardRemoved = false;
		
		for (tCardIndex = 0; tCardIndex < cards.size () && !tCardRemoved; tCardIndex++) {
			tCard = get (tCardIndex);
			if (tCard == aCard) {
				remove (tCardIndex);
				tCardRemoved = true;
			}
		}
	}
	
	public boolean isEmpty () {
		return cards.isEmpty ();
	}

	public int getCount () {
		return cards.size ();
	}

	public Card pullFirstCard () {
		Card tCard;
		
		tCard = get (0);
		remove (0);
		
		return tCard ;
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
	
	public void sort () {
		Collections.sort (cards, new CardComparator ());
	}
	
	public class CardComparator implements Comparator<Card> {
		
	    @Override
	    public int compare (Card aCard1, Card aCard2) {
	    	int tOrder;
	    	
	    	if (aCard1.isSameSuit (aCard2)) {
	    		tOrder = aCard1.rankValueCompare (aCard2);
	    	} else {
	    		tOrder = aCard1.suitCompare (aCard2);
	    	}
	    	
	        return tOrder;
	    }
	}
	
	public Card getLowestInSuit (Suits aCardSuit) {
		Card tCard;
		
		tCard = Card.NO_CARD;
		for (Card tACard : cards) {
			if (tACard.isSuit (aCardSuit)) {
				if (tCard == Card.NO_CARD) {
					tCard = tACard;
				} else {
					if (tACard.getRankValue () < tCard.getRankValue ()) {
						tCard = tACard;
					}
				}
			}
		}
		
		return tCard;
	}

}

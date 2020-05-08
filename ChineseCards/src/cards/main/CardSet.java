package cards.main;

import java.util.ArrayList;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public abstract class CardSet {
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

}

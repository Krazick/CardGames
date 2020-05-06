package cards.main;

import java.util.ArrayList;
import java.util.Collections;

import cards.main.Card.Suits;

public class Deck {
	ArrayList<Card> cards;
	
	public Deck () {
		cards = new ArrayList<Card> ();		
	}
	
	public Deck (String aType) {
		int tRankIndex;
		Card tCard;
		
		cards = new ArrayList<Card> ();
		for (Suits tSuit : Card.Suits.values ()) {
			for (tRankIndex = Card.MIN_RANK_INDEX; tRankIndex <= Card.MAX_RANK_INDEX; tRankIndex++) {
				tCard = new Card (tRankIndex, tSuit);
				cards.add (tCard);
			}
		}
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
	
	public void add (Card aCard) {
		cards.add (aCard);
	}
	
	public Card get (int aIndex) {
		return cards.get (aIndex);
	}
	
	public void remove (int aIndex) {
		cards.remove (aIndex);
	}
	
	public int getCount () {
		return cards.size ();
	}
	
	public void dealACard (Deck aToDeck) {
		if (getCount () > 0) {
			Card tThisCard;
			
			tThisCard = get (0);
			remove (0);
			aToDeck.add (tThisCard);
		}
	}
}

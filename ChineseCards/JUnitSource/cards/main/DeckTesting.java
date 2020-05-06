package cards.main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName ("Testing Deck Class")
class DeckTesting {

	@Test
	@DisplayName ("Empty Deck Constructor")
	void EmptyDeckConstructorTest () {
		Deck tDeck;
		
		tDeck = new Deck ();
		assertEquals (0, tDeck.getCount ());
		assertTrue (tDeck.isEmpty ());
		assertEquals (0, tDeck.getCountOfSuit (Card.Suits.DIAMONDS));
		assertEquals (0, tDeck.getCountOfRank (Card.Ranks.FOUR));
	}

	@Test
	@DisplayName ("Standard Deck Constructor Test")
	void StandardDeckConstructorTest () {
		Deck tDeck;
		
		tDeck = new Deck (Deck.Types.STANDARD);
		assertEquals (52, tDeck.getCount ());
		assertFalse (tDeck.isEmpty ());
		assertEquals (13, tDeck.getCountOfSuit (Card.Suits.DIAMONDS));
		assertEquals (4, tDeck.getCountOfRank (Card.Ranks.FOUR));
		assertEquals (0, tDeck.getCountOfRank (Card.Ranks.JOKER));
	}
	
	@Test
	@DisplayName ("Standard Joker Deck Constructor Test")
	void StandardJokerDeckConstructorTest () {
		Deck tDeck;
		
		tDeck = new Deck (Deck.Types.STANDARD_JOKERS);
		assertEquals (54, tDeck.getCount ());
		assertFalse (tDeck.isEmpty ());
		assertEquals (13, tDeck.getCountOfSuit (Card.Suits.HEARTS));
		assertEquals (4, tDeck.getCountOfRank (Card.Ranks.SEVEN));
		assertEquals (2, tDeck.getCountOfRank (Card.Ranks.JOKER));
	}
	
	@Test
	@DisplayName ("Test Dealing Cards to 4 Players") 
	void DealingCardsTest () {
		Deck tMainDeck, tPlayerHand;
		ArrayList<Deck> tPlayerHands;
		int tPlayerIndex, tPlayerCount = 4;
		
		tMainDeck = new Deck (Deck.Types.STANDARD);
		tPlayerHands = new ArrayList<Deck> ();
		
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tPlayerHand = new Deck ();
			tPlayerHands.add (tPlayerHand);
		}
		
		assertEquals (52, tMainDeck.getCount ());
		assertTrue (tPlayerHands.get (0).isEmpty ());
		assertTrue (tPlayerHands.get (1).isEmpty ());
		assertTrue (tPlayerHands.get (2).isEmpty ());
		assertTrue (tPlayerHands.get (3).isEmpty ());
		
		tMainDeck.dealAllCards (tPlayerHands);		
		assertTrue (tMainDeck.isEmpty ());
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			assertEquals (13, tPlayerHands.get (tPlayerIndex).getCount ());
		}
		
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tMainDeck.add (tPlayerHands.get (tPlayerIndex));
			assertTrue (tPlayerHands.get (tPlayerIndex).isEmpty ());
			assertEquals (13 * (tPlayerIndex + 1), tMainDeck.getCount ());
		}
		
		tMainDeck.dealXCards (tPlayerHands, 7);
		assertEquals (24, tMainDeck.getCount ());
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			assertEquals (7, tPlayerHands.get (tPlayerIndex).getCount ());
		}
		
		for (tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tMainDeck.add (tPlayerHands.get (tPlayerIndex));
		}
	}
	
	@Test
	@DisplayName ("Test Duplicating and Merging Deck")
	void duplicateAndMergeDeckTess () {
		Deck tMainDeck;
		Deck tDuplicateDeck = new Deck ();
		
		tMainDeck = new Deck (Deck.Types.STANDARD);
		
		tMainDeck.duplicate (tDuplicateDeck);
		assertEquals (52, tDuplicateDeck.getCount ());
		tMainDeck.add (tDuplicateDeck);
		assertEquals (104, tMainDeck.getCount ());
	}
	
	
}

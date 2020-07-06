package cards.main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName ("Testing Deck Class")
class DeckTesting {
	Deck emptyDeck;
	Deck standardDeck;
	Deck standardJokerDeck;
	CardImages cardImages;
	
	@BeforeEach
	void setUp () throws Exception {
		emptyDeck = new Deck ();
		cardImages = new CardImages ();
		standardDeck = new Deck (Deck.Types.STANDARD, cardImages);
		standardJokerDeck = new Deck (Deck.Types.STANDARD_JOKERS, cardImages);
	}
	
	
	@Test
	@DisplayName ("Empty Deck Constructor")
	void EmptyDeckConstructorTest () {
		assertEquals (0, emptyDeck.getCount ());
		assertTrue (emptyDeck.isEmpty ());
		assertEquals (0, emptyDeck.getCountOfSuit (Card.Suits.DIAMONDS));
		assertEquals (0, emptyDeck.getCountOfRank (Card.Ranks.FOUR));
	}

	@Test
	@DisplayName ("Standard Deck Constructor Test")
	void StandardDeckConstructorTest () {
		assertEquals (52, standardDeck.getCount ());
		assertFalse (standardDeck.isEmpty ());
		assertEquals (13, standardDeck.getCountOfSuit (Card.Suits.DIAMONDS));
		assertEquals (4, standardDeck.getCountOfRank (Card.Ranks.FOUR));
		assertEquals (0, standardDeck.getCountOfRank (Card.Ranks.JOKER));
	}
	
	@Test
	@DisplayName ("Standard Joker Deck Constructor Test")
	void StandardJokerDeckConstructorTest () {
		assertEquals (54, standardJokerDeck.getCount ());
		assertFalse (standardJokerDeck.isEmpty ());
		assertEquals (13, standardJokerDeck.getCountOfSuit (Card.Suits.HEARTS));
		assertEquals (4, standardJokerDeck.getCountOfRank (Card.Ranks.SEVEN));
		assertEquals (2, standardJokerDeck.getCountOfRank (Card.Ranks.JOKER));
	}
	
	@Test
	@DisplayName ("Test Dealing Cards to 4 Players") 
	void DealingCardsTest () {
		Deck tMainDeck;
		PlayerHands tPlayerHands;
		int tPlayerIndex, tPlayerCount = 4;
		
		tMainDeck = standardDeck;
		tPlayerHands = buildPlayerHands (tPlayerCount);
		
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

	private PlayerHands buildPlayerHands (int aPlayerCount) {
		PlayerHands tPlayerHands;
		int tPlayerIndex;
		
		tPlayerHands = new PlayerHands ();
		
		for (tPlayerIndex = 0; tPlayerIndex < aPlayerCount; tPlayerIndex++) {
			tPlayerHands.add ("DeckTester_" + tPlayerIndex);
			assertEquals ("DeckTester_" + tPlayerIndex, tPlayerHands.getPlayerName (tPlayerIndex));
		}
		
		return tPlayerHands;
	}
	
	@Test
	@DisplayName ("Test Dealing when not enough Cards")
	void dealingWithTooFewCardsTest () {
		Deck tMainDeck;
		Hand tPlayerHand;
		boolean tGoodDeal, tDuplicateSuccess;
		PlayerHands tPlayerHands;
		int tPlayerCount = 4, tDealCount;
		
		tMainDeck = emptyDeck;
		tPlayerHand = new Hand ("DeckTester");
		tGoodDeal = tMainDeck.dealACard (tPlayerHand);
		assertFalse (tGoodDeal);
		
		standardDeck.duplicate (tMainDeck);
		tPlayerHands = buildPlayerHands (tPlayerCount);
		tDealCount = 10;
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, tDealCount);
		
		assertEquals (12, tMainDeck.getCount ());
		assertEquals (52, standardDeck.getCount ());
		assertTrue (tGoodDeal);
		
		tMainDeck = new Deck ();
		assertTrue (tMainDeck.isEmpty ());
		tDuplicateSuccess = standardDeck.duplicate (tMainDeck);
		assertTrue (tDuplicateSuccess);
		assertEquals (52, tMainDeck.getCount ());
		tPlayerHands = buildPlayerHands (tPlayerCount);
		tDealCount = 15;
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, tDealCount);
		assertFalse (tGoodDeal);
		assertEquals (52, tMainDeck.getCount ());
	}
	
	@Test
	@DisplayName ("Test Duplicating and Merging Deck")
	void duplicateAndMergeDeckTest () {
		Deck tMainDeck;
		Deck tDuplicateDeck = new Deck ();
		boolean tGoodMerge, tDuplicated;
		
		tMainDeck = standardDeck;
		
		tMainDeck.duplicate (tDuplicateDeck);
		assertEquals (52, tDuplicateDeck.getCount ());
		tMainDeck.add (tDuplicateDeck);
		assertEquals (104, tMainDeck.getCount ());
		
		tDuplicateDeck = null;
		tGoodMerge = tMainDeck.add (tDuplicateDeck);
		assertFalse (tGoodMerge);
		
		tDuplicated = tMainDeck.duplicate (tDuplicateDeck);
		assertFalse (tDuplicated);
		
	}
	
	@Test
	@DisplayName ("Test Duplicating non-empty Target") 
	void duplicateToNonEmptyDeckTest () {
		Deck tMainDeck;
		Deck tDuplicateDeck;
		boolean tDuplicateSuccess;
		
		tMainDeck = standardDeck;
		tDuplicateDeck = emptyDeck;
		tDuplicateSuccess = tMainDeck.duplicate (tDuplicateDeck);
		assertTrue (tDuplicateSuccess);
		
		tDuplicateDeck = emptyDeck;
		assertEquals (52, tMainDeck.getCount ());
		
		tMainDeck.dealACard (tDuplicateDeck);
		tDuplicateSuccess = tMainDeck.duplicate (tDuplicateDeck);
		assertFalse (tDuplicateSuccess);
	}
	
	@Test
	@DisplayName ("Test Duplicating an Empty Source")
	void duplicateFromEmptyDeckTest () {
		Deck tMainDeck;
		boolean tDuplicateSuccess;
		
		tMainDeck = new Deck ();
		tDuplicateSuccess = emptyDeck.duplicate (tMainDeck);
		assertTrue (tDuplicateSuccess);
		assertTrue (tMainDeck.isEmpty ());
		
		tMainDeck = new Deck (Deck.Types.STANDARD, cardImages);
		assertFalse (tMainDeck.isEmpty ());
		
		tDuplicateSuccess = emptyDeck.duplicate (tMainDeck);
		assertFalse (tDuplicateSuccess);
		assertFalse (tMainDeck.isEmpty ());
	}
	
	@Test
	@DisplayName ("Test Dealing from a -BAD- Deck")
	void dealFromBadDeckTest () {
		Deck tMainDeck;
		Hand tPlayerHand;
		boolean tGoodDeal;
		
		tMainDeck = standardDeck;
		tPlayerHand = null;
		tGoodDeal = tMainDeck.dealACard (tPlayerHand);
		assertFalse (tGoodDeal);
	}
	
	@Test
	@DisplayName ("Test with -BAD- PlayerHands")
	void dealToBadPlayerHandsTest () {
		Deck tMainDeck;
		boolean tGoodDeal;
		PlayerHands tPlayerHands;
		
		tPlayerHands = null;
		tMainDeck = standardDeck;
		tGoodDeal = tMainDeck.dealAllCards (tPlayerHands);
		assertFalse (tGoodDeal);
	
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, 3);
		assertFalse (tGoodDeal);
				
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, 5);
		assertFalse (tGoodDeal);

		tPlayerHands = buildPlayerHands (4);
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, -15);
		assertFalse (tGoodDeal);
		
		tPlayerHands.remove (0);
		tPlayerHands.remove (0);
		tPlayerHands.remove (0);
		tPlayerHands.remove (0);
		tGoodDeal = tMainDeck.dealXCards (tPlayerHands, -15);
		assertFalse (tGoodDeal);
	}
	
	@Test
	@DisplayName ("Test Unkown Deck Type construction")
	void unknownDeckTypeConstructionTest () {
		Deck tMainDeck;
		
		tMainDeck = new Deck (Deck.Types.UNKNOWN, cardImages);
		assertEquals (0, tMainDeck.getCount ());
	}
	
	@Test
	@DisplayName ("Test that Shuffle changes cards orders")
	void deckShuffleTest () {
		Deck tMainDeck;
		int tCountMatches;
		Long tShuffleSeed;
		Random tRandomGenerator;
		tMainDeck = new Deck (Deck.Types.STANDARD, cardImages);
		tCountMatches = countMatches (tMainDeck);
		assertEquals (52, tCountMatches);
		
		tRandomGenerator = new Random ();
		tShuffleSeed = tRandomGenerator.nextLong ();

		tMainDeck.shuffle (tShuffleSeed);
		tCountMatches = countMatches (tMainDeck);
		assertNotEquals (52, tCountMatches);
		System.out.println ("Shuffle results in only " + tCountMatches + " compared to Standard");
	}

	private int countMatches (Deck aMainDeck) {
		int tCountMatches;
		int tDeckIndex;
		Card tCardStandard;
		Card tCardMain;
		tCountMatches = 0;
		for (tDeckIndex = 0; tDeckIndex < 52; tDeckIndex++) {
			tCardStandard = standardDeck.get (tDeckIndex);
			tCardMain = aMainDeck.get (tDeckIndex);
			if (tCardStandard.getFullName ().equals (tCardMain.getFullName ())) {
				tCountMatches++;
			}
		}
		return tCountMatches;
	}
}

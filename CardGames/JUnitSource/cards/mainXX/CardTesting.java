package cards.mainXX;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cards.mainXX.Card.Ranks;
import cards.mainXX.Card.Suits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName ("Card Class Testing")
class CardTesting {

	@Test
	@DisplayName ("Verify Card Constructors")
	void test () {
		Card singleCard, anotherCard, thirdCard, fourthCard, fifthCard;
		CardImages tCardImages;
		
		tCardImages = new CardImages ();
		
		singleCard = new Card (Card.Ranks.ACE, "Hearts", tCardImages);
		assertEquals ("Ace of Hearts", singleCard.getFullName ());
		assertEquals ("AHearts", singleCard.getShortName ());
		assertEquals ("AH", singleCard.getAbbrev ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());
		
		singleCard = new Card (0, "Diamonds", tCardImages);
		assertEquals ("Ace of Diamonds", singleCard.getFullName ());
		assertEquals ("ADiamonds", singleCard.getShortName ());
		assertEquals ("AD", singleCard.getAbbrev ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());
		
		anotherCard = new Card (Card.Ranks.SEVEN, "Clubs", tCardImages);
		assertEquals ("7 of Clubs", anotherCard.getFullName ());
		assertEquals ("7Clubs", anotherCard.getShortName ());
		assertEquals ("7C", anotherCard.getAbbrev ());
		assertTrue (anotherCard.isBlack ());
		assertFalse (anotherCard.isRed ());
		
		thirdCard = new Card (2, "Diamonds", tCardImages);
		assertEquals ("3 of Diamonds", thirdCard.getFullName ());
		assertEquals ("3Diamonds", thirdCard.getShortName ());
		assertEquals ("3D", thirdCard.getAbbrev ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());
		
		anotherCard = new Card (Card.Ranks.KING, "Spades", tCardImages);
		assertEquals ("King of Spades", anotherCard.getFullName ());
		assertEquals ("KSpades", anotherCard.getShortName ());
		assertEquals ("KS", anotherCard.getAbbrev ());
		assertTrue (anotherCard.isBlack ());
		assertFalse (anotherCard.isRed ());
		
		anotherCard = new Card (Card.Ranks.QUEEN, Card.Suits.HEARTS, tCardImages);
		assertEquals ("Queen of Hearts", anotherCard.getFullName ());
		assertEquals ("QHearts", anotherCard.getShortName ());
		assertEquals ("QH", anotherCard.getAbbrev ());
		assertTrue (anotherCard.isRed ());
		assertFalse (anotherCard.isBlack ());

		thirdCard = new Card (6, Card.Suits.DIAMONDS, tCardImages);
		assertEquals ("7 of Diamonds", thirdCard.getFullName ());
		assertEquals ("7Diamonds", thirdCard.getShortName ());
		assertEquals ("7D", thirdCard.getAbbrev ());
		assertTrue (thirdCard.isRed ());
		assertFalse (thirdCard.isBlack ());

		fourthCard = new Card (9, "Spades", tCardImages);
		assertEquals ("10 of Spades", fourthCard.getFullName ());
		assertEquals ("10Spades", fourthCard.getShortName ());
		assertEquals ("10S", fourthCard.getAbbrev ());
		assertTrue (fourthCard.isBlack ());
		assertFalse (fourthCard.isRed ());

		fifthCard = new Card (Ranks.JOKER, Suits.JOKER_RED, tCardImages);
		assertEquals ("Joker of Red Joker", fifthCard.getFullName ());
		assertEquals ("JRed Joker", fifthCard.getShortName ());
		assertEquals ("JR", fifthCard.getAbbrev ());
		assertTrue (fifthCard.isRed ());
		assertFalse (fifthCard.isBlack ());
		
		fifthCard = new Card (Ranks.JOKER, Suits.JOKER_BLACK, tCardImages);
		assertEquals ("Joker of Black Joker", fifthCard.getFullName ());
		assertEquals ("JBlack Joker", fifthCard.getShortName ());
		assertEquals ("JB", fifthCard.getAbbrev ());
		assertTrue (fifthCard.isBlack ());
		assertFalse (fifthCard.isRed ());

		Assertions.assertThrows (IllegalArgumentException.class, () -> {
			new Card (0, "NOT ACE", tCardImages); });
		
		Assertions.assertThrows (IllegalArgumentException.class, () -> {
			new Card (53, "NOT GOOD", tCardImages); });

	}

}

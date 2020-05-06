package cards.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName ("Card Class Testing")
class CardTesting {

	@Test
	@DisplayName ("Verify Card Constructors")
	void test () {
		Card singleCard, anotherCard, thirdCard;
		
		singleCard = new Card (Card.Ranks.ACE, "Hearts");
		assertEquals ("Ace of Hearts", singleCard.getFullName ());
		assertEquals ("AHearts", singleCard.getShortName ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());
		
		anotherCard = new Card (Card.Ranks.SEVEN, "Clubs");
		assertEquals ("7 of Clubs", anotherCard.getFullName ());
		assertEquals ("7Clubs", anotherCard.getShortName ());
		assertTrue (anotherCard.isBlack ());
		assertFalse (anotherCard.isRed ());
		
		thirdCard = new Card (3, "Diamonds");
		assertEquals ("3 of Diamonds", thirdCard.getFullName ());
		assertEquals ("3Diamonds", thirdCard.getShortName ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());
		
		anotherCard = new Card (Card.Ranks.KING, "Spades");
		assertEquals ("King of Spades", anotherCard.getFullName ());
		assertEquals ("KSpades", anotherCard.getShortName ());
		assertTrue (anotherCard.isBlack ());
		assertFalse (anotherCard.isRed ());
		
		anotherCard = new Card (Card.Ranks.QUEEN, Card.Suits.HEARTS);
		assertEquals ("Queen of Hearts", anotherCard.getFullName ());
		assertEquals ("QHearts", anotherCard.getShortName ());
		assertTrue (anotherCard.isRed ());
		assertFalse (anotherCard.isBlack ());

		thirdCard = new Card (7, Card.Suits.DIAMONDS);
		assertEquals ("7 of Diamonds", thirdCard.getFullName ());
		assertEquals ("7Diamonds", thirdCard.getShortName ());
		assertTrue (singleCard.isRed ());
		assertFalse (singleCard.isBlack ());

		Assertions.assertThrows (IllegalArgumentException.class, () -> {
			new Card (0, "NOT ACE"); });
		
		Assertions.assertThrows (IllegalArgumentException.class, () -> {
			new Card (53, "NOT GOOD"); });

	}

}

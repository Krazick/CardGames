package cards.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cards.main.Card.Rank;

@DisplayName ("Card Class Testing")
class CardTesting {

	@Test
	@DisplayName ("Verify Card Constructors")
	void test () {
		Card singleCard, anotherCard, thirdCard;
		Card invalidCard;
		
		singleCard = new Card (Card.Rank.ACE, "Hearts");
		assertEquals ("Ace of Hearts", singleCard.getFullName ());
		assertEquals ("AHearts", singleCard.getShortName ());
		
		anotherCard = new Card (Card.Rank.SEVEN, "Clubs");
		assertEquals ("7 of Clubs", anotherCard.getFullName ());
		assertEquals ("7Clubs", anotherCard.getShortName ());
		
		thirdCard = new Card (3, "Diamonds");
		assertEquals ("3 of Diamonds", thirdCard.getFullName ());
		assertEquals ("3Diamonds", thirdCard.getShortName ());
		
		invalidCard = new Card (0, "NOT ACE");
		assertEquals (Rank.NO_CARD, invalidCard.getRank ());
		
		invalidCard = new Card (53, "NOT GOOD");
		assertEquals (Rank.NO_CARD, invalidCard.getRank ());
	}

}

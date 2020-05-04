package cards.main;

public class Card {
	public static int MIN_RANK_INDEX = 1;
	public static int MAX_RANK_INDEX = 13;
	public Card NO_CARD = null;
	
	public enum Suit { 
		NO_SUIT  ("INVALID",  "NO COLOR", "SYMBOL"),
		HEARTS   ("Hearts",   "Red",      "H"),
		DIAMONDS ("Diamonds", "Red",      "D"),
		CLUBS    ("Clubs",    "Black",    "C"),
		SPADES   ("Spades",   "Black",    "B");
		
		String name, color, symbol;
		Suit (String aName, String aColor, String aSymbol) {
			name = aName;
			color = aColor;
			symbol = aSymbol;
		}
		
		public String getName () {
			return name;
		}
		
		public String toString () {
			return name;
		}
		
		public String getColor () {
			return color;
		}
		
		public String getSymbol () {
			return symbol;
		}
	}
	
	public enum Rank { NO_CARD ("INVALID", 0),
		ACE  ("Ace", 1),   TWO ("2",   2),      THREE ("3", 3), FOUR  ("4", 4), 
		FIVE ("5",   5),   SIX ("6",   6),      SEVEN ("7", 7), EIGHT ("8", 8), 
		NINE ("9",   9),   TEN ("10", 10), 
		JACK ("Jack", 11), QUEEN ("Queen", 12), KING ("King", 13); 
		
		String name;
		int value;
		
		Rank (String aName, int aValue) {
			name = aName;
			value = aValue;
		}
		
		
		public String getName () {
			return name;
		}
		
		public String toString () {
			return name;
		}
		
		public int getValue () {
			return value;
		}
		
		public String getShortName () {
			return name.substring (0, 1);
		}
	}
	
	private Rank rank;
	private Suit suit;
	private boolean faceUp;
	public Rank [] allRanks = { 
			Rank.ACE,  Rank.TWO,   Rank.THREE,  Rank.FOUR, Rank.FIVE, 
			Rank.SIX,  Rank.SEVEN, Rank.EIGHT,  Rank.NINE, Rank.TEN, 
			Rank.JACK, Rank.QUEEN, Rank.KING };
	
	public Card (Rank aRank, String aSuit) {
		setRank (aRank);
		setSuit (aSuit);
		faceUp = true;
	}

	public Card (int aRank, String aSuit) {
		Rank tRank = getMatchingRank (aRank);
		setRank (tRank);
		setSuit (aSuit);
		faceUp = true;
	}
	
	public Suit getMatchingSuit (String aSuit) {
		Suit tSuit = Suit.NO_SUIT;
		
		if (Suit.CLUBS.getName ().equals (aSuit)) {
			tSuit = Suit.CLUBS;
		} else if (Suit.DIAMONDS.getName ().equals (aSuit)) {
			tSuit = Suit.DIAMONDS;
		} else if (Suit.HEARTS.getName ().equals (aSuit)) {
			tSuit = Suit.HEARTS;
		} else if (Suit.SPADES.getName ().equals (aSuit)) {
			tSuit = Suit.SPADES;
		}
		
		return tSuit;
	}
	
	public Rank getMatchingRank (int aRankIndex) {
		Rank tRank;
		
		if ((aRankIndex < MIN_RANK_INDEX) || (aRankIndex > MAX_RANK_INDEX)) {
			tRank = Rank.NO_CARD;
		} else {
			tRank = allRanks [aRankIndex - 1];
		}
		
		return tRank;
	}

	private void setRank (Rank aRank) {
		rank = aRank;
	}
	
	private void setSuit (Suit aSuit) {
		suit = aSuit;
	}
	
	private void setSuit (String aSuit) {
		setSuit (getMatchingSuit (aSuit));
	}
	
	public Rank getRank () {
		return rank;
	}
	
	public String getSuit () {
		return suit.getName ();
	}
	
	public boolean isFaceUp () {
		return faceUp;
	}
	
	public String getFullName () {
		return rank.getName () + " of " + suit;
	}
	
	public String getShortName () {
		return rank.getShortName () + suit;
	}
}

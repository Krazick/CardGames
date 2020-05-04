package cards.main;

public class Card {
	public static int MIN_RANK = 1;
	public static int MAX_RANK = 13;
	public Card NO_CARD = null;
	
	public enum Rank { NO_CARD ("INVALID", 0),
		ACE ("Ace", 1), TWO ("2", 2), THREE ("3", 3), FOUR ("4", 4), 
		FIVE ("5", 5), SIX ("6", 6), SEVEN ("7", 7), EIGHT ("8", 8), 
		NINE ("9", 9), TEN ("10", 10), JACK ("Jack", 11), 
		QUEEN ("Queen", 12), KING ("King", 13); 
		
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
	private String suit;
	private boolean faceUp;
	public Rank [] allRanks = {Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, 
			Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, 
			Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING};
	
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
	
	public Rank getMatchingRank (int aRank) {
		Rank tRank;
		
		if ((aRank < MIN_RANK) || (aRank > MAX_RANK)) {
			tRank = Rank.NO_CARD;
		} else {
			tRank = allRanks [aRank - 1];
		}
		
		return tRank;
	}

	private void setRank (Rank aRank) {
		rank = aRank;
	}
	
	private void setSuit (String aSuit) {
		suit = aSuit;
	}
	
	public Rank getRank () {
		return rank;
	}
	
	public String getSuit () {
		return suit;
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

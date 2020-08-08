package cards.main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card {
	public static int MIN_RANK_INDEX = 0;
	public static int MAX_RANK_INDEX = 12;
	public static Card NO_CARD = null;
	public static String RED_CARD = "Red";
	public static String BLACK_CARD = "Black";
	
	private int cardOverlapUp = 20;
	private int cardOverlapDown = 5;
	private int cardPopUp = 10;
	
	public int getCardOverlapDown () {
		return cardOverlapDown;
	}
	
	public int getCardOverlapUp () {
		return cardOverlapUp;
	}
	
	public int getCardPopup () {
		return cardPopUp;
	}
	
	public enum Suits { 
		HEARTS   ("Hearts",   RED_CARD,      "H", 4),
		DIAMONDS ("Diamonds", RED_CARD,      "D", 2),
		CLUBS    ("Clubs",    BLACK_CARD,    "C", 1),
		SPADES   ("Spades",   BLACK_CARD,    "S", 3),
		JOKER_RED   ("Red Joker",   RED_CARD,   "RJ", 5),
		JOKER_BLACK ("Black Joker", BLACK_CARD, "BJ", 6);
		
		String name, color, symbol;
		int order;
		
		Suits (String aName, String aColor, String aSymbol, int aOrder) {
			name = aName;
			color = aColor;
			symbol = aSymbol;
			order = aOrder;
		}
		
		public String getName () {
			return name;
		}
		
		public int getOrder () {
			return order;
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
		
		public String getAbbrev () {
			return symbol;
		}
	}
	
	public enum Ranks { 
		ACE  ("Ace", 14),   TWO ("2",   2),      THREE ("3", 3), FOUR  ("4", 4), 
		FIVE ("5",   5),   SIX ("6",   6),      SEVEN ("7", 7), EIGHT ("8", 8), 
		NINE ("9",   9),   TEN ("10", 10), 
		JACK ("Jack", 11), QUEEN ("Queen", 12), KING ("King", 13), JOKER ("Joker", 0); 
		
		String name;
		int value;
		
		Ranks (String aName, int aValue) {
			name = aName;
			setValue (aValue);
		}
		
		public void setValue (int aValue) {
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
			String tShortName;
			
			if (value == 10) {
				tShortName = name;
			} else {
				tShortName = name.substring (0, 1);
			}
			
			return tShortName;
		}
		
		public String getAbbrev () {
			return getShortName ();
		}
	}
	
	private int points;
	private Ranks rank;
	private Suits suit;
	private boolean faceUp;
	ImageIcon image;
	ImageIcon backImage = null;
	ImageIcon blankImage = null;
	JLabel cardLabel;
	
	public Ranks [] allRanks = { 
			Ranks.ACE,  Ranks.TWO,   Ranks.THREE,  Ranks.FOUR, Ranks.FIVE, 
			Ranks.SIX,  Ranks.SEVEN, Ranks.EIGHT,  Ranks.NINE, Ranks.TEN, 
			Ranks.JACK, Ranks.QUEEN, Ranks.KING };
	
	public Card (Ranks aRank, String aSuit, CardImages aCardImages) {
		setupBasicCard (aRank, getMatchingSuit (aSuit), aCardImages);
	}

	public Card (int aRank, String aSuit, CardImages aCardImages) {
		setupBasicCard (getMatchingRank (aRank), getMatchingSuit (aSuit), aCardImages);
	}
	
	public Card (int aRank, Suits aSuit, CardImages aCardImages) {
		setupBasicCard (getMatchingRank (aRank), aSuit, aCardImages);
	}
	
	public Card (Ranks aRank, Suits aSuit, CardImages aCardImages) {
		setupBasicCard (aRank, aSuit, aCardImages);
	}

	public Card (Ranks aRank, Suits aSuit) {
		setupBasicCard (aRank, aSuit);
	}
	
	private void setupBasicCard (Ranks aRank, Suits aSuit, CardImages aCardImages) {
		setupBasicCard (aRank, aSuit);
		setImages (aCardImages);
	}

	private void setupBasicCard (Ranks aRank, Suits aSuit) {
		setRank (aRank);
		setSuit (aSuit);
		setFaceUp (true);
		setPoints ();
	}
	
	public void setPoints () {
		setPoints (0);
		if (suit.equals (Suits.HEARTS)) {
			setPoints (1);
		} else {
			if (suit.equals (Suits.SPADES)) {
				if (rank.equals (Ranks.QUEEN)) {
					setPoints (13);
				}
			}
		}
	}
	
	public void setPoints (int aPoints) {
		points = aPoints;
	}
	
	public int getPoints () {
		return points;
	}
	
	public void setFaceUp (boolean aFaceUp) {
		JLabel tCardLabel;

		faceUp = aFaceUp;
		
		tCardLabel = getCardLabel ();
		if (tCardLabel != null) {
			if (faceUp) {
				tCardLabel.setIcon (image);
			} else {
				tCardLabel.setIcon (backImage);
			}
		}
	}
	
	public boolean getFaceUp () {
		return faceUp;
	}
	
	public Suits getMatchingSuit (String aSuit) {
		Suits tSuit;
		
		if (Suits.CLUBS.getName ().equals (aSuit)) {
			tSuit = Suits.CLUBS;
		} else if (Suits.DIAMONDS.getName ().equals (aSuit)) {
			tSuit = Suits.DIAMONDS;
		} else if (Suits.HEARTS.getName ().equals (aSuit)) {
			tSuit = Suits.HEARTS;
		} else if (Suits.SPADES.getName ().equals (aSuit)) {
			tSuit = Suits.SPADES;
		} else {
			throw new IllegalArgumentException ("Invalid Suit");
		}
		
		return tSuit;
	}
	
	public Ranks getMatchingRank (int aRankIndex) {
		Ranks tRank;
		
		if ((aRankIndex < MIN_RANK_INDEX) || (aRankIndex > MAX_RANK_INDEX)) {
			throw new IllegalArgumentException ("Invalid Rank");
		} else {
			tRank = allRanks [aRankIndex];
		}
		
		return tRank;
	}

	private void setRank (Ranks aRank) {
		rank = aRank;
	}
	
	private void setSuit (Suits aSuit) {
		suit = aSuit;
	}
	
	public Ranks getRank () {
		return rank;
	}
	
	public int getRankValue () {
		return rank.getValue ();
	}
	
	public Suits getTheSuit () {
		return suit;
	}
	
	public String getSuit () {
		return suit.getName ();
	}
	
	public boolean isFaceUp () {
		return faceUp;
	}
	
	public boolean isRank (Ranks aRank) {
		return rank.equals (aRank);
	}
	
	public boolean isSameSuit (Card aCard) {
		return getSuit ().equals (aCard.getSuit ());
	}
	
	public boolean isSuit (Suits aSuit) {
		return suit.equals (aSuit);
	}
	
	public boolean isRed () {
		return (RED_CARD.equals (suit.getColor ()));
	}
	
	public boolean isBlack () {
		return (BLACK_CARD.equals (suit.getColor ()));
	}
	
	public String getFullName () {
		return rank.getName () + " of " + suit;
	}
	
	public String getShortName () {
		return rank.getShortName () + suit;
	}
	
	public int getOrder () {
		return suit.getOrder ();
	}
	
	public String getAbbrev () {
		return rank.getShortName () + suit.toString ().substring (0, 1);
	}
	
	public ImageIcon getImage () throws Exception {
		if (image != null) {
			return image;
		} else {
			throw (new Exception ("Missing Image"));
		}
	}
	
	public void setCardLabel (JLabel aCardLabel) {
		cardLabel = aCardLabel;
	}
	
	public JLabel getCardLabel () {
		return cardLabel;
	}
	
	private void setImages (CardImages aCardImages) {
		setImage (aCardImages);
		setBackImage (aCardImages);
		setBlankImage (aCardImages);
	}
	
	private void setImage (CardImages aCardImages) {
		CardImage tCardImage;
		
		tCardImage = aCardImages.getCardImage (getAbbrev ());
		image = tCardImage.getImage ();
		setCardLabel (tCardImage.getCardLabel ());
	}
	
	private void setBackImage (CardImages aCardImages) {
		CardImage tCardImage;
		
		tCardImage = aCardImages.getCardImage (CardImage.CARD_BACK);
		backImage = tCardImage.getImage ();
	}
	
	private void setBlankImage (CardImages aCardImages) {
		CardImage tCardImage;
		
		tCardImage = aCardImages.getCardImage (CardImage.BLANK_CARD);
		blankImage = tCardImage.getImage ();
	}
	
	public int suitCompare (Card aCard) {
		int tOrder, tThisOrder, tOtherOrder;
		
		tOrder =  - (getSuit ().compareTo (aCard.getSuit ()));
		
		tThisOrder = getOrder ();
		tOtherOrder = aCard.getOrder ();
		if (tThisOrder == tOtherOrder) {
			tOrder = 0;
		} else if (tThisOrder > tOtherOrder) {
			tOrder = -1;
		} else {
			tOrder = 1;
		}
		
		return tOrder;
	}
	
	public int rankValueCompare (Card aCard) {
		int tRankOrder;
		int tOtherRankValue = aCard.getRankValue ();
		int tThisRankValue = getRankValue ();
		
		if (tThisRankValue == tOtherRankValue) {
			tRankOrder = 0;
		} else if (tThisRankValue > tOtherRankValue) {
			tRankOrder = -1;
		} else {
			tRankOrder = 1;
		}
		
		return tRankOrder;
	}

	public ImageIcon getIconImage() {
		return (ImageIcon) cardLabel.getIcon ();
	}
}

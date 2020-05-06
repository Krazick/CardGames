package cards.main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Card {
	public static int MIN_RANK_INDEX = 1;
	public static int MAX_RANK_INDEX = 13;
	public Card NO_CARD = null;
	
	public enum Suits { 
		HEARTS   ("Hearts",   "Red",      "H"),
		DIAMONDS ("Diamonds", "Red",      "D"),
		CLUBS    ("Clubs",    "Black",    "C"),
		SPADES   ("Spades",   "Black",    "B");
		
		String name, color, symbol;
		Suits (String aName, String aColor, String aSymbol) {
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
	
	public enum Ranks { 
		ACE  ("Ace", 1),   TWO ("2",   2),      THREE ("3", 3), FOUR  ("4", 4), 
		FIVE ("5",   5),   SIX ("6",   6),      SEVEN ("7", 7), EIGHT ("8", 8), 
		NINE ("9",   9),   TEN ("10", 10), 
		JACK ("Jack", 11), QUEEN ("Queen", 12), KING ("King", 13); 
		
		String name;
		int value;
		
		Ranks (String aName, int aValue) {
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
			String tShortName;
			
			if (value == 10) {
				tShortName = name;
			} else {
				tShortName = name.substring (0, 1);
			}
			
			return tShortName;
		}
	}
	
	private Ranks rank;
	private Suits suit;
	private boolean faceUp;
	ImageIcon image;
	public Ranks [] allRanks = { 
			Ranks.ACE,  Ranks.TWO,   Ranks.THREE,  Ranks.FOUR, Ranks.FIVE, 
			Ranks.SIX,  Ranks.SEVEN, Ranks.EIGHT,  Ranks.NINE, Ranks.TEN, 
			Ranks.JACK, Ranks.QUEEN, Ranks.KING };
	
	public Card (Ranks aRank, String aSuit) {
		setRank (aRank);
		setSuit (aSuit);
		faceUp = true;
		setImage ();
	}

	public Card (int aRank, String aSuit) {
		setRank (getMatchingRank (aRank));
		setSuit (aSuit);
		faceUp = true;
		setImage ();
	}
	
	public Card (int aRank, Suits aSuit) {
		setRank (getMatchingRank (aRank));
		setSuit (aSuit);
		faceUp = true;
		setImage ();
	}
	
	public Card (Ranks aRank, Suits aSuit) {
		setRank (aRank);
		setSuit (aSuit);
		faceUp = true;
		setImage ();
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
			tRank = allRanks [aRankIndex - 1];
		}
		
		return tRank;
	}

	private void setRank (Ranks aRank) {
		rank = aRank;
	}
	
	private void setSuit (Suits aSuit) {
		suit = aSuit;
	}
	
	private void setSuit (String aSuit) {
		setSuit (getMatchingSuit (aSuit));
	}
	
	public Ranks getRank () {
		return rank;
	}
	
	public String getSuit () {
		return suit.getName ();
	}
	
	public boolean isFaceUp () {
		return faceUp;
	}
	
	public boolean isRed () {
		return ("Red".equals (suit.getColor ()));
	}
	
	public boolean isBlack () {
		return ("Black".equals (suit.getColor ()));
	}
	
	public String getFullName () {
		return rank.getName () + " of " + suit;
	}
	
	public String getShortName () {
		return rank.getShortName () + suit;
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
	
	private void setImage () {
		
		try {
			image = loadAndScaleImage ();
		} catch (Exception tException) {
			System.err.println ("Missing Image File " + getFullName ());
		}
	}
	
	private ImageIcon loadAndScaleImage () {
		ImageIcon tImage;
		Image tScaledImage;
		
		tImage = new ImageIcon ("Images/" + getAbbrev () + ".jpg");
		System.out.println ("Found and Loaded Image for " + getFullName ());
		tScaledImage = getScaledImage (tImage.getImage (), 110, 170);
		tImage.setImage (tScaledImage);
		
		return tImage;
	}
	
	private Image getScaledImage (Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
}

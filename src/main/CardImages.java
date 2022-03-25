package cards.main;

import java.util.ArrayList;

import javax.swing.JLabel;

import cards.main.Card.Ranks;
import cards.main.Card.Suits;

public class CardImages {
	ArrayList<CardImage> cardImages;
	
	CardImages ()  {
		ArrayList<String> tStandardNames;
		CardImage tACardImage;
		
		tStandardNames = getStandardNames ();
		
		cardImages = new ArrayList<CardImage> ();
		
		for (String tCardName : tStandardNames) {
			tACardImage = new CardImage (tCardName);
			cardImages.add (tACardImage);
		}
	}
	
	public ArrayList<String> getStandardNames () {
		ArrayList <String> tStandardNames;
		int tRankIndex;
		String tSuitAbbrev, tRankAbbrev;
		Ranks tRanks [] = new Ranks [] {
			Card.Ranks.ACE, Card.Ranks.TWO, Card.Ranks.THREE, Card.Ranks.FOUR, Card.Ranks.FIVE,
			Card.Ranks.SIX, Card.Ranks.SEVEN, Card.Ranks.EIGHT, Card.Ranks.NINE, Card.Ranks.TEN, 
			Card.Ranks.JACK, Card.Ranks.QUEEN, Card.Ranks.KING
		};
				
		tStandardNames = new ArrayList<String> ();
		for (Suits tSuit : Card.Suits.values ()) {
			tSuitAbbrev = tSuit.getAbbrev ();
			if (tSuitAbbrev.length () == 1) {
				for (tRankIndex = Card.MIN_RANK_INDEX; tRankIndex <= Card.MAX_RANK_INDEX; tRankIndex++) {
					tRankAbbrev = tRanks [tRankIndex].getAbbrev ();
					tStandardNames.add (tRankAbbrev + tSuitAbbrev);
				}
			}
		}
		tStandardNames.add ("JR");
		tStandardNames.add ("JB");
		tStandardNames.add (CardImage.CARD_BACK);
		tStandardNames.add (CardImage.BLANK_CARD);
		
		return tStandardNames;
	}

	public CardImage getCardImage (String aCardName) {
		CardImage tFoundCardImage = CardImage.NO_CARD_IMAGE;
		
		for (CardImage tCardImage : cardImages) {
			if (tCardImage.getName ().equals (aCardName)) {
				tFoundCardImage = tCardImage;
			}
		}
		
		return tFoundCardImage;
	}
	
	public JLabel getCardLabel (String aCardName) {
		JLabel tFoundCardLabel = null;
		CardImage tFoundCardImage;
		
		tFoundCardImage = getCardImage (aCardName);
		if (tFoundCardImage != CardImage.NO_CARD_IMAGE) {
			tFoundCardLabel = tFoundCardImage.getCardLabel();
		}
		
		return tFoundCardLabel;
	}
}

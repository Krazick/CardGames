package cards.main;

import java.util.ArrayList;

public class Trick extends CardSet {
	ArrayList<Player> whoPlayed;
	
	public Trick () {
		buildEmptyTrick ();
	}
	
	public void buildEmptyTrick () {
		whoPlayed = new ArrayList<Player> ();
	}

	public void add (Card aCard, Player aWhoPlayed) {
		add (aCard);
		whoPlayed.add (aWhoPlayed);
	}
	
	public String getTrickInfo () {
		String tTrickInfo = "";
		int tTrickIndex;
		Player tPlayer;
		Card tCard;
		
		for (tTrickIndex = 0; tTrickIndex < whoPlayed.size (); tTrickIndex++) {
			tPlayer = whoPlayed.get (tTrickIndex);
			tCard = get (tTrickIndex);
			tTrickInfo += tPlayer.getName () + " played " + tCard.getAbbrev () + "<br>";
//			tTrickInfo += TPlayer.getName () + " played " + tCard.getFullName () + "<br>";
		}
		
		return tTrickInfo;
	}
	
	public void removeAll () {
		super.removeAll ();
		whoPlayed.removeAll (whoPlayed);
	}
}

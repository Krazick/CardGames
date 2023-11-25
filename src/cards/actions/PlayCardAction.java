package cards.actions;

import cards.effects.Effect;
import cards.effects.PlayCardEffect;
import cards.main.Card;
import cards.main.GameManager;
import geUtilities.XMLNode;

public class PlayCardAction extends Action {
	public final static String NAME = "Play Card";

	public PlayCardAction () {
		this (NAME);
	}

	public PlayCardAction (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PlayCardAction (ActorI aActor) {
		this (NAME, aActor);
	}

	public PlayCardAction (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public PlayCardAction (XMLNode aActionNode, GameManager aGameManager) {
		super (aActionNode, aGameManager);
	}
	
	public void addPlayCardEffect (ActorI aFromPlayer, Card aCard) {
		PlayCardEffect tPlayCardEffect;

		tPlayCardEffect = new PlayCardEffect (aFromPlayer, aCard);
		addEffect (tPlayCardEffect);
	}
	
	private String getPlayedCardName () {
		String tCardName = "CARD";
		PlayCardEffect tPlayedCardEffect;
		
		for (Effect tEffect : effects) {
			if (tEffect instanceof PlayCardEffect) {
				tPlayedCardEffect = (PlayCardEffect) tEffect;
				tCardName = tPlayedCardEffect.getFullCardName ();
			}
		}
		
		return tCardName;
	}
	
	@Override
	public String getSimpleActionReport () {
		String tSimpleActionReport = "";
		String tCardName;
		
		tCardName = getPlayedCardName ();
		
		tSimpleActionReport = actor.getName () + " Played the Card " + tCardName;
		
		return tSimpleActionReport;
	}
}

package cards.actions;

import cards.effects.PassACardEffect;
import cards.effects.PassedCardsEffect;
import cards.main.Card;
import cards.main.GameManager;
import cards.utilities.XMLNode;

public class PassCardsAction extends Action {
	public final static String NAME = "Pass Cards";

	public PassCardsAction () {
		this (NAME);
	}

	public PassCardsAction (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PassCardsAction (ActorI aActor) {
		this (NAME, aActor);
	}

	public PassCardsAction (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public PassCardsAction (XMLNode aActionNode, GameManager aGameManager) {
		super (aActionNode, aGameManager);
	}
	
	public void addPassACardEffect (ActorI aFromPlayer, ActorI aToPlayer, Card aCard) {
		PassACardEffect tPassACardEffect;

		tPassACardEffect = new PassACardEffect (aFromPlayer, aToPlayer, aCard);
		addEffect (tPassACardEffect);
	}
	
	public void addPassedCardsEffect (ActorI aFromPlayer, ActorI aToPlayer) {
		PassedCardsEffect tPassedCardsEffect;

		tPassedCardsEffect = new PassedCardsEffect (aFromPlayer, aToPlayer);
		addEffect (tPassedCardsEffect);
	}
	
	@Override
	public String getSimpleActionReport () {
		String tSimpleActionReport = "";
		String tFromPlayerName, tToPlayerName, tCardName;
		
		tFromPlayerName = "FROM";
		tToPlayerName = "TO";
		tCardName = "CARD";
		
		tSimpleActionReport = actor.getName () + " Pass the Card " + tCardName +
				" from " + tFromPlayerName + " to " + tToPlayerName;
		
		return tSimpleActionReport;
	}
}

package cards.actions;

import cards.effects.Effect;
import cards.effects.PassTheCardEffect;
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
		PassTheCardEffect tPassACardEffect;

		tPassACardEffect = new PassTheCardEffect (aFromPlayer, aToPlayer, aCard);
		addEffect (tPassACardEffect);
	}
	
	public void addPassedCardsEffect (ActorI aFromPlayer, ActorI aToPlayer) {
		PassedCardsEffect tPassedCardsEffect;

		tPassedCardsEffect = new PassedCardsEffect (aFromPlayer, aToPlayer);
		addEffect (tPassedCardsEffect);
	}
	
	private String getPassedCardTo () {
		String tToPlayerName = "TO";
		PassedCardsEffect tPassedCardsEffect;
		
		for (Effect tEffect : effects) {
			if (tEffect instanceof PassedCardsEffect) {
				tPassedCardsEffect = (PassedCardsEffect) tEffect;
				tToPlayerName = tPassedCardsEffect.getToActorName ();
			}
		}
		
		return tToPlayerName;
	}

	@Override
	public String getSimpleActionReport () {
		String tSimpleActionReport = "";
		String tToPlayerName, tCardName;
		
		tToPlayerName = getPassedCardTo ();
		tCardName = "3 Cards";
		
		tSimpleActionReport = actor.getName () + " Passed " + tCardName +
				 " to " + tToPlayerName;
		
		return tSimpleActionReport;
	}
}

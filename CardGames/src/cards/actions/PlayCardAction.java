package cards.actions;

import cards.effects.PlayCardEffect;
import cards.main.Card;
import cards.main.GameManager;
import cards.utilities.XMLNode;

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
	
	@Override
	public String getSimpleActionReport () {
		String tSimpleActionReport = "";
		String tFromPlayerName, tCardName;
		
		tFromPlayerName = "FROM";
		tCardName = "CARD";
		
		tSimpleActionReport = actor.getName () + " Play the Card " + tCardName +
				" from " + tFromPlayerName;
		
		return tSimpleActionReport;
	}
}

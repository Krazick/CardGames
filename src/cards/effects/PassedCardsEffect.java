package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.main.Player;
import geUtilities.AttributeName;
import geUtilities.XMLDocument;
import geUtilities.XMLElement;
import geUtilities.XMLNode;

public class PassedCardsEffect extends ToActorEffect {
	public final static String NAME = "Passed Cards";
	
	public PassedCardsEffect () {
		this (NAME);
	}

	public PassedCardsEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PassedCardsEffect (String aName, ActorI aFromActor) {
		this (aName, aFromActor, ActorI.NO_ACTOR);
	}

	public PassedCardsEffect (ActorI aFromActor, ActorI aToActor) {
		this (NAME, aFromActor, aToActor);
	}
	
	public PassedCardsEffect (String aName, ActorI aFromActor, ActorI aToActor) {
		super (aName, aFromActor, aToActor);
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);

		return tEffectElement;
	}

	public PassedCardsEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
	}
	
	@Override
	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied = false;
		Player tFromPlayer, tToPlayer;
		
		tFromPlayer = getFromPlayer ();
		tToPlayer = getToPlayer ();
		tFromPlayer.passedCards (tToPlayer);
		tEffectApplied = true;

		return tEffectApplied;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		String tFromActorName, tToActorName;
		
		tFromActorName = getActorName ();
		tToActorName = getToActorName ();
		return (REPORT_PREFIX + name + " from " + tFromActorName + " to " + tToActorName + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

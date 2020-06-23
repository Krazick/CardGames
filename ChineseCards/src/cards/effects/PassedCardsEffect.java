package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.main.Player;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class PassedCardsEffect extends Effect {
	public final static String NAME = "Passed Cards";
	ActorI toActor;
	
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
		super (aName, aFromActor);
		setToActor (aToActor);
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
		tEffectElement.setAttribute (ActorI.AN_TO_ACTOR_NAME, toActor.getName ());

		return tEffectElement;
	}

	public PassedCardsEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);

		ActorI tToActor;
		String tToActorName;
		
		tToActorName = aEffectNode.getThisAttribute (ActorI.AN_TO_ACTOR_NAME);
		tToActor = aGameManager.getActor (tToActorName);
		setToActor (tToActor);		
	}

	public void setToActor (ActorI aToActor) {
		toActor = aToActor;
	}
	
	public Player getFromPlayer () {
		return (Player) actor;
	}
	
	public Player getToPlayer () {
		return (Player) toActor;
	}
	
	public ActorI getToActor () {
		return toActor;
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
		
		tFromActorName = actor.getName ();
		tToActorName = toActor.getName ();
		return (REPORT_PREFIX + name + " from " + tFromActorName + " to " + tToActorName + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

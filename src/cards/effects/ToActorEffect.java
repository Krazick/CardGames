package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.main.Player;
import geUtilities.xml.AttributeName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

public class ToActorEffect extends Effect {
	public final static String NAME = "To Actor";
	ActorI toActor;
	
	public ToActorEffect () {
		this (NAME);
	}

	public ToActorEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public ToActorEffect (String aName, ActorI aFromActor) {
		this (aName, aFromActor, ActorI.NO_ACTOR);
	}

	public ToActorEffect (ActorI aFromActor, ActorI aToActor) {
		this (NAME, aFromActor, aToActor);
	}
	
	public ToActorEffect (String aName, ActorI aFromActor, ActorI aToActor) {
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

	public ToActorEffect (XMLNode aEffectNode, GameManager aGameManager) {
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
	
	public Player getToPlayer () {
		return (Player) toActor;
	}
	
	public ActorI getToActor () {
		return toActor;
	}
	
	public String getToActorName () {
		return toActor.getName ();
	}
	
	@Override
	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied = false;
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

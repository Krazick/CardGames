package cards.effects;

import cards.main.GameManager;
import cards.main.Player;
import cards.actions.ActorI;
import geUtilities.AttributeName;
import geUtilities.ElementName;
import geUtilities.XMLDocument;
import geUtilities.XMLElement;
import geUtilities.XMLNode;

public class Effect {
	public final static String NO_NAME = ">>NO EFFECT NAME<<";
	public final static ActorI NO_ACTOR = null;
	public static final ElementName EN_EFFECTS = new ElementName ("Effects");
	public static final ElementName EN_EFFECT = new ElementName ("Effect");
	public final static String REPORT_PREFIX = "--" + EN_EFFECT + ": ";
	public final static AttributeName AN_CLASS = new AttributeName ("class");
	static final AttributeName AN_NAME = new AttributeName ("name");

	String name;
	ActorI actor;

	Effect () {
		this (NO_NAME);
	}

	Effect (String aName) {
		this (aName, NO_ACTOR);
	}
	
	Effect (String aName, ActorI aActor) {
		setName (aName);
		setActor (aActor);
	}
	
	Effect (XMLNode aEffectNode, GameManager aGameManager) {
		String tEffectName, tActorName;
		ActorI tActor;
		
		tEffectName = aEffectNode.getThisAttribute (AN_NAME);
		tActorName = aEffectNode.getThisAttribute (ActorI.AN_ACTOR_NAME);
		if (tActorName == null) {
			tActorName = aEffectNode.getThisAttribute (ActorI.AN_FROM_ACTOR_NAME);
		}
		
		tActor = aGameManager.getActor (tActorName);
		if (tActor == null) {
			System.err.println ("No Actor Found -- Looking for [" + tActorName + "]");
		}
		setName (tEffectName);
		setActor (tActor);
	}
	
	public boolean actorIsSet () {
		boolean tActorSet;
		
		tActorSet = false;
		if (actor != null) {
			tActorSet = true;
		}
		
		return tActorSet;
	}
	
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		String tActorName;
		
		tEffectElement = aXMLDocument.createElement (EN_EFFECT);
		tEffectElement.setAttribute (AN_CLASS, this.getClass ().getName ());
		tActorName = actor.getName ();
		tEffectElement.setAttribute (AN_NAME, getName ());
		tEffectElement.setAttribute (aActorAN, tActorName);
	
		return tEffectElement;
	}
	
	public String getName () {
		return name;
	}
	
	public ActorI getActor () {
		return actor;
	}
	
	public String getEffectReport (GameManager aGameManager) {
		return (REPORT_PREFIX + name + " for " + getActorName () + ".");
	}
	
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
	
	public void setName (String aName) {
		name = aName;
	}
	
	public void setActor (ActorI aActor) {
		actor = aActor;
	}

	public String getActorName () {
		return actor.getName ();
	}
	
	public boolean undoEffect (GameManager aGameManager) {
		return false;
	}
	
	public boolean wasNewStateAuction () {
		return false;
	}

	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied;
		
		tEffectApplied = false;
		
		return tEffectApplied;
	}

	public Player getFromPlayer () {
		return (Player) actor;
	}
}

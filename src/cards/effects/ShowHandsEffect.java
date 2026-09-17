package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import geUtilities.xml.AttributeName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

public class ShowHandsEffect extends Effect {
	public static final String NAME = "Show Hands";

	public ShowHandsEffect (ActorI aActor) {
		this (NAME, aActor);
	}
	
	public ShowHandsEffect (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public ShowHandsEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
	
		return tEffectElement;
	}

	@Override
	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied;
		
		tEffectApplied = false;
		aGameManager.showHands ();
		tEffectApplied = true;
		
		return tEffectApplied;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		return (REPORT_PREFIX + name + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

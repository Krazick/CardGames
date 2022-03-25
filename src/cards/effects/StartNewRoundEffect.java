package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class StartNewRoundEffect extends Effect {
	public final static String NAME = "Start New Round";

	public StartNewRoundEffect () {
		this (NAME);
	}

	public StartNewRoundEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public StartNewRoundEffect (ActorI aActor) {
		this (NAME, aActor);
	}
	public StartNewRoundEffect (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public StartNewRoundEffect (XMLNode aEffectNode, GameManager aGameManager) {
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
		boolean tEffectApplied = false;
		
		aGameManager.finishRound ();
		aGameManager.startNewRound ();
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

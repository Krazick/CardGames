package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class InitiateGameEffect extends Effect {
	public final static String NAME = "Initiate Game";
	final static AttributeName AN_INITIATE_GAME = new AttributeName ("initiateGame");
	boolean initiateGame;
	
	public InitiateGameEffect () {
		this (NAME);
	}

	public InitiateGameEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public InitiateGameEffect (String aName, ActorI aActor) {
		this (aName, aActor, false);
	}

	public InitiateGameEffect (ActorI aActor, boolean aInitiateGame) {
		this (NAME, aActor, aInitiateGame);
	}
	
	public InitiateGameEffect (String aName, ActorI aActor, boolean aInitiateGame) {
		super (aName, aActor);
		setInitiateGame (aInitiateGame);
	}
	
	public InitiateGameEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
		initiateGame = aEffectNode.getThisBooleanAttribute (AN_INITIATE_GAME);
	}

	public void setInitiateGame (boolean aInitiateGame) {
		initiateGame = aInitiateGame;
	}
	
	public boolean getInitiateGame () {
		return initiateGame;
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
		tEffectElement.setAttribute (AN_INITIATE_GAME, initiateGame);
	
		return tEffectElement;
	}

	@Override
	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied = false;
		
		if (initiateGame) {
			aGameManager.initiateNetworkGame ();
			tEffectApplied = true;
		}
		
		return tEffectApplied;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		return (REPORT_PREFIX + name + " set as " + initiateGame + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

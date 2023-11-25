package cards.actions;

import cards.effects.InitiateGameEffect;
import cards.main.GameManager;
import geUtilities.XMLNode;

public class StartNewGameAction extends StartNewRoundAction {
	public final static String NAME = "Start New Game";
	
	public StartNewGameAction () {
		this (NAME);
	}

	public StartNewGameAction (String aName) {
		this (aName, NO_ACTOR);
	}

	public StartNewGameAction (ActorI aActor) {
		this (NAME, aActor);
	}
	
	public StartNewGameAction (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public StartNewGameAction (XMLNode aActionNode, GameManager aGameManager) {
		super (aActionNode, aGameManager);
		setName (NAME);
	}

	public void addInitiateGameEffect (ActorI aPlayer, boolean aInitiateGame) {
		InitiateGameEffect tInitiateGameEffect;

		tInitiateGameEffect = new InitiateGameEffect (aPlayer, aInitiateGame);
		addEffect (tInitiateGameEffect);
	}
}

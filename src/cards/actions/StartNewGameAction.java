package cards.actions;

import cards.effects.InitiateGameEffect;
import cards.effects.ShowHandsEffect;
import cards.main.GameManager;
import geUtilities.xml.XMLNode;

public class StartNewGameAction extends StartNewRoundAction {
	public static final String NAME = "Start New Game";

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
	
	public void addShowHandsEffect (ActorI aPlayer) {
		ShowHandsEffect tShowHandsEffect;
		
		tShowHandsEffect = new ShowHandsEffect (aPlayer);
		addEffect (tShowHandsEffect);
	}
}

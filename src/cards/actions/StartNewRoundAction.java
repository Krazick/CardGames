package cards.actions;

import cards.main.GameManager;
import cards.main.Player;
import cards.effects.Effect;
import cards.effects.NewShuffleSeedEffect;
import cards.effects.StartNewRoundEffect;
import geUtilities.XMLNode;

public class StartNewRoundAction extends Action {
	public final static String NAME = "Start New Round";
	
	public StartNewRoundAction () {
		this (NAME);
	}

	public StartNewRoundAction (String aName) {
		this (aName, NO_ACTOR);
	}

	public StartNewRoundAction (ActorI aActor) {
		this (NAME, aActor);
	}
	
	public StartNewRoundAction (String aName, ActorI aActor) {
		super (aName, aActor);
	}

	public StartNewRoundAction (XMLNode aActionNode, GameManager aGameManager) {
		super (aActionNode, aGameManager);
		setName (NAME);
	}
//
//	public void addInitiateGameEffect (ActorI aPlayer, boolean aInitiateGame) {
//		InitiateGameEffect tInitiateGameEffect;
//
//		tInitiateGameEffect = new InitiateGameEffect (aPlayer, aInitiateGame);
//		addEffect (tInitiateGameEffect);
//	}

	public void addStartNewRoundEffect (Player tPlayer) {
		StartNewRoundEffect tStartNewRoundEffect;

		tStartNewRoundEffect = new StartNewRoundEffect (tPlayer);
		addEffect (tStartNewRoundEffect);
	}

	public void addNewShuffleSeedEffect (ActorI aPlayer, Long aShuffleSeed) {
		NewShuffleSeedEffect tNewShuffleSeedEffect;

		tNewShuffleSeedEffect = new NewShuffleSeedEffect (aPlayer, aShuffleSeed);
		addEffect (tNewShuffleSeedEffect);
	}
	
	public Long getNewShuffleSeed () {
		Long tNewShuffleSeed = (long) 0;
		
		for (Effect tEffect : effects) {
			if (tEffect instanceof NewShuffleSeedEffect) {
				tNewShuffleSeed = ((NewShuffleSeedEffect) tEffect).getNewShuffleSeed ();
			}
		}
		
		return tNewShuffleSeed;
	}
	
	@Override
	public String getSimpleActionReport () {
		String tSimpleActionReport = "";
		Long tNewShuffleSeed;
		
		tNewShuffleSeed = getNewShuffleSeed ();
		tSimpleActionReport = actor.getName () + " set New Shuffle Seed to " + tNewShuffleSeed + ".";
		
		return tSimpleActionReport;
	}
}

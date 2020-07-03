package cards.actions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cards.main.Player;

@DisplayName ("Action Class Testing")
class ActionTesting {

	@Test
	@DisplayName ("Testing Action Class NoArg Constructor")
	void testBasicNoArgConstructor () {
		Action tAction;
		
		tAction = new Action ();
		assertFalse (tAction.actorIsSet ());
		assertEquals (Action.NO_NAME, tAction.getName ());
		assertEquals (ActorI.NO_ACTOR, tAction.getActor ());
		assertFalse (tAction.getChainToPrevious ());
	}
	
	@Test
	@DisplayName ("Testing Action Class Actor Constructor")
	void testBasicActorConstructor () {
		Action tAction;
		Player mPlayer;
				
		mPlayer = Mockito.mock (Player.class);
		
		tAction = new Action (mPlayer);
		assertTrue (tAction.actorIsSet ());
		assertEquals (Action.NO_NAME, tAction.getName ());
		assertEquals (mPlayer, tAction.getActor ());
		assertFalse (tAction.getChainToPrevious ());
	}

	@Test
	@DisplayName ("Testing Action Class Constructor")
	void testBasicConstructor () {
		Action tAction;
		Player mPlayer;
		String tActionName;
		
		mPlayer = Mockito.mock (Player.class);
		tActionName = "JUnitTest";
		
		tAction = new Action (tActionName, mPlayer);
		assertTrue (tAction.actorIsSet ());
		assertEquals (tActionName + " Action", tAction.getName ());
		assertEquals (mPlayer, tAction.getActor ());
		assertFalse (tAction.getChainToPrevious ());
	}
}

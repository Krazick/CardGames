package cards.actions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.Mockito;

import cards.main.Player;

@DisplayName ("Action Class Testing")
class ActionTesting {
	Action action;
	Player mPlayer;
	String actionName;
	String mPlayerName = "JUnitPlayer";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp () throws Exception {
		
		mPlayer = Mockito.mock (Player.class);
		Mockito.when (mPlayer.getName ()).thenReturn (mPlayerName);
		actionName = "JUnitTest";
		
		action = new Action (actionName, mPlayer);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown () throws Exception {
	}

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
		assertTrue (action.actorIsSet ());
		assertEquals (actionName + " Action", action.getName ());
		assertEquals (mPlayer, action.getActor ());
		assertFalse (action.getChainToPrevious ());
		assertEquals ("Dummy Action", action.getName ("Dummy"));
	}
	
	@Test
	@DisplayName ("Testing Action Reports")
	void testActionReports () {
		assertEquals ("-" + mPlayerName + " performed " + actionName + " Action Chain to Previous [false]",
						action.getBriefActionReport ());
		
		actionName = "JUnitTest2";
		action.setName (actionName);
		
		assertEquals ("Brief [-" + mPlayerName + " performed " + actionName + " Action Chain to Previous [false]]",
						action.getSimpleActionReport ());
	}
	
//	@Test
//	@DisplayName ("Testing Action Reports with Mocked Effects")
//	void testActionWithMockedEffects () {
////		Effect mEffect;
//		
////		mPlayer = Mockito.mock (Player.class);
////		Mockito.when (mPlayer.getName ()).thenReturn (mPlayerName);
////		mEffect = Mockito.mock (Effect.class);
////		Mockito.when(mEffect.getEffectReport(aGameManager))
////		action.addEffect (mEffect);
//	}
}

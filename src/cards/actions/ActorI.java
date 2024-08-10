package cards.actions;

import geUtilities.xml.AttributeName;

public interface ActorI {
	public final ActorI NO_ACTOR = null;
	public static final AttributeName AN_ACTOR_NAME = new AttributeName ("actor");
	public static final AttributeName AN_TO_ACTOR_NAME = new AttributeName ("toActor");
	public static final AttributeName AN_FROM_ACTOR_NAME = new AttributeName ("fromActor");
	public enum ActionStates { 
		NoAction ("No Action"), Shuffle ("Shuffle"), NotPassed ("Not Passed"),
		Passed ("Passed"), ReceivedPass ("Received Passed"), ReadyToPlay ("Ready to Play"),
		PlayedCard ("Played Card"), WillLead ("Will Lead"),	
		
		NoState ("No State");
		
		private String enumString;
		
		ActionStates (String aEnumString) { enumString = aEnumString; }
		
		@Override
		public String toString () { return enumString; }
		
		};
	
	public String getName ();
	public String getStateName ();
	public void resetActionState (ActionStates aActionState);
	public boolean isAPlayer ();
}
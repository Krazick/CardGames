package cards.actions;

import cards.main.GameManager;
import cards.effects.Effect;
import cards.utilities.AttributeName;
import cards.utilities.ElementName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;
import org.w3c.dom.NodeList;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

public class Action {
	public final static String NO_NAME = ">> NO ACTION NAME <<";
	public final static ActorI NO_ACTOR = null;
	public final static String NO_ROUND_ID = ">> NO ROUND ID <<";
	public final static String REPORT_PREFIX = "-";
	public final static ElementName EN_ACTIONS = new ElementName ("Actions");
	public final static ElementName EN_ACTION = new ElementName ("Action");
	public final static AttributeName AN_CLASS = new AttributeName ("class");
	static final AttributeName AN_NAME = new AttributeName ("name");
	static final AttributeName AN_ROUND_TYPE = new AttributeName ("roundType");
	static final AttributeName AN_ROUND_ID = new AttributeName ("roundID");
	static final AttributeName AN_CHAIN_PREVIOUS = new AttributeName ("chainPrevious");
	String name;
	String roundID;
	ActorI actor;
	List<Effect> effects;
	Boolean chainToPrevious; // Chain this Action to Previous Action -- 
							// If Undo This Action, Undo Previous Action as well - Default is FALSE;
	
	public Action () {
		this (NO_NAME);
	}
	
	public Action (String aName) {
		setName (aName);
		setActor (NO_ACTOR);
		setChainToPrevious (false);
		effects = new LinkedList<Effect> ();		
	}
	
	public Action (ActorI.ActionStates aRoundType, String aRoundID, ActorI aActor) {
		setName (NO_NAME);
		setActor (aActor);
		setRoundID (aRoundID);
		setChainToPrevious (false);
		effects = new LinkedList<Effect> ();
	}
	
	public Action (XMLNode aActionNode, GameManager aGameManager) {
		String tActionName, tActorName;
		ActorI tActor;
		Boolean tChainToPrevious;
		
		tActionName = aActionNode.getThisAttribute (AN_NAME);
		tActorName = aActionNode.getThisAttribute (ActorI.AN_ACTOR_NAME);
		tChainToPrevious = aActionNode.getThisBooleanAttribute (AN_CHAIN_PREVIOUS);
		tActor = aGameManager.getActor (tActorName);
		
		setName (tActionName);
		setActor (tActor);
		setChainToPrevious (tChainToPrevious);
		effects = new LinkedList<Effect> ();
		
		XMLNode tEffectsNode, tEffectNode;
		NodeList tEffectsChildren, tEffectChildren;
		int tEffectNodeCount, tEffectsNodeCount, tEffectIndex, tEffectsIndex;
		String tEffectsNodeName, tEffectNodeName;
		String tClassName;
		Effect tEffect;
		Class<?> tEffectToLoad;
		Constructor<?> tEffectConstructor;
	
		tEffectsChildren = aActionNode.getChildNodes ();
		tEffectsNodeCount = tEffectsChildren.getLength ();
		try {
			for (tEffectsIndex = 0; tEffectsIndex < tEffectsNodeCount; tEffectsIndex++) {
				tEffectsNode = new XMLNode (tEffectsChildren.item (tEffectsIndex));
				tEffectsNodeName = tEffectsNode.getNodeName ();
				if (Effect.EN_EFFECTS.equals (tEffectsNodeName)) {
					tEffectChildren = tEffectsNode.getChildNodes ();
					tEffectNodeCount = tEffectChildren.getLength ();
					for (tEffectIndex = 0; tEffectIndex < tEffectNodeCount; tEffectIndex++) {
						tEffectNode = new XMLNode (tEffectChildren.item (tEffectIndex));
						tEffectNodeName = tEffectNode.getNodeName ();
						if (Effect.EN_EFFECT.equals (tEffectNodeName)) {
							// Use Reflections to identify the OptionEffect to create, and call the constructor with the XMLNode and Game Manager
							tClassName = tEffectNode.getThisAttribute (Effect.AN_CLASS);
							tEffectToLoad = Class.forName (tClassName);
							tEffectConstructor = tEffectToLoad.getConstructor (tEffectNode.getClass (), aGameManager.getClass ());
							tEffect = (Effect) tEffectConstructor.newInstance (tEffectNode, aGameManager);
							addEffect (tEffect);
						}
					}
				}
			}			
		} catch (Exception tException) {
			System.out.println ("Caught Exception with message ");
			tException.printStackTrace ();
		}
	}

	public boolean actorIsSet () {
		boolean tActorSet;
		
		tActorSet = false;
		if (actor != null) {
			tActorSet = true;
		}
		
		return tActorSet;
	}
	
	public void addEffect (Effect aEffect) {
		effects.add (aEffect);
	}
	
	public String getXMLFormat (ElementName aElementName) {
		XMLDocument tXMLDocument = new XMLDocument ();
		String tXMLFormat = "";
		XMLElement tActionElement, tGameActivityElement;
		
		tActionElement = getActionElement (tXMLDocument);
		tGameActivityElement = tXMLDocument.createElement (aElementName);
		tGameActivityElement.appendChild(tActionElement);
		tXMLDocument.appendChild (tGameActivityElement);
		tXMLFormat = tXMLDocument.toString ();
		
		return tXMLFormat;
	}
	
	/* Build XML Element to save the State */
	public XMLElement getActionElement (XMLDocument aXMLDocument) {
		XMLElement tActionElement, tEffectsElement, tEffectElement;
		String tActorName;
		
		tActorName = actor.getName ();

		tActionElement = aXMLDocument.createElement (EN_ACTION);
		tActionElement.setAttribute (AN_CLASS, this.getClass ().getName ());
		tActionElement.setAttribute (AN_NAME, getName ());
		tActionElement.setAttribute (AN_ROUND_ID, getRoundID ());
		tActionElement.setAttribute (ActorI.AN_ACTOR_NAME, tActorName);
		tActionElement.setAttribute (AN_CHAIN_PREVIOUS, getChainToPrevious ());
		tEffectsElement = aXMLDocument.createElement (Effect.EN_EFFECTS);
		for (Effect tEffect : effects) {
			tEffectElement = tEffect.getEffectElement (aXMLDocument, ActorI.AN_ACTOR_NAME);
			tEffectsElement.appendChild (tEffectElement);
		}

		tActionElement.appendChild (tEffectsElement);
		
		return tActionElement;
	}
	
	public ActorI getActor () {
		return actor;
	}
	
	public Boolean getChainToPrevious () {
		return chainToPrevious;
	}
	
	public String getName () {
		return name;
	}

	public String getName (String aName) {
		return aName + " " + EN_ACTION;
	}
	
	public String getRoundID () {
		return roundID;
	}
	
	public String getActionReport (GameManager aGameManager) {
		String tActionReport;
		
		tActionReport = getBriefActionReport ();
		for (Effect tEffect : effects) {
			tActionReport += "\n" + tEffect.getEffectReport (aGameManager);
		}
		
		return tActionReport;
	}
	
	public String getSimpleActionReport () {
		String tReport = getBriefActionReport ();
		
		tReport = "Brief [" + tReport + "]";
		
		return tReport;
	}
	
	public String getBriefActionReport () {
		return REPORT_PREFIX + actor.getName () + 
				" performed " + name + " Chain to Previous [" + chainToPrevious + "]";
	}
	
	public void printBriefActionReport () {
		System.out.println (getBriefActionReport ());		
	}
	
	public void printUndoCompletion (boolean aActionUndone) {
		if (aActionUndone == false) {
			System.out.println ("***Not all Effects Undone properly***");
		}
	}
	
	public void setActor (ActorI aActor) {
		actor = aActor;
	}
	
	public void setChainToPrevious (Boolean aChainToPrevious) {
		chainToPrevious = aChainToPrevious;
	}
	
	public void setName (String aName) {
		name = createFullName (aName);
	}
	
	private String createFullName (String aName) {
		return aName + " Action";
	}
	
	public void setRoundID (String aRoundID) {
		roundID = aRoundID;
	}
		
	public boolean undoAction (GameManager aGameManager) {
		boolean tActionUndone, tEffectUndone;
		
		tActionUndone = true;
		for (Effect tEffect: effects) {
			System.out.println ("Trying to Undo " + name + " Effect: " + tEffect.getName ());
			tEffectUndone = tEffect.undoEffect (aGameManager);
			tActionUndone &= tEffectUndone;
			System.out.println ("Tried to Undo a " + name + ", Effect " + tEffect.getName () +
					" EffectUndone Flag " + tEffectUndone);
		}
		
		System.out.println ("Request to Undo Action -- All Effects Undone");
		
		return tActionUndone;
	}
	
	public boolean wasLastActionStartAuction () {
		return false;
	}

	public boolean applyAction (GameManager aGameManager) {
		boolean tActionApplied, tEffectApplied;
		
		tActionApplied = true;
		for (Effect tEffect: effects) {
//			System.out.println ("---Trying to Apply Effect:");
//			tEffect.printEffectReport (aGameManager);
			tEffectApplied = tEffect.applyEffect (aGameManager);
			tActionApplied &= tEffectApplied;
			System.out.println ("Tried to Apply a |" + name + "|, Effect " + tEffect.getName () +
					" EffectApplied Flag " + tEffectApplied);
		}
		
		System.out.println ("Applied All Effects " + tActionApplied);

		return tActionApplied;
	}
}

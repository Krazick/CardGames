package cards.effects;

import cards.actions.ActorI;
import cards.main.GameManager;
import cards.utilities.XMLNode;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.AttributeName;

public class NewShuffleSeedEffect extends Effect {
	public final static String NAME = "New Shuffle Seed";
	final static AttributeName AN_NEW_SHUFFLE_SEED = new AttributeName ("newShuffleSeed");
	Long newShuffleSeed;
	
	public NewShuffleSeedEffect () {
		this (NAME);
	}

	public NewShuffleSeedEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public NewShuffleSeedEffect (ActorI aActor, Long aNewShuffleSeed) {
		this (NAME, aActor, aNewShuffleSeed);
	}

	public NewShuffleSeedEffect (String aName, ActorI aActor) {
		super (aName, aActor);
	}
	
	public NewShuffleSeedEffect (String aName, ActorI aActor, Long aNewShuffleSeed) {
		super (aName, aActor);
		setNewShuffleSeed (aNewShuffleSeed);
	}

	public NewShuffleSeedEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
		newShuffleSeed = aEffectNode.getThisLongAttribute (AN_NEW_SHUFFLE_SEED);
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
		tEffectElement.setAttribute (AN_NEW_SHUFFLE_SEED, newShuffleSeed);
	
		return tEffectElement;
	}

	public void setNewShuffleSeed (Long aNewShuffleSeed) {
		newShuffleSeed = aNewShuffleSeed;
	}
	
	public Long getNewShuffleSeed () {
		return newShuffleSeed;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		return (REPORT_PREFIX + name + " set to  " + newShuffleSeed + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

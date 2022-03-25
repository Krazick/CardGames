package cards.effects;

import cards.actions.ActorI;
import cards.main.Card;
import cards.main.GameManager;
import cards.main.Player;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class PassTheCardEffect extends ToActorEffect {
	public final static String NAME = "Pass the Card";
	final static AttributeName AN_CARD_NAME = new AttributeName ("cardName");
	Card card;

	public PassTheCardEffect () {
		this (NAME);
	}

	public PassTheCardEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PassTheCardEffect (String aName, ActorI aFromActor) {
		this (aName, aFromActor, ActorI.NO_ACTOR);
	}

	public PassTheCardEffect (String aName, ActorI aFromActor, ActorI aToActor) {
		this (aName, aFromActor, aToActor, Card.NO_CARD);
	}
	
	public PassTheCardEffect (ActorI aFromActor, ActorI aToActor, Card aCard) {
		this (NAME, aFromActor, aToActor, aCard);
	}
	
	public PassTheCardEffect (String aName, ActorI aFromActor, ActorI aToActor, Card aCard) {
		super (aName, aFromActor, aToActor);
		setCard (aCard);
	}
	
	public PassTheCardEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
		Player tFromPlayer;
		Card tCard;
		String tCardName;
		
		tFromPlayer = getFromPlayer ();
		tCardName = aEffectNode.getThisAttribute (AN_CARD_NAME);
		tCard = tFromPlayer.getCard (tCardName);
		setCard (tCard);
	}
	
	public void setCard (Card aCard) {
		card = aCard;
	}
	
	public Card getCard () {
		return card;
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
		tEffectElement.setAttribute (AN_CARD_NAME, card.getAbbrev ());
	
		return tEffectElement;
	}

	@Override
	public boolean applyEffect (GameManager aGameManager) {
		boolean tEffectApplied = false;
		Player tFromPlayer, tToPlayer;
		
		tFromPlayer = getFromPlayer ();
		tToPlayer = getToPlayer ();
		tFromPlayer.passACard (card, tToPlayer);
		tEffectApplied = true;

		return tEffectApplied;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		String tCardName, tFromActorName, tToActorName;
		
		tCardName = card.getShortName ();
		tFromActorName = actor.getName ();
		tToActorName = toActor.getName ();
		return (REPORT_PREFIX + name + tCardName + " from " + tFromActorName + " to " + tToActorName + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}
}

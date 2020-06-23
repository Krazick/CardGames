package cards.effects;

import cards.actions.ActorI;
import cards.main.Card;
import cards.main.GameManager;
import cards.main.Player;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class PassACardEffect extends Effect {
	public final static String NAME = "Pass the Card";
	final static AttributeName AN_CARD_NAME = new AttributeName ("cardName");
	ActorI toActor;
	Card card;

	public PassACardEffect () {
		this (NAME);
	}

	public PassACardEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PassACardEffect (String aName, ActorI aFromActor) {
		this (aName, aFromActor, ActorI.NO_ACTOR);
	}

	public PassACardEffect (String aName, ActorI aFromActor, ActorI aToActor) {
		this (aName, aFromActor, aToActor, Card.NO_CARD);
	}
	
	public PassACardEffect (ActorI aFromActor, ActorI aToActor, Card aCard) {
		this (NAME, aFromActor, aToActor, aCard);
	}
	
	public PassACardEffect (String aName, ActorI aFromActor, ActorI aToActor, Card aCard) {
		super (aName, aFromActor);
		setToActor (aToActor);
		setCard (aCard);
	}
	
	public PassACardEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
		Player tFromPlayer;
		ActorI tToActor;
		Card tCard;
		String tToActorName, tCardName;
		
		tFromPlayer = (Player) getActor ();
		tToActorName = aEffectNode.getThisAttribute (ActorI.AN_TO_ACTOR_NAME);
		tToActor = aGameManager.getActor (tToActorName);
		setToActor (tToActor);
		tCardName = aEffectNode.getThisAttribute (AN_CARD_NAME);
		tCard = tFromPlayer.getCard (tCardName);
		setCard (tCard);
	}

	public void setToActor (ActorI aToActor) {
		toActor = aToActor;
	}
	
	public void setCard (Card aCard) {
		card = aCard;
	}
	
	public Player getFromPlayer () {
		return (Player) actor;
	}
	
	public Player getToPlayer () {
		return (Player) toActor;
	}
	
	public ActorI getToActor () {
		return toActor;
	}
	
	public Card getCard () {
		return card;
	}
	
	@Override
	public XMLElement getEffectElement (XMLDocument aXMLDocument, AttributeName aActorAN) {
		XMLElement tEffectElement;
		
		tEffectElement = super.getEffectElement (aXMLDocument, aActorAN);
		tEffectElement.setAttribute (ActorI.AN_TO_ACTOR_NAME, toActor.getName ());
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
//		aGameManager.passACard (getFromPlayer (), getToPlayer (), card);
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

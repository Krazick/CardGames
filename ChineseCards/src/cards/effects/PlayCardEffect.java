package cards.effects;

import cards.actions.ActorI;
import cards.main.Card;
import cards.main.GameManager;
import cards.main.Player;
import cards.utilities.AttributeName;
import cards.utilities.XMLDocument;
import cards.utilities.XMLElement;
import cards.utilities.XMLNode;

public class PlayCardEffect extends Effect {
	public final static String NAME = "Play Card";
	final static AttributeName AN_CARD_NAME = new AttributeName ("cardName");
	Card card;
	
	public PlayCardEffect () {
		this (NAME);
	}

	public PlayCardEffect (String aName) {
		this (aName, ActorI.NO_ACTOR);
	}

	public PlayCardEffect (ActorI aActor, Card aCard) {
		this (NAME, aActor, aCard);
	}
	
	public PlayCardEffect (String aName, ActorI aActor) {
		this (aName, aActor, Card.NO_CARD);
	}

	public PlayCardEffect (String aName, ActorI aActor, Card aCard) {
		super (aName, aActor);
		setCard (aCard);
	}
	
	public PlayCardEffect (XMLNode aEffectNode, GameManager aGameManager) {
		super (aEffectNode, aGameManager);
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
		Player tFromPlayer;
		
		tFromPlayer = getFromPlayer ();
		tFromPlayer.playCard (card);
		tEffectApplied = true;

		return tEffectApplied;
	}
	
	@Override
	public String getEffectReport (GameManager aGameManager) {
		String tCardName, tFromActorName;
		
		tCardName = card.getShortName ();
		tFromActorName = actor.getName ();
		return (REPORT_PREFIX + name + tCardName + " from " + tFromActorName  + ".");
	}
	
	@Override
	public void printEffectReport (GameManager aGameManager) {
		System.out.println (getEffectReport (aGameManager));
	}

}

package cards.main;

import org.w3c.dom.NodeList;

import geUtilities.AttributeName;
import geUtilities.ElementName;
import geUtilities.ParsingRoutineI;
import geUtilities.XMLDocument;
import geUtilities.XMLElement;
import geUtilities.XMLNode;
import geUtilities.XMLNodeList;
//import cards.main.OptionEffect;
//import cards.main.Option;

public class GameInfo {
	final AttributeName AN_MIN_PLAYERS = new AttributeName ("minPlayers");
	final AttributeName AN_MAX_PLAYERS = new AttributeName ("maxPlayers");
	final AttributeName AN_ID = new AttributeName ("id");
	final AttributeName AN_NAME = new AttributeName ("name");
	public final static ElementName EN_GAME_INFO = new ElementName ("GameInfo");
	static final int NO_GAME_ID = 0;
	static final String NO_NAME = "<NONE>";
	static final int NO_MIN_PLAYERS = 0;
	static final int NO_MAX_PLAYERS = 0;
	int id;
	String name;
	int minPlayers;
	int maxPlayers;
	int optionIndex;
	private boolean gameTestFlag;
	Option options [];

	public GameInfo () {
		setValues (NO_GAME_ID, NO_NAME, NO_MIN_PLAYERS, NO_MAX_PLAYERS);
	}
	
	public GameInfo (XMLNode aCellNode) {
		NodeList tChildren;
		XMLNode tChildNode;
		XMLNodeList tXMLNodeList;
		String tChildName;
		int tOptionCount;
		String tName;
		int tID, tMinPlayers, tMaxPlayers;
		int tChildrenCount, tIndex;

		tID = aCellNode.getThisIntAttribute (AN_ID);
		tName = aCellNode.getThisAttribute (AN_NAME);
		tMinPlayers = aCellNode.getThisIntAttribute (AN_MIN_PLAYERS);
		tMaxPlayers = aCellNode.getThisIntAttribute (AN_MAX_PLAYERS);
			
		setValues (tID, tName, tMinPlayers, tMaxPlayers);

		tChildren = aCellNode.getChildNodes ();
		tChildrenCount = tChildren.getLength ();
		for (tIndex = 0; tIndex < tChildrenCount; tIndex++) {
			tChildNode = new XMLNode (tChildren.item (tIndex));
			tChildName = tChildNode.getNodeName ();
			if (Option.EN_OPTIONS.equals (tChildName)) {
				tXMLNodeList = new XMLNodeList (optionsParsingRoutine);
				tOptionCount = tXMLNodeList.getChildCount (tChildNode, Option.EN_OPTION);
				options = new Option [tOptionCount];
				optionIndex = 0;
				tXMLNodeList.parseXMLNodeList (tChildNode, Option.EN_OPTION);
			}
		}
	}
	
	ParsingRoutineI optionsParsingRoutine  = new ParsingRoutineI ()  {
		@Override
		public void foundItemMatchKey1 (XMLNode aChildNode) {
			options [optionIndex++] = new Option (aChildNode);	
		}
	};
	
	public int getOptionCount () {
		if (options == null) {
			return 0;
		} else {
			return options.length;
		}
	}

	public Option getOptionIndex (int aIndex) {
		if (options == null) {
			return null;
		} else if (aIndex >= getOptionCount ()) {
			return null;
		} else {
			return options [aIndex];
		}
	}

	public boolean canPlayWithXPlayers (int aNumPlayers) {
		boolean tCanPlay;
		
		tCanPlay = false;
		if ((aNumPlayers >= minPlayers) && (aNumPlayers <= maxPlayers)) {
			tCanPlay = true;
		}
		
		return tCanPlay;
	}
		
	public XMLElement getGameInfoElement (XMLDocument aXMLDocument) {
		XMLElement tXMLElement;
		XMLElement tGameOptions, tGameOption;
		
		tXMLElement = aXMLDocument.createElement (EN_GAME_INFO);
		tXMLElement.setAttribute (AN_NAME, name);
		
		if (options != null) {
			tGameOptions = aXMLDocument.createElement (Option.EN_OPTIONS);
			for (Option tOption : options) {
				if (tOption.isEnabled ()) {
					tGameOption = tOption.getOptionElement (aXMLDocument);
					tGameOptions.appendChild (tGameOption);
				}
			}
			tXMLElement.appendChild (tGameOptions);
		}
		
		return tXMLElement;
	}

	public String getGameName () {
		return name;
	}

	public String getHTMLDescription () {
		String tHTMLDescription;
		
		tHTMLDescription = "<html><body><h3>" + name;
		tHTMLDescription += "</h3>";
		
		return tHTMLDescription;
	}
	
	public int getID () {
		return id;
	}
	
	public int getMaxPlayers () {
		return maxPlayers;
	}
	
	public int getMinPlayers () {
		return minPlayers;
	}
	
	public String getName () {
		return name;
	}

	public void printGameInfo () {
		System.out.println ("Title: " + name);
		System.out.println ("Min Players: " + minPlayers);
		System.out.println ("Max Players: " + maxPlayers);
	}
	
	public void setupOptions (GameManager aGameManager) {
//		int tEffectCount, tEffectIndex;
//		OptionEffect tEffect;
		System.out.println ("Setup Options");
	}
	
	public void setValues (int aID, String aName, int aMinPlayers, int aMaxPlayers) {
		id = aID;
		name = aName;
		minPlayers = aMinPlayers;
		maxPlayers = aMaxPlayers;
	}

	public boolean isATestGame () {
		return gameTestFlag;
	}
	
	public void setTestingFlag (boolean aGameTestFlag) {
		gameTestFlag = aGameTestFlag;
	}
}

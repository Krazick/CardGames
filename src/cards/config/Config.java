package cards.config;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

import cards.main.GameManager;
import geUtilities.xml.AttributeName;
import geUtilities.xml.ElementName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

public class Config {
	public static final AttributeName AN_GAME_NAME = new AttributeName ("gameName");
	public static final ElementName EN_CONFIG = new ElementName ("Config");
	public static final ElementName EN_FRAMES = new ElementName ("Frames");
	public static final GameFrameConfig NO_GAME_FRAME = null;
	ArrayList<GameFrameConfig> gameFrames;
	GameManager gameManager;

	public Config (GameManager aGameManager) {
		gameManager = aGameManager;	
		gameFrames = new ArrayList<GameFrameConfig> ();
	}
	
	public Config (XMLNode aConfigNode, GameManager aGameManager) {
		NodeList tChildren;
		XMLNode tChildNode;
		int tNodeCount, tNodeIndex;
		GameFrameConfig tGameFrameConfig;
		
		gameManager = aGameManager;
		tChildren = aConfigNode.getChildNodes ();
		tNodeCount = tChildren.getLength ();
		gameFrames = new ArrayList<GameFrameConfig> ();
		try {
			for (tNodeIndex = 0; tNodeIndex < tNodeCount; tNodeIndex++) {
				tChildNode = new XMLNode (tChildren.item (tNodeIndex));
				if (EN_FRAMES.equals (tChildNode.getNodeName ())) {
					tGameFrameConfig = new GameFrameConfig (tChildNode);
					gameFrames.add (tGameFrameConfig);
				}
			}
		} catch (Exception tException) {
			System.err.println ("Caught Exception with message ");
			tException.printStackTrace ();
		}
	}
	
	public int getGameFramesCount () {
		return gameFrames.size ();
	}
	
	public XMLElement getXMLFramesElement (XMLDocument aXMLDocument, int aGameIndex) {
		int tGamesCount, tFrameCount, tFrameIndex;
		XMLElement tXMLFramesElement = XMLElement.NO_XML_ELEMENT;
		XMLElement tXMLFrameElement;
		GameFrameConfig tGameFrameConfig;
		String tGameName, tFrameName;
		
		tGamesCount = getGameFramesCount ();
		if ((tGamesCount > 0) && (aGameIndex < tGamesCount)) {
			tXMLFramesElement = aXMLDocument.createElement (EN_FRAMES);
			tGameFrameConfig = gameFrames.get (aGameIndex);
			tGameName = tGameFrameConfig.getGameName ();
			tXMLFramesElement.setAttribute (AN_GAME_NAME, tGameName);
			tFrameCount = tGameFrameConfig.getFrameCount ();
			for (tFrameIndex = 0; tFrameIndex < tFrameCount; tFrameIndex++) {
				tFrameName = tGameFrameConfig.getFrameName (tFrameIndex);
				if (tFrameName != FrameInfo.NO_FRAME_NAME) {
					tXMLFrameElement = tGameFrameConfig.getXMLFrameElement (tFrameName, aXMLDocument);
					tXMLFramesElement.appendChild (tXMLFrameElement);
				}
			}
		}
		
		return tXMLFramesElement;
	}
	
	public GameFrameConfig getGameFrameConfigFor (int aGameIndex) {
		GameFrameConfig tGameFrameConfig = NO_GAME_FRAME;
		
		if ((getGameFramesCount () > 0) && (aGameIndex < getGameFramesCount ())) {
			tGameFrameConfig = gameFrames.get (aGameIndex);
		}
		
		return tGameFrameConfig;
	}
	
	public GameFrameConfig getGameFrameConfigFor (String aGameName) {
		GameFrameConfig tFoundGameFrameConfig = NO_GAME_FRAME;
		
		if (getGameFramesCount () > 0) {
			for (GameFrameConfig tGameFrameConfig : gameFrames) {
				if (tGameFrameConfig.getGameName ().equals (aGameName)) {
					tFoundGameFrameConfig = tGameFrameConfig;
				}
			}
		}
		
		return tFoundGameFrameConfig;
	}
}

package cards.config;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

import geUtilities.xml.AttributeName;
import geUtilities.xml.ElementName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

public class GameFrameConfig {
	public static final AttributeName AN_GAME_NAME = new AttributeName ("gameName");
	public static final ElementName EN_FRAME = new ElementName ("Frame");

	String gameName;
	ArrayList<FrameInfo> frameInfoList;
	
	public GameFrameConfig (String aGameName) {
		gameName = aGameName;
	}
	
	public GameFrameConfig (XMLNode aFramesNode) {
		String tGameName;
		NodeList tChildren;
		XMLNode tChildNode;
		int tNodeCount, tNodeIndex;
		FrameInfo tFrameInfo;
		
		tGameName = aFramesNode.getThisAttribute (AN_GAME_NAME);
		gameName = tGameName;	
		tChildren = aFramesNode.getChildNodes ();
		tNodeCount = tChildren.getLength ();
		frameInfoList = new ArrayList<FrameInfo> ();
		
		try {
			for (tNodeIndex = 0; tNodeIndex < tNodeCount; tNodeIndex++) {
				tChildNode = new XMLNode (tChildren.item (tNodeIndex));
				if (EN_FRAME.equals (tChildNode.getNodeName ())) {
					tFrameInfo = new FrameInfo (tChildNode);
					frameInfoList.add (tFrameInfo);
				}
			}
		} catch (Exception tException) {
			System.err.println ("Caught Exception with message ");
			tException.printStackTrace ();
		}
	}
	
	public XMLElement createXMLFrameElement (XMLDocument aXMLDocument) {
		return aXMLDocument.createElement (EN_FRAME);
	}
	
	public XMLElement getXMLFrameElement (String aFrameName, XMLDocument aXMLDocument) {
		XMLElement tXMLFrameElement;

		tXMLFrameElement = createXMLFrameElement (aXMLDocument);
		if (aFrameName != null) {
			if (! ("".equals (aFrameName))) {
				for (FrameInfo tFrameInfo : frameInfoList) {
					if (tFrameInfo.getName ().equals (aFrameName)) {
						tXMLFrameElement = tFrameInfo.appendXMLFrameAttributes (tXMLFrameElement);
					}
				}
			}
		}
		
		return tXMLFrameElement;
	}
	
	public String getGameName () {
		return gameName;
	}
	
	public int getFrameCount () {
		return frameInfoList.size ();
	}
	
	public String getFrameName (int aFrameIndex) {
		String tFrameName = FrameInfo.NO_FRAME_NAME;
		
		if ((aFrameIndex >= 0) && (aFrameIndex < getFrameCount ())) {
			tFrameName = frameInfoList.get (aFrameIndex).getName ();
		}
		
		return tFrameName;
	}
	
	public FrameInfo getFrameInfoFor (String aFrameName) {
		FrameInfo tFrameInfoRequested = FrameInfo.NO_FRAME_INFO;
		
		for (FrameInfo tFrameInfo : frameInfoList) {
			if (aFrameName.startsWith (tFrameInfo.getName ())) {
				tFrameInfoRequested = tFrameInfo;
			}
		}
		
		return tFrameInfoRequested;
	}
}

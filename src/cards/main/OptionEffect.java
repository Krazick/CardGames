package cards.main;

import geUtilities.xml.AttributeName;
import geUtilities.xml.ElementName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

public class OptionEffect {
	static final String NO_NAME = "<NO NAME>";
	static final AttributeName AN_NAME = new AttributeName ("name");
	public static final ElementName EN_OPTION_EFFECT = new ElementName ("OptionEffect");
	public static final ElementName EN_OPTION_EFFECTS = new ElementName ("OptionEffects");
	String name;
	boolean state;
	
	public OptionEffect () {
		setValue (NO_NAME);
	}
	
	public OptionEffect (XMLNode aCellNode) {
//		String tName;
//		
//		tName = aCellNode.getThisAttribute (AN_NAME);
	}
	
	public boolean getState () {
		return state;
	}
	
	public XMLElement getEffectElement (XMLDocument aXMLDocument) {
		XMLElement tXMLElement;

		tXMLElement = aXMLDocument.createElement (EN_OPTION_EFFECT);
		tXMLElement.setAttribute (AN_NAME, name);
		
		return tXMLElement;
	}
	
	public String getName () {
		return name;
	}
		
	public void setValue (String aEffectName, boolean aState) {
		name = aEffectName;
		state = aState;
	}
	
	private void setValue (String aEffectName) {
		name = aEffectName;
	}
}

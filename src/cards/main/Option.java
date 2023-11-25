package cards.main;

import org.w3c.dom.NodeList;

import geUtilities.AttributeName;
import geUtilities.ElementName;
import geUtilities.XMLDocument;
import geUtilities.XMLElement;
import geUtilities.XMLNode;

public class Option {
	static final String NO_TITLE = "<NO TITLE>";
	static final AttributeName AN_TITLE = new AttributeName ("title");
	public static final OptionEffect NO_OPTION_EFFECT = null;
	public static final ElementName EN_OPTION = new ElementName ("Option");
	public static final ElementName EN_OPTIONS = new ElementName ("Options");
	String title;
	OptionEffect optionEffects [];
	boolean enabled;
	
	public Option () {
		setValue (NO_TITLE);
	}
	
	public Option (XMLNode aCellNode) {
		String tTitle;
		String tChildName;
		XMLNode tChildNode;
		NodeList tChildren;
		int tIndex;
		int tChildrenCount;
		int tEffectIndex;
		
		tTitle = aCellNode.getThisAttribute (AN_TITLE);
		setValue (tTitle);
		tChildren = aCellNode.getChildNodes ();
		tChildrenCount = tChildren.getLength ();
		tEffectIndex = 0;
		optionEffects = new OptionEffect [tChildrenCount];
		for (tIndex = 0; tIndex < tChildrenCount; tIndex++) {
			tChildNode = new XMLNode (tChildren.item (tIndex));
			tChildName = tChildNode.getNodeName ();
			if (OptionEffect.EN_OPTION_EFFECT.equals (tChildName)) {
				optionEffects [tEffectIndex++] = new OptionEffect (tChildNode);
			}
		}

	}
	
	public int getEffectCount () {
		return optionEffects.length;
	}
	
	public OptionEffect getEffectIndex (int aIndex) {
		OptionEffect tEffect;
		
		if ((aIndex >= 0) && (aIndex < optionEffects.length)){
			tEffect = optionEffects [aIndex];
		} else {
			tEffect = NO_OPTION_EFFECT;
		}
		
		return tEffect;
	}
	
	public XMLElement getOptionElement (XMLDocument aXMLDocument) {
		XMLElement tXMLElement;
		XMLElement tOptionEffectElements;
		XMLElement tOptionEffectElement;
		
		tXMLElement = aXMLDocument.createElement (EN_OPTION);
		tXMLElement.setAttribute (AN_TITLE, title);
		tOptionEffectElements = aXMLDocument.createElement (OptionEffect.EN_OPTION_EFFECTS);
		for (OptionEffect tEffect : optionEffects) {
			if (tEffect != null) {
				tOptionEffectElement = tEffect.getEffectElement (aXMLDocument);
				tOptionEffectElements.appendChild (tOptionEffectElement);
			}
		}
		tXMLElement.appendChild (tOptionEffectElements);
		
		return tXMLElement;
	}
	
	public String getTitle () {
		return title;
	}
	
	public boolean isEnabled () {
		return enabled;
	}
	
	public void setEnabled (boolean aEnabled) {
		enabled = aEnabled;
	}
	
	private void setValue (String aTitle) {
		title = aTitle;
		setEnabled (false);
	}
}


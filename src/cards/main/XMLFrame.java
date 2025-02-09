package cards.main;

//
//  XMLFrames.java
//  Game_18XX
//
//  Created by Mark Smith on 11/28/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

import cards.config.Config;
import cards.config.FrameInfo;
import cards.config.GameFrameConfig;

import geUtilities.xml.LoadableXMLI;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;

import java.io.IOException;

import javax.swing.JFrame;

public class XMLFrame extends JFrame {
	public static enum Visibility { ON, OFF, CONFIG_VALUE };
	
	private static final long serialVersionUID = 1L;
	public static final XMLFrame NO_XML_FRAME = null;
	int defaultWidth;
	int defaultHeight;
	int defaultXLocation;
	int defaultYLocation;
	boolean defaultVisible;
	String gameName;
	
	public XMLFrame (String aFrameName) {
		this (aFrameName, GameManager.NO_GAME_NAME);
	}
	
	public XMLFrame (String aFrameName, String aGameName) {
		super (aFrameName);
		gameName = aGameName;
	}
	
	public String getGameName () {
		return gameName;
	}
	
	public boolean loadXML (String aXMLFileName, LoadableXMLI aLoadableObject) throws IOException {
		boolean tXMLFileWasLoaded;
		
		if (! ("".equals (aXMLFileName))) {
			try {
				XMLDocument tXMLDocument = new XMLDocument (aXMLFileName);
				aLoadableObject.loadXML (tXMLDocument);
				tXMLFileWasLoaded = true;
			} catch (Exception tException) {
				System.err.println ("Oops, mucked up the XML " + aLoadableObject.getTypeName () + " File Access [" + 
								aXMLFileName + "].");
				System.err.println ("Exception Message [" + tException.getMessage () + "].");
				tException.printStackTrace (System.err);
				tXMLFileWasLoaded = false;
			}
		} else {
			System.err.println ("No File Name for " + aLoadableObject.getTypeName ());
			tXMLFileWasLoaded = false;
		}
		
		return tXMLFileWasLoaded;
	}
	
	public void setDefaults (int aWidth, int aHeight, int aXLocation, int aYLocation, boolean aVisible) {
		defaultWidth = aWidth;
		defaultHeight = aHeight;
		defaultXLocation = aXLocation;
		defaultYLocation = aYLocation;
		defaultVisible = aVisible;
	}
	
	public void setDefaults (FrameInfo aFrameInfo) {
		setDefaults (aFrameInfo.getWidth (), aFrameInfo.getHeight (),
				aFrameInfo.getXLocation (), aFrameInfo.getYLocation (), 
				aFrameInfo.getVisible ());
	}
	
	public void setDefaults (XMLNode aXMLMapRoot) {
		FrameInfo tFrameInfo = new FrameInfo (aXMLMapRoot);
		setDefaults (tFrameInfo);
	}
	
	public int getDefaultWidth () {
		return defaultWidth;
	}
	
	public int getDefaultHeight () {
		return defaultHeight;
	}
	
	public int getDefaultXLocation () {
		return defaultXLocation;
	}
	
	public int getDefaultYLocation () {
		return defaultYLocation;
	}
	
	public boolean getDefaultVisible () {
		return defaultVisible;
	}

	public void setDefaultFrameInfo () {
		setLocation (defaultXLocation, defaultYLocation);
		setSize (defaultWidth, defaultHeight);
		setVisible (defaultVisible);
	}
	
	public XMLElement getXMLFrameElement (XMLDocument aXMLDocument) {
		XMLElement tXMLFrameElement;
		FrameInfo tFrameInfo;
		GameFrameConfig tGameFrameConfig;
		
		tXMLFrameElement = XMLElement.NO_XML_ELEMENT;
		tGameFrameConfig = new GameFrameConfig ("template");
		tXMLFrameElement = tGameFrameConfig.createXMLFrameElement (aXMLDocument);
		tFrameInfo = new FrameInfo (this);
		tFrameInfo.appendXMLFrameAttributes (tXMLFrameElement);
	
		return tXMLFrameElement;
	}
	
	public static String getVisibileConfig () {
		return Visibility.CONFIG_VALUE.toString ();
	}
	
	public static String getVisibileOFF () {
		return Visibility.OFF.toString ();
	}
	
	public static String getVisibileON () {
		return Visibility.ON.toString ();
	}
	
	public void setFrameToConfigDetails (GameManager aGameManager) {
		setFrameToConfigDetails (aGameManager, getVisibileOFF ());
	}
	
	public void setFrameToConfigDetails (GameManager aGameManager, String aVisibility) {
		GameFrameConfig tGameFrameConfig;
		
		tGameFrameConfig = aGameManager.getGameFrameConfig ();
		if (tGameFrameConfig != Config.NO_GAME_FRAME) {
			setFrameToConfigDetails (tGameFrameConfig, aVisibility);
		}
	}
	
	public void setFrameToConfigDetails (GameFrameConfig aGameFrameConfig) {
		setFrameToConfigDetails (aGameFrameConfig, getVisibileConfig ());
	}
	
	public void setFrameToConfigDetails (GameFrameConfig aGameFrameConfig, String aVisibility) {
		String tGameName = getGameName ();
		
		if (aGameFrameConfig == Config.NO_GAME_FRAME) {
			System.err.println ("No Configuration Data for " + tGameName);
		} else {
			setFrameToConfigDefaults (aGameFrameConfig, aVisibility);
		}
	}
	
	public void setFrameToConfigDefaults (GameFrameConfig aGameFrameConfig, String aVisibility) {
		String tFrameName;
		FrameInfo tFrameInfo;
		
		tFrameName = getTitle ();
		tFrameInfo = aGameFrameConfig.getFrameInfoFor (tFrameName);
		if (tFrameInfo != FrameInfo.NO_FRAME_INFO) {
			setDefaults (tFrameInfo);
			setDefaultFrameInfo ();
		}
		// If the Visibility Flag passed in is 
		//		ON- always set it Visible, 
		//		OFF - always set it to Invisible
		//		Otherwise, the Default Frame setting will be used from the Game, or the User's Config File
		if (aVisibility.equals (Visibility.ON.toString ())) {
			setVisible (true);
		} else if (aVisibility.equals (Visibility.OFF.toString ())) {
			setVisible (false);				
		}

	}
}
package cards.main;

//
//  GameSet.java
//  Game_18XX
//
//  Created by Mark Smith on 12/19/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

import cards.network.JGameClient;
import geUtilities.xml.LoadableXMLI;
import geUtilities.ParsingRoutineI;
import geUtilities.xml.ElementName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import geUtilities.xml.XMLNode;
import geUtilities.xml.XMLNodeList;
import swingTweaks.KButton;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.w3c.dom.NodeList;

public class GameSet implements LoadableXMLI, ActionListener, ItemListener {
	final ElementName EN_GAMES = new ElementName ("Games");
	final ElementName EN_NETWORK = new ElementName ("Network");
	static final int NO_GAME_SELECTED = -1;
	static final String NO_GAME_NAME = "<NO-GAME>";
	static final String CHAT_TITLE = "Cards Chat Client";
	static final GameInfo [] NO_GAMES = null;
	static final GameInfo NO_GAME = null;
	private static final String NO_DESCRIPTION = "<html><body><h3>Game Description</h3><p>NO GAME SELECTED</p></body></html>";
	private static final String NEW_GAME = "New Game";
	private static final String NETWORK_GAME = "Network Game";
	private static final String LOAD_GAME = "Load Game";
	private static final String REASON_WRONG_PLAYER_COUNT = "Either too many, or too few Players for this game";
	private static final String REASON_NO_NEW_GAME = "Must select Game before you can start";
	private static final String NO_TIP = "";
	GameInfo gameInfo [];
	JPanel gamePanel;
	JPanel gameInfoPanel;
	ButtonGroup gameButtons;
	JRadioButton gameRadioButtons [];
	JCheckBox gameOptions [];
	KButton newGameButton;
	KButton networkGameButton;
	KButton loadGameButton;
	Container listAndButtonBox;
	JLabel gameInfoLabel;
	JLabel gameDescriptionLabel;
	Container boxOfDescAndOptions = null;
	int selectedGameIndex;
	int gameIndex;
	GameManager gameManager;
	
	public GameSet (GameManager aGameManager) {
		gameInfo = NO_GAMES;
		setSelectedGame (NO_GAME_SELECTED);
		gameInfoPanel = new JPanel ();
		setGameManager (aGameManager);
	}
	
	public void setSelectedGameIndex (int aSelectedGameIndex) {
		gameRadioButtons [aSelectedGameIndex].setSelected (true);
		setSelectedGame (aSelectedGameIndex);
	}
	
	public void setSelectedGame (int aSelectedGameIndex) {
		selectedGameIndex = aSelectedGameIndex;
	}
	
	@Override
	public void actionPerformed (ActionEvent e) {
		String tActionName;
		int tGameIndex;
		
		if (gameInfo != NO_GAMES) {
			tActionName = e.getActionCommand ();
			tGameIndex = getSelectedGameIndex ();
			setSelectedGame (tGameIndex);
			if (NETWORK_GAME.equals (tActionName)) {
				handleNetworkGameConnect ();
			} else {
				handleGameSelection (tGameIndex, true);
			}
		}
	}

	public void handleGameSelection (int aGameIndex, boolean aNotify) {
		JGameClient tJGameClient;
		
		setSelectedGame (aGameIndex);
		gameRadioButtons [aGameIndex].setSelected (true);
		showDescriptionAndOptions (aGameIndex);
		setGameRadioButtons (gameManager.getPlayerCount ());
		if (aNotify) {
			tJGameClient = gameManager.getNetworkJGameClient ();
			tJGameClient.handleGameSelection (aGameIndex, gameInfo [aGameIndex].getName ());
		}
	}

	public void initiateGame () {
		gameManager.initiateGame (gameInfo [selectedGameIndex]);
	}
	
	private void handleNetworkGameConnect () {
		JGameClient tNetworkGameJClient;
		String tPlayerName;
		
		tPlayerName = gameManager.getClientUserName ();
		gameManager.clearOtherPlayers (tPlayerName);
		tNetworkGameJClient = new JGameClient (CHAT_TITLE + " (" + tPlayerName + ")", gameManager);
		gameManager.setNotifyNetwork (true);
		tNetworkGameJClient.addLocalPlayer (tPlayerName, false);
		removeGamePanelButtons ();
		tNetworkGameJClient.addGamePanel (gamePanel);
	}
	
	public void addGameInfo (JPanel aGamePanel) {
		Container tBoxOfGames;
		JPanel tGameList;
		int tIndex, tGameCount;
		String tGameName;
		
		if (gameInfo != NO_GAMES) {
			gameInfoPanel = new JPanel ();
			gamePanel = aGamePanel;
			tGameCount = gameInfo.length;
			gameRadioButtons = new JRadioButton [tGameCount];
			tBoxOfGames = Box.createVerticalBox ();
			tGameList = new JPanel ();
			gameButtons = new ButtonGroup ();
			for (tIndex = 0; tIndex < tGameCount; tIndex++ ) {
				tGameName = "<html><body>" + gameInfo [tIndex].getName () + " <i>(" + gameInfo [tIndex].getMinPlayers () + "-" + gameInfo [tIndex].getMaxPlayers () + " Players)</i></body></html>";
				gameRadioButtons [tIndex] = new JRadioButton (tGameName);
				gameRadioButtons [tIndex].setEnabled (false);
				gameRadioButtons [tIndex].setToolTipText (REASON_WRONG_PLAYER_COUNT);
				gameRadioButtons [tIndex].setActionCommand (gameInfo [tIndex].getName ());
				gameRadioButtons [tIndex].addActionListener (this);
				gameButtons.add (gameRadioButtons [tIndex]);
				tBoxOfGames.add (gameRadioButtons [tIndex]);
				tBoxOfGames.add (Box.createVerticalStrut (10));
			}
			tGameList.add (tBoxOfGames);			
			listAndButtonBox = Box.createVerticalBox ();
			listAndButtonBox.add (tGameList);
			
			networkGameButton = new KButton ();
			setupButton (networkGameButton, NETWORK_GAME);
			
			newGameButton = new KButton ();
			setupButton (newGameButton, NEW_GAME);
			setEnabledGameButtons (false, REASON_NO_NEW_GAME);
			
			loadGameButton = new KButton ();
			setupButton (loadGameButton, LOAD_GAME);
			
			gamePanel.add (listAndButtonBox);
			gamePanel.add (Box.createVerticalStrut (10));
			
			showDescriptionAndOptions (NO_GAME_SELECTED);
		}
	}

	private void setupButton (KButton aButton, String aName) {
		aButton.setText (aName);
		aButton.setActionCommand (aName);
		aButton.addActionListener (this);
		listAndButtonBox.add (aButton);
	}
	
	public void removeGamePanelButtons () {
		listAndButtonBox.remove (networkGameButton);
		listAndButtonBox.remove (newGameButton);
		listAndButtonBox.remove (loadGameButton);
	}
	
	public boolean gameIsSelected () {
		boolean tGameIsSelected;
		
		tGameIsSelected = false;
		if (selectedGameIndex != NO_GAME_SELECTED) {
			tGameIsSelected = true;
		}
		
		return tGameIsSelected;
	}
	
	public GameInfo getGameByName (String aName) {
		int tIndex, tGameCount;
		GameInfo tFoundGame;
		
		tGameCount = gameInfo.length;
		tFoundGame = NO_GAME;
		if ((tGameCount > 0) && (aName != null)) {
			for (tIndex = 0; tIndex < tGameCount; tIndex++) {
				if (aName.equals (gameInfo [tIndex].getName ())) {
					tFoundGame = gameInfo [tIndex];
				}
			}
		}
		
		return tFoundGame;
	}
	
	public int getSelectedGameIndex () {
		int tIndex, tGameCount, tFoundGame;
		
		tGameCount = gameInfo.length;
		tFoundGame = NO_GAME_SELECTED;
		for (tIndex = 0; tIndex < tGameCount; tIndex++) {
			if (gameRadioButtons [tIndex].isSelected ()) {
				tFoundGame = tIndex;
			}
		}
		
		return tFoundGame;
	}
	
	public GameInfo getSelectedGame () {
		GameInfo tGameInfo;
		int tIndex;
		
		if (gameIsSelected ()) {
			tIndex = getSelectedGameIndex ();
			tGameInfo = gameInfo [tIndex];
		} else {
			tGameInfo = NO_GAME;
		}
		
		return tGameInfo;
	}
	
	public String getSelectedGameName () {
		int tIndex;
		String tGameName = "";
		
		if (gameIsSelected ()) {
			tIndex = getSelectedGameIndex ();
			tGameName = gameInfo [tIndex].getName ();
		}
		
		return tGameName;
	}
	
	@Override
	public String getTypeName () {
		return "Game Set";
	}
	
	@Override
	public void itemStateChanged (ItemEvent aItemEvent) {
		Object tSource = aItemEvent.getItemSelectable ();
		int tIndex;
		Option tOption;
		GameInfo tGameInfo;
		boolean tIsSelected;
		
		tIndex = 0;
		tGameInfo = getSelectedGame ();
		for (Object tObject : gameOptions) {
			if (tObject == tSource) {
				tIsSelected = ((JCheckBox) tSource).isSelected ();
				tOption = tGameInfo.getOptionIndex (tIndex);
				tOption.setEnabled (tIsSelected);
			}
			tIndex++;
		}
	}
	
	@Override
	public void loadXML (XMLDocument aXMLDocument) throws IOException {
//		XMLNode tXMLGameSetRoot;
		XMLElement tXMLGameSetRoot;
		
		tXMLGameSetRoot = aXMLDocument.getDocumentElement ();
		ParseGameConfig (tXMLGameSetRoot);
	}
	
	public void ParseGameConfig (XMLElement aCellNode) {
		XMLNodeList tXMLNodeList;
		NodeList tChildren;
		XMLNode tChildNode;
		String tChildName;
		int tChildrenCount;
		int tIndex;
		int tGameCount;
		
		tChildren = aCellNode.getChildNodes ();
		tChildrenCount = tChildren.getLength ();
		for (tIndex = 0; tIndex < tChildrenCount; tIndex++) {
			tChildNode = new XMLNode (tChildren.item (tIndex));
			tChildName = tChildNode.getNodeName ();
			if (EN_GAMES.equals (tChildName)) {
				tXMLNodeList = new XMLNodeList (gameInfoParsingRoutine);
				
				tGameCount = tXMLNodeList.getChildCount (tChildNode, GameInfo.EN_GAME_INFO);
				gameInfo = new GameInfo [tGameCount];
				gameIndex = 0;
				tXMLNodeList.parseXMLNodeList (tChildNode, GameInfo.EN_GAME_INFO);
			} else if (EN_NETWORK.equals (tChildName)) {
				System.out.println ("Found Tag " + EN_NETWORK);
			}
		}
	}

	ParsingRoutineI gameInfoParsingRoutine  = new ParsingRoutineI ()  {
		@Override
		public void foundItemMatchKey1 (XMLNode aChildNode) {
			gameInfo [gameIndex] = new GameInfo (aChildNode);
			gameIndex++;
		}
	};

	@Override
	public void foundItemMatchKey1 (XMLNode aChildNode) {
	}

	public void handleGameOptions (String aOptions) {
		// TODO Auto-generated method stub
	}

	private void setEnabledGameButtons (boolean aState, String aToolTipText) {
		newGameButton.setEnabled (aState);
		newGameButton.setToolTipText (aToolTipText);
	}
	
	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}
	
	public GameManager getGameManager () {
		return gameManager;
	}
	
	public void setGameRadioButtons (int aCurrentPlayerCount) {
		int tIndex, tGameCount;

		if (gameInfo != NO_GAMES) {
			tGameCount = gameInfo.length;
			for (tIndex = 0; tIndex < tGameCount; tIndex++) {
				if (gameInfo [tIndex].canPlayWithXPlayers (aCurrentPlayerCount)) {
					gameRadioButtons [tIndex].setEnabled (true);
					gameRadioButtons [tIndex].setToolTipText (NO_TIP);
					if (gameRadioButtons [tIndex].isSelected ()) {
						selectedGameIndex = tIndex;
						setEnabledGameButtons (true, NO_TIP);
					}
				} else {
					if (selectedGameIndex == tIndex) {
						selectedGameIndex = NO_GAME_SELECTED;
						setEnabledGameButtons (false, REASON_NO_NEW_GAME);
						gameButtons.clearSelection ();
					}
					gameRadioButtons [tIndex].setEnabled (false);
					gameRadioButtons [tIndex].setToolTipText (REASON_WRONG_PLAYER_COUNT);
				}
			}
		}
	}
	
	private void showDescriptionAndOptions (int aIndex) {
		String tDescription;
		int tOptionCount, tOptionIndex;
		Option tOption;
		String tOptionName;
		
		if (boxOfDescAndOptions == null) {
			boxOfDescAndOptions = Box.createVerticalBox ();
		}
		boxOfDescAndOptions.removeAll ();
		if (aIndex == NO_GAME_SELECTED) {
			if (gameDescriptionLabel == null) {
				gameDescriptionLabel = new JLabel (NO_DESCRIPTION);
				boxOfDescAndOptions.add (gameDescriptionLabel);
			} else {
				gameDescriptionLabel.setText (NO_DESCRIPTION);
			}
			tDescription = NO_DESCRIPTION;
			boxOfDescAndOptions.add (gameDescriptionLabel);
		} else {
			tDescription = gameInfo [aIndex].getHTMLDescription ();
			gameDescriptionLabel.setText (tDescription);
			boxOfDescAndOptions.add (gameDescriptionLabel);			
			tOptionCount = gameInfo [aIndex].getOptionCount ();
			gameOptions = new JCheckBox [tOptionCount];
			for (tOptionIndex = 0; tOptionIndex < tOptionCount; tOptionIndex++) {
				tOption = gameInfo [aIndex].getOptionIndex (tOptionIndex);
				tOptionName = tOption.getTitle ();
				gameOptions [tOptionIndex] = new JCheckBox (tOptionName);
				gameOptions [tOptionIndex].addItemListener (this);
				boxOfDescAndOptions.add (gameOptions [tOptionIndex]);
			}
		}
		gameInfoPanel.removeAll ();
		gameInfoPanel.add (boxOfDescAndOptions);
		gameManager.addGameInfoPanel (gameInfoPanel);
	}
}

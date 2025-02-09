package cards.network;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.net.ConnectException;

import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import cards.main.XMLFrame;
import geUtilities.xml.AttributeName;
import geUtilities.xml.ElementName;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLElement;
import swingTweaks.KButton;

public class JGameClient extends XMLFrame {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_SERVER_PORT = 52000;
	private static final String DEFAULT_REMOTE_SERVER_IP = "72.83.66.50";
	private static final String DEFAULT_LOCAL_SERVER_IP = "72.83.66.50";
	private static final String CONNECT_ACTION = "CONNECT";
	public  static final String EMPTY_GAME_ID = "";
	public  static final String NO_GAME_ID = "NOID";
	private final String ALREADY_CONNECTED = "You are already connected";
	private final String NO_TOOL_TIP = "";
	private final String NOT_CONNECTED = "You are not connected yet";
	private final String WAITING_FOR_PLAYERS = "Waiting for enough Players (4)";
	private final String WAITING_FOR_GAME = "Waiting for Game Selection";
	private final String GAME_SELECTED = "Game has been Selected, hit the button when ready to play";
	private final String WAITING_FOR_ALL = "Waiting for ALL players to be Ready";

	private static ChatServerHandler serverHandler;
	private GameSupportHandler gameSupportHandler;
	
	// Static Strings used by Client Handler - Should replace with XML Utilities handling
	public static final String GAME_ACTIVITY_TAG = "GA";
	public static final String GAME_ACTIVITY_PREFIX = "Game Activity";

	// XML Utilities Element Names and Attribute Names
	public static final ElementName EN_NETWORK_GAME = new ElementName ("NetworkGame");
	public static final ElementName EN_GAME_ACTIVITY = new ElementName ("GA");
	public static final ElementName EN_GAME_SELECTION = new ElementName ("GameSelection");
	public static final ElementName EN_PLAYER_ORDER = new ElementName ("PlayerOrder");
	public static final AttributeName AN_SERVER_IP = new AttributeName ("serverIP");
	public static final AttributeName AN_SERVER_PORT = new AttributeName ("serverPort");
	public static final AttributeName AN_GAME_INDEX = new AttributeName ("gameIndex");
	public static final AttributeName AN_GAME_OPTIONS = new AttributeName ("gameOptions");
	public static final AttributeName AN_BROADCAST_MESSAGE = new AttributeName ("broadcast");
	public static final AttributeName AN_PLAYER_ORDER = new AttributeName ("players");

	// Java Swing Objects
	private JTextPane chatText;
	private JTextPane gameActivity;
	private JTextField playerName;
	private JTextField message;
	private KButton connectButton;
	private KButton sendMessageButton;
	private KButton awayFromKeyboardAFKButton;
	private KButton disconnectButton;
	private KButton refreshPlayersButton;
	private KButton startReadyButton;
	private final ButtonGroup bgServerChoice = new ButtonGroup ();
	private JRadioButton rbLocalServer;
	private JRadioButton rbRemoteServer;
	private JScrollPane spChatText;
	private JScrollPane spGameActivity;
	private JPanel gameActivityPanel;
	private JPanel gamePanel;
//	private JPanel gameInfoPanel;
	private JList<NetworkPlayer> playerList;
	
	private HeartbeatThread heartbeatThread;
	private Thread hbeatThread;
	private Thread serverThread = null;
	private SimpleAttributeSet normal = new SimpleAttributeSet ();
	private SimpleAttributeSet iSaid = new SimpleAttributeSet ();
	
	// Game Engine Specific Objects
	private NetworkGameSupport gameManager;
	private NetworkPlayers networkPlayers;

	// Standard Java Objects
	private String serverIP;
	private int serverPort;
	private int selectedGameIndex;
	private String selectedGameName;
		
	public JGameClient (String aTitle, NetworkGameSupport aGameManager) {
		this (aTitle, aGameManager, DEFAULT_REMOTE_SERVER_IP, DEFAULT_SERVER_PORT);
	}
	
	public JGameClient (String aTitle, NetworkGameSupport aGameManager, String aServerIP, int aServerPort) {
		super (aTitle);
		
		gameManager = aGameManager;
		networkPlayers = new NetworkPlayers (aGameManager);
		setupJFrame ();
		setupActions ();
		setServerIP (aServerIP);
		setServerPort (aServerPort);
		gameSupportHandler = new GameSupportHandler (this);
		if (gameManager != null) {
			gameManager.addNewFrame (this);
			setVisible (false);
		}
	}
	
	private void setupServerInfo () {
		if (rbLocalServer.isSelected ()) {
			setServerIP (DEFAULT_LOCAL_SERVER_IP);
		} else {
			setServerIP (DEFAULT_REMOTE_SERVER_IP);
		}
		setServerPort (DEFAULT_SERVER_PORT);
	}

	private boolean setupNewPlayer (String aAction) {
		boolean tValidNewPlayer = false;
		String tPlayerName = playerName.getText ();

		tValidNewPlayer = NetworkPlayer.validPlayerName (tPlayerName);
		if (tValidNewPlayer) {
			connect (aAction);
		} else {
			appendToChat ("Player Name [" + tPlayerName + "] is not valid");
		}
		
		return tValidNewPlayer;
	}
	
	private void setupActions () {
		connectButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				String tAction = e.getActionCommand ();
				setupServerInfo ();
				setupNewPlayer (tAction);
			}
		});
		
		connectButton.addKeyListener (new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent e) {
				if (e.getKeyCode () == KeyEvent.VK_ENTER){
					String tAction = connectButton.getActionCommand ();
					setupServerInfo ();
					setupNewPlayer (tAction);
				 }
			}
		});
		
		sendMessageButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				sendMessage (e);
			}
		});
		
		sendMessageButton.addKeyListener (new KeyAdapter () {
			@Override
			public void keyReleased (KeyEvent e) {
				if (e.getKeyCode () == KeyEvent.VK_ENTER) {
					String tAction = sendMessageButton.getActionCommand ();
					sendMessage (tAction);
				 }
			}
		});
		
		refreshPlayersButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				String tAction = e.getActionCommand ();
				
				if ("REFRESH".equals (tAction)) {
					backFromAFK ();
					refreshPlayers ();
				}
			}
		});

		message.addActionListener (new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				sendMessage (e);
			}
		});
		
		awayFromKeyboardAFKButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tAction = e.getActionCommand ();
				
				if ("AFK".equals (tAction)) {
					serverHandler.sendUserIsAFK ();
					networkPlayers.setPlayerAFK (playerName.getText (), true);
					awayFromKeyboardAFKButton.setEnabled (false);
				}
			}
		});
		
		startReadyButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent aException) {
				String tAction = aException.getActionCommand ();
				
				if ("SELECT GAME".equals (tAction)) {
					sendGameSelection ();
				} else if ("READY".equals (tAction)) {
					sendPlayerReady ();
				}
				if ("START".equals (tAction)) {
					handleStartGame ();
				}
			}

		});

		disconnectButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				String tAction = e.getActionCommand ();
				
				if ("DISCONNECT".equals (tAction)) {
					serverHandler.shutdown ();
					networkPlayers.removeAllPlayers ();
					updateGameButtons ();
					setForUnconnected ();
				}
			}
		});
	}
	
	public void printButtonStatus (KButton aButton) {
		System.out.println (aButton.getActionCommand () + " Button is Enabled [" + aButton.isEnabled () + "]");		
	}
	
	public void setForUnconnected () {
		sendMessageButton.setEnabled (false);
		sendMessageButton.setToolTipText (NOT_CONNECTED);
		disconnectButton.setEnabled (false);
		disconnectButton.setToolTipText (NOT_CONNECTED);
		refreshPlayersButton.setEnabled (false);
		refreshPlayersButton.setToolTipText (NOT_CONNECTED);
		
		awayFromKeyboardAFKButton.setEnabled (false);
		awayFromKeyboardAFKButton.setToolTipText (NOT_CONNECTED);
		connectButton.setEnabled (true);
		connectButton.setToolTipText (NO_TOOL_TIP);
		connectButton.requestFocusInWindow ();
		updateReadyButton ("SELECT GAME", false, NOT_CONNECTED);
		
		message.setEnabled (false);
		message.setFocusable (false);
		rbLocalServer.setEnabled (true);
		rbLocalServer.setToolTipText (NO_TOOL_TIP);
		rbRemoteServer.setEnabled (true);
		rbRemoteServer.setToolTipText (NO_TOOL_TIP);
	}
	
	public void setForConnected () {
		connectButton.setEnabled (false);
		connectButton.setToolTipText (ALREADY_CONNECTED);
		sendMessageButton.setEnabled (true);
		sendMessageButton.setToolTipText (NO_TOOL_TIP);
		disconnectButton.setEnabled (true);
		disconnectButton.setToolTipText ("For Debugging Purposes ONLY");
		awayFromKeyboardAFKButton.setEnabled (true);
		awayFromKeyboardAFKButton.setToolTipText (NO_TOOL_TIP);
		refreshPlayersButton.setEnabled (true);
		refreshPlayersButton.setToolTipText (NO_TOOL_TIP);
		updateReadyButton ("SELECT GAME", false, WAITING_FOR_GAME);
		
		rbLocalServer.setEnabled (false);
		rbLocalServer.setToolTipText (ALREADY_CONNECTED);
		rbRemoteServer.setEnabled (false);
		rbRemoteServer.setToolTipText (ALREADY_CONNECTED);
		
		playerName.setFocusable (false);
		playerName.setEnabled (false);
		playerName.setEditable (false);
		message.setEnabled (true);
		message.setFocusable (true);
		message.requestFocusInWindow ();
	}
	
	public void startHeartbeat () {
		heartbeatThread = new HeartbeatThread (this);
		hbeatThread = new Thread (heartbeatThread);
		heartbeatThread.setContinueRunning (true);
		hbeatThread.start ();
	}

	private void setupJFrame () {

		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		// Text Fields
		playerName = new JTextField ();
		playerName.setColumns (10);
		message = new JTextField ();
		message.setActionCommand ("SEND");
		message.setColumns (80);

		// Static Labels
		JLabel lblName = new JLabel ("Name:");
		lblName.setLabelFor (playerName);
//		JLabel lblServerChoice = new JLabel ("Server");
		JLabel lblPlayers = new JLabel ("Players");
		JLabel lblMessage = new JLabel ("Message:");		
		lblMessage.setVerticalTextPosition (SwingConstants.BOTTOM);
		lblMessage.setLabelFor (message);
		
		// Radio Buttons
		rbLocalServer = new JRadioButton ("Local");
		rbLocalServer.setSelected (true);
		bgServerChoice.add (rbLocalServer);
		rbRemoteServer = new JRadioButton ("Remote");
		bgServerChoice.add (rbRemoteServer);
		
		// Action Buttons
		connectButton = new KButton (CONNECT_ACTION);
		sendMessageButton = new KButton ("SEND");
		awayFromKeyboardAFKButton = new KButton ("AFK");
		refreshPlayersButton = new KButton ("REFRESH");
		disconnectButton = new KButton("DISCONNECT");
		startReadyButton = new KButton ("SELECT GAME");

		// Text Panes and Scroll Panes
		chatText = new JTextPane ();
		chatText.setText ("Player Chat Area");
		chatText.setFocusable (true);
		chatText.setFocusTraversalKeysEnabled (false);
		chatText.setFocusCycleRoot (false);
		chatText.setEditable (false);
		
		spChatText = new JScrollPane ();
		spChatText.setAutoscrolls (true);
		spChatText.setViewportBorder (null);
		spChatText.setViewportView (chatText);
		
		gameActivityPanel = new JPanel ();
		gameActivityPanel.setBorder (new LineBorder (Color.GRAY, 1, true));
		gameActivityPanel.setPreferredSize (new Dimension (100, 100));
		
		gameActivity = new JTextPane ();
		gameActivity.setText ("Game Activity Area");
		gameActivity.setFocusable (true);
		gameActivity.setFocusTraversalKeysEnabled (false);
		gameActivity.setFocusCycleRoot (false);
		gameActivity.setEditable (false);
		
		spGameActivity = new JScrollPane ();
		spGameActivity.setAutoscrolls (true);
		spGameActivity.setViewportView (gameActivity);

		playerList = new JList<NetworkPlayer> (networkPlayers.getPlayerList ());
		playerList.setFocusable (false);
		playerList.setFocusTraversalKeysEnabled (false);
		playerList.setEnabled (false);		
				
		GroupLayout groupLayout = new GroupLayout (getContentPane ());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(playerName, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(connectButton)
							.addGap (18)
							.addComponent (startReadyButton)
							.addGap (18)
//							.addComponent(lblServerChoice)
//							.addPreferredGap(ComponentPlacement.UNRELATED)
//							.addComponent(rbLocalServer)
//							.addPreferredGap(ComponentPlacement.UNRELATED)
//							.addComponent(rbRemoteServer)
							)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMessage)
							.addGap (18)
							.addComponent(message, GroupLayout.PREFERRED_SIZE, 619, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(sendMessageButton)
							.addPreferredGap(ComponentPlacement.RELATED, 339, Short.MAX_VALUE)
							.addComponent(awayFromKeyboardAFKButton)
							.addGap (85))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(gameActivityPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
								.addComponent(spChatText, GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(38)
									.addComponent(lblPlayers, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(19)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(playerList, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
										.addComponent (disconnectButton)
										)))
							.addGap(31))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(refreshPlayersButton, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
							.addGap(40))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(playerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(connectButton)
						.addComponent (startReadyButton)
//						.addComponent(lblServerChoice)
//						.addComponent(rbLocalServer)
//						.addComponent(rbRemoteServer)
						.addComponent(lblPlayers))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(playerList, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
						.addComponent(gameActivityPanel, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(spChatText, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMessage)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(refreshPlayersButton)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(sendMessageButton)
						.addComponent(awayFromKeyboardAFKButton)
						.addComponent(disconnectButton))
					.addGap(13))
		);
		gameActivityPanel.setLayout (new BorderLayout (0, 0));
		getContentPane ().setLayout (groupLayout);
		
		setForUnconnected ();
		setSize (919, 470);
		pack ();
	}
	
	public NetworkGameSupport getGameManager () {
		return gameManager;
	}
	
	private void handleStartGame () {
		String tGameID;
		
		tGameID = getGameID ();
		serverHandler.sendUserStart (tGameID);
		startsGame ();
		gameManager.setNotifyNetwork (true); 
		gameManager.initiateNetworkGame ();
	}
	
	public void startsGame () {
		startReadyButton.setEnabled (false);
		startReadyButton.setToolTipText ("Game already started");
		swapToGameActivity ();
	}

	private void swapToGameActivity () {
		removeGamePanel ();
		addSPGameActivity ();		
	}
	
	public void removeGamePanel () {
		gameActivityPanel.remove (gamePanel);
		revalidate ();
	}
	
	public void addGamePanel (JPanel aGamePanel) {
		gameActivityPanel.add (aGamePanel, BorderLayout.WEST);
		gamePanel = aGamePanel;
		revalidate ();
	}

	public void addGameInfoPanel (JPanel aGameInfoPanel) {
		gameActivityPanel.add (aGameInfoPanel, BorderLayout.EAST);
		revalidate ();
	}

	public void removeSPGameActivity () {
		gameActivityPanel.remove (spGameActivity);
	}
	
	public void addSPGameActivity () {
		gameActivityPanel.add (spGameActivity);
		revalidate ();
	}

	private void log (String aMessage, Exception aException) {
		System.err.println (aMessage);
		aException.printStackTrace ();
    }
	
	// Server Handler Connection Routines ---
	
	private boolean connectToServer (String aPlayerName) {
		boolean tSuccess = false;
		
		try {
			serverHandler = new ChatServerHandler (serverIP, serverPort, this);
			if (serverHandler != null) {
				if (serverHandler.isConnected ()) {
					serverThread = new Thread (serverHandler);
					serverThread.start ();
					serverHandler.initializeChat (this);
					// If initializeChat method caused the Server Handler to abort 
					// for example: same name used by another client, it will stop
					// the Handler, so do NOT add the player, and return Failure here
					if (serverHandler.isConnected ()) {
						tSuccess = true;
					}
				}
			}
		} catch (ConnectException tException) {
			appendToChat ("Server Connection Failed - confirm Server is up and operational");
		} catch (Exception tException) {
			log ("Exception thrown when creating Socket to Server", tException);
		}

		return tSuccess;
	}

	private void connect (String aAction) {
		String tPlayerName;
		boolean tSuccess;
		
		if (CONNECT_ACTION.equals (aAction)) {
			tPlayerName = playerName.getText ();
			tSuccess = connectToServer (tPlayerName);
			if (tSuccess) {
				if (serverHandler != null) {
					if (serverHandler.isConnected ()) {
						setForConnected ();
						serverHandler.requestUserNameList ();
					}
				}			
			}
		}
	} 

	// --- End Server Handler Connection Routines
	
	// Message and Chat Management ---
	
	public void refreshPlayers () {
		networkPlayers.removeAllPlayers ();
		gameManager.removeAllNetworkPlayers ();
		// Request from the ServerHandler to add all of the Players
		serverHandler.requestUserNameList ();
	}
	
	public void sendGameActivity (String aGameActivity) {
		serverHandler.sendGameActivity (GAME_ACTIVITY_PREFIX + " " + aGameActivity);
	}

	public void handleGameSelection (int aGameIndex, String aGameName) {
		selectedGameIndex = aGameIndex;
		selectedGameName = aGameName;
		updateReadyButton ("SELECT GAME", true, GAME_SELECTED);
	}
	
	public void sendPlayerReady () {
		String tGameID;
		
		tGameID = getGameID ();
		serverHandler.sendUserReady (tGameID);
		sendPlayerOrder ();
	}
	
	public void sendPlayerOrder () {
		String tBroadcastMessage, tGameActivity;
		String tPlayerOrder;

		gameManager.randomizePlayerOrder ();
		tPlayerOrder = gameManager.getPlayersInOrder ();
		tBroadcastMessage = getName () + " has sent a new Player Order [" + tPlayerOrder + "]";
		tGameActivity = constructGameActivityXML (EN_PLAYER_ORDER, AN_PLAYER_ORDER, tPlayerOrder,
				AN_BROADCAST_MESSAGE, tBroadcastMessage);
		sendGameActivity (tGameActivity);
	
		updateReadyButton ("WAITING", false, WAITING_FOR_ALL);
		playerReady ();
		appendToChat ("You have sent a new Player Order [" + tPlayerOrder + "]");
	}
	
	public void sendGameSelection () {
		String tBroadcastMessage, tGameActivity;

		setTitle (selectedGameName + " Game Client Frame");
		tBroadcastMessage = getName () + " has Selected [" + selectedGameName + "] Are you ready to Play?";
		tGameActivity = constructGameActivityXML (EN_GAME_SELECTION, AN_GAME_INDEX, selectedGameIndex + "",
				AN_BROADCAST_MESSAGE, tBroadcastMessage);
		sendGameActivity (tGameActivity);
		sendPlayerReady ();
	}
	
	public void updateReadyButton (String aAction, boolean aEnabled, String aToolTip) {
		startReadyButton.setActionCommand (aAction);
		startReadyButton.setText (aAction);
		startReadyButton.setToolTipText (aToolTip);
		if (aEnabled) {
			if (gameManager.gameStarted ()) {
				startReadyButton.setEnabled (false);
				startReadyButton.setToolTipText ("Game Has Started, don't need to Start Again");
			} else {
				startReadyButton.setEnabled (true);
			}
		} else {
			startReadyButton.setEnabled (false);
		}
	}

	public String constructGameActivityXML (ElementName aElementName, 
			AttributeName aAttributeName1, int aAttributeValue1,
			AttributeName aAttributeName2, String aAttributeValue2) {
		return constructGameActivityXML (aElementName, aAttributeName1, aAttributeValue1 + "", aAttributeName2, aAttributeValue2);
	}

	public String constructGameActivityXML (ElementName aElementName, 
			AttributeName aAttributeName1, String aAttributeValue1,
			AttributeName aAttributeName2, String aAttributeValue2) {
		String tGameActivity = "";
		XMLDocument tXMLDocument = new XMLDocument ();
		XMLElement tXMLGameActivity, tXMLElement;
		
		tXMLGameActivity = tXMLDocument.createElement (EN_GAME_ACTIVITY);
		tXMLElement = tXMLDocument.createElement (aElementName);
		tXMLElement.setAttribute (aAttributeName1, aAttributeValue1);
		tXMLElement.setAttribute (aAttributeName2, aAttributeValue2);
		tXMLGameActivity.appendChild (tXMLElement);
		tXMLDocument.appendChild (tXMLGameActivity);
		
		tGameActivity = tXMLDocument.toString ();
		tGameActivity = tGameActivity.replace ("\n", "");
		
		return tGameActivity;
	}

	private void sendMessage (ActionEvent e) {
		String tAction = e.getActionCommand ();
		sendMessage (tAction);
	}
	
	private void sendMessage (String aAction) {
		if ("SEND".equals (aAction)) {
			String tMessage = message.getText ();
			
			// De-activate AFK, if it has been set
			backFromAFK ();
			
			// Send the Message to the Server
			if (tMessage.length () > 0) {
				if (tMessage.startsWith ("<GA>") && tMessage.endsWith ("</GA>")) {
					serverHandler.sendGameActivity (tMessage);
				} else {
					serverHandler.sendMessage (tMessage);
					StyleConstants.setItalic (iSaid, true);
					appendToChat ("I said: " + tMessage, iSaid);
				}
			}
			message.setText ("");
		}
	}
	
	public void appendToGameActivity (String aGameActivity) {
		try {
			Document doc = gameActivity.getDocument ();
			doc.insertString (doc.getLength (), "\n" + aGameActivity, normal);
			scroll (spGameActivity, ScrollDirection.DOWN);
		} catch (BadLocationException exc) {
			exc.printStackTrace ();
		}
	
	}
	
	public void handleGameActivity (String aGameActivity) {
		String tGameActivity;
		
		tGameActivity = aGameActivity.substring (14);
		gameManager.handleGameActivity (tGameActivity);
	}
	
	public void handleServerMessage (String tMessage) {
		String tPatternStart, tPatternEnd;
		
		tPatternStart = GAME_ACTIVITY_PREFIX + " <" + GAME_ACTIVITY_TAG + ">";
		tPatternEnd = "</" + GAME_ACTIVITY_TAG + ">";
		if (tMessage.startsWith (tPatternStart) && tMessage.endsWith (tPatternEnd)) {
			handleGameActivity (tMessage);
		} else {
			appendToChat (tMessage);
		}
	}

	public void appendToChat (String aString) {
		appendToChat (aString, normal);
	}
	
	public void appendToChat (String aString, boolean aISent) {
		if (aISent) {
			StyleConstants.setItalic (iSaid, true);
			appendToChat (aString, iSaid);
		}
	}
	
	public void appendToChat (String aString, SimpleAttributeSet aStyle) {
		try {
			Document doc = chatText.getDocument ();
			doc.insertString (doc.getLength (), "\n" + aString, aStyle);
			scroll (spChatText, ScrollDirection.DOWN);
		} catch (BadLocationException exc) {
			exc.printStackTrace ();
		}
	}

	public JTextPane getChatText () {
		return chatText;
	}

	// --- End Message and Chat Management
	
	// Client Player Management ---
		
	public void playerReady () {
		backFromAFK ();
		playerReady (getName ());
	}
	
	public void playerReady (String aPlayerName) {
		int tIndex = aPlayerName.indexOf (aPlayerName);
		String tPlayerName = aPlayerName;
		
		if (tIndex > 0) {
			tPlayerName = aPlayerName.substring (0, tIndex);
		}
		
		networkPlayers.setPlayerReady (tPlayerName, true);
		if (networkPlayers.allPlayersAreReady ()) {
			updateReadyButton ("START", true, "Hit to Start Game");
		}
	}
	
	public void addLocalPlayer (String aLocalPlayer, boolean aAutoConnect) {
		if (NetworkPlayer.validPlayerName (aLocalPlayer)) {
			playerName.setText (aLocalPlayer);
			if (aAutoConnect) {
				setupNewPlayer (CONNECT_ACTION);
				gameManager.updatePlayerCountLabel ();
			}
		}
	}
	
	private void backFromAFK () {
		if (isPlayerAFK ()) {
			serverHandler.sendUserIsNotAFK ();
		}
		resetPlayerFromeAFK ();
		awayFromKeyboardAFKButton.setEnabled (true);
	}

	public boolean isPlayerAFK () {
		return networkPlayers.playerIsAFK (getName());
	}
		
	@Override
	public String getName () {
		return getPlayerName ();
	}
	
	public String getPlayerName () {
		return playerName.getText ();
	}
	
	public JTextField getPlayerNameJTF () {
		return playerName;
	}
	
	// --- End Client Player Management ---

	// Player List Management --- 
				
	public void addPlayer (String aPlayerName) {
		networkPlayers.addPlayer (aPlayerName);
		updateGameButtons();
	}

	private void updateGameButtons () {
		// TODO Get min and max players to enable this button based on Game Info
		if (networkPlayers.getPlayerCount () >= 2) {
			gameManager.enableAllGameButtons (true);
			updateReadyButton ("SELECT GAME", true, WAITING_FOR_GAME);
		} else {
			gameManager.enableAllGameButtons (false);
			updateReadyButton ("SELECT GAME", false, WAITING_FOR_PLAYERS);
		}
	}
	
	public void setPlayerAsAFK (String aPlayerName) {
		networkPlayers.setPlayerAFK (aPlayerName, true);
	}
	
	public void resetPlayerFromeAFK () {
		resetPlayerFromAFK (playerName.getText ());
	}
	
	public void resetPlayerFromAFK (String aPlayerName) {
		networkPlayers.setPlayerAFK (aPlayerName, false);
	}
	
	public void removePlayer (String aPlayerName) {
		networkPlayers.removePlayer (aPlayerName);
		updateGameButtons();
	}
	
	public void rejectedConnect () {
		networkPlayers.removeAllPlayers ();
		setForUnconnected ();
	}
	
	// --- End Player List Management
	
	/**
	 * Scrolls a {@code scrollPane} all the way up or down.
	 *
	 * @param aScrollPane the scrollPane that we want to scroll up or down
	 * @param aDirection  we scroll up if this is {@link ScrollDirection#UP},
	 *                   or down if it's {@link ScrollDirection#DOWN}
	 */
	
	public static void scroll (JScrollPane aScrollPane, ScrollDirection aDirection) {
	    JScrollBar tVerticalBar = aScrollPane.getVerticalScrollBar ();
	    
	    // If we want to scroll to the top, set this value to the minimum,
	    // else to the maximum
	    int tTopOrBottom = aDirection == ScrollDirection.UP ?
	                      tVerticalBar.getMinimum () :
	                      tVerticalBar.getMaximum ();

	    AdjustmentListener scroller = new AdjustmentListener () {
	        @Override
	        public void adjustmentValueChanged (AdjustmentEvent tEvent) {
	            Adjustable adjustable = tEvent.getAdjustable ();
	            adjustable.setValue (tTopOrBottom);
	            // We have to remove the listener, otherwise the
	            // user would be unable to scroll afterwards
	            tVerticalBar.removeAdjustmentListener (this);
	        }
	    };
	    tVerticalBar.addAdjustmentListener (scroller);
	}

	public enum ScrollDirection { UP, DOWN }

	public XMLElement getNetworkElement (XMLDocument aXMLDocument) {
		XMLElement tXMLElement;
		
		tXMLElement = aXMLDocument.createElement (EN_NETWORK_GAME);
		tXMLElement.setAttribute (AN_SERVER_IP, getServerIP ());
		tXMLElement.setAttribute (AN_SERVER_PORT, getServerPort ());
		
		return tXMLElement;
	}

	public void setServerIP (String aServerIP) {
		serverIP = aServerIP;
	}
	
	public void setServerPort (int aServerPort) {
		serverPort = aServerPort;
	}
	
	public String getServerIP () {
		return serverIP;
	}
	
	public int getServerPort () {
		return serverPort;
	}
	
	// Need to make a Request for Game Support, then block until we get a response
	// Send the response back
	
	public String requestGameSupport (String aGameID, String aRequestGameSupport) {
		String tGSResponse;
		String tFullGSRequest;
		
		tFullGSRequest = serverHandler.buildGameSupportXML (aGameID, aRequestGameSupport);
		tGSResponse = gameSupportHandler.requestGameSupport (tFullGSRequest);
		
		return tGSResponse;
	}

	public void handleGSResponse (String aGSResponse) {
		gameSupportHandler.handleGSResponse (aGSResponse);
	}

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public String getXMLBaseDirectory () {
		return gameManager.getXMLBaseDirectory ();
	}
	
	public String getGameID () {
		return gameManager.getGameID ();
	}

	public void retrieveGameID () {
		String tGameIDRequest;
		String tGameID;
		String tResponse;
		
		tGameIDRequest = GameSupportHandler.GAME_SUPPORT_PREFIX + " <GS><GameIDRequest></GS>";
		tResponse = gameSupportHandler.requestGameSupport (tGameIDRequest);
		tGameID = gameSupportHandler.getFromResponseGameID (tResponse);
		gameManager.resetGameID (tGameID);
	}
	
	public void setGameIDonServer (String aGameID, int aLastActionNumber, String aGameName) {
		String tGameIDRequest;
		String tResponse;

		tGameIDRequest = GameSupportHandler.GAME_SUPPORT_PREFIX + " <GS><LoadGameSetup gameID=\"" + aGameID + "\" " + ""
				+ "actionNumber=\"" + aLastActionNumber + "\" gameName=\"" + aGameName + "\"></GS>";
		tResponse = gameSupportHandler.requestGameSupport (tGameIDRequest);
		System.out.println ("Request sent is [" + tGameIDRequest + "]");
		System.out.println ("Response is [" + tResponse + "]");
//		logger.info ("Request sent is [" + tGameIDRequest + "]");
//		logger.info ("Response is [" + tResponse + "]");
	}

//	2021-10-10 14:24:37.066 INFO RAW Input from Jeff [Game Support <GS><GameIDRequest></GS>]
//	2021-10-10 14:24:37.066 INFO ----- Client Jeff Last Action Number 100
//	2021-10-10 14:24:37.066 INFO Generated Response is [<GSResponse gameID="2021-10-10-1424">]
//	2021-10-10 14:24:37.067 INFO Sending to Jeff over Port 63647 the following [<GSResponse gameID="2021-10-10-1424">]
//	2021-10-10 14:24:37.067 INFO Reading for Jeff from SocketPort 63647 Thread ID 24

}
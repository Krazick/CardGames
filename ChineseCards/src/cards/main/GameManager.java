package cards.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.w3c.dom.NodeList;

import cards.actions.Action;
import cards.actions.ActionManager;
import cards.actions.ActorI;
import cards.actions.StartNewRoundAction;
import cards.config.GameFrameConfig;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;
import cards.utilities.XMLDocument;
import cards.utilities.XMLNode;

public class GameManager extends JFrame implements NetworkGameSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	private final String ENTER_USER_NAME = "Must Enter User Name";
	private final String NO_TOOL_TIP = "";
	public PlayerFrame playerFrame;
	public GameFrame gameFrame;
	public Players players;
	public ActionManager actionManager;
	private JTextField clientUserName;
	private JGameClient jGameClient;
	private JButton newGameButton;
	private JButton quitButton;
	private GamePanel gamePanel;
	private boolean notifyNetwork;
	private Long shuffleSeed;
	private Random randomGenerator;
	
	public static void main (String [] args) {
		new GameManager ();
	}
	
	public GameManager () {
		String tTitle;
		Long tSeed;
		
		tTitle = "Cards Game Startup";
		setTitle (tTitle);
		
//		createActions ();
//		addMenus ();
		
		setSize (375, 250);
		
		setFrameContents ();
		setupFrameActions ();
		setVisible (true);
		actionManager = new ActionManager (this);
		randomGenerator = new Random ();
		tSeed = randomGenerator.nextLong ();
		setShuffleSeed (tSeed);
	}

	public void setFrameContents () {
		JLabel tGameEngineTitle = new JLabel ("Cards Game Manager");
//		tGameEngineTitle.setText (resbundle.getString ("message"));
		tGameEngineTitle.setFont (new Font ("Lucida Grande", Font.BOLD, 24));
		tGameEngineTitle.setHorizontalAlignment (SwingConstants.CENTER);
		
		JLabel tGameEngineVersion = new JLabel ("Version: X.X");
		tGameEngineVersion.setText ("Version: 0.1 Alpha");
		tGameEngineVersion.setHorizontalAlignment (SwingConstants.CENTER);
		tGameEngineVersion.setFont (new Font ("Lucida Grande", Font.PLAIN, 20));
		
		JLabel tClientLabel = new JLabel ("User Name:");
		
		clientUserName = new JTextField ();
		clientUserName.setColumns (10);
		clientUserName.setEnabled (true);
		
		newGameButton = new JButton ("New Game");
		quitButton = new JButton("Quit");
		
		GroupLayout groupLayout = new GroupLayout (getContentPane ());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(114)
							.addComponent(tGameEngineVersion))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(55)
							.addComponent(tGameEngineTitle))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(57)
							.addComponent(tClientLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(clientUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(21)
							.addComponent(newGameButton)
							.addGap(140)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(quitButton)))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addComponent(tGameEngineTitle)
					.addGap(18)
					.addComponent(tGameEngineVersion)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(tClientLabel)
						.addComponent(clientUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newGameButton)
						.addComponent(quitButton))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		disableGameButtons ();
		getContentPane ().setLayout (groupLayout);

	}
	
	private void disableGameButtons () {
		newGameButton.setEnabled (false);
		newGameButton.setToolTipText (ENTER_USER_NAME);
	}
	
	public void enableGameStartItems () {
//		enableNewMenuItem ();
		newGameButton.setEnabled (true);
		newGameButton.setToolTipText (NO_TOOL_TIP);
		clientUserName.setEnabled (true);
	}

	private void setupFrameActions () {		
		clientUserName.addKeyListener (new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent e) {
				enableGameStartItems ();
			}
		});
	
		newGameButton.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				newGame ();
			}
		});
		
		newGameButton.addKeyListener (new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent e) {
				if (e.getKeyCode () == KeyEvent.VK_ENTER){
					newGame ();
				 }
			}
		});
				
		quitButton.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				System.exit (0);
			}
		});
	}
	
	public GamePanel getGamePanel () {
		return gamePanel;
	}
	
	public JGameClient getJGameClient () {
		return jGameClient;
	}
	
	public void newGame () {
		String tPlayerName;
		
		tPlayerName = getClientUserName ();
		players = new Players ();
		players.addNewPlayer (tPlayerName);
		gamePanel = new GamePanel (this, tPlayerName);
//		setupJGameClient (tPlayerName);
//		gameFrame = new GameFrame ("Game Frame for " + getClientUserName (), this);
	}

	public Players getPlayers () {
		return players;
	}
	
	public GameFrameConfig getGameFrameConfig () {
		return null;
	}

	@Override
	public void updatePlayerCountLabel () {
	}

	@Override
	public void addNetworkPlayer (String aPlayerName) {
		if (! players.hasPlayer (aPlayerName)) {
			players.addNewPlayer (aPlayerName);
		}
	}

	@Override
	public void removeNetworkPlayer (String aPlayerName) {
		players.removePlayer (aPlayerName);
	}

	@Override
	public void removeAllNetworkPlayers () {
		players.removeAll ();
	}

	@Override
	public void handleGameActivity (String aGameActivity) {
		XMLDocument tXMLGameActivity;

		XMLNode tXMLGameActivityNode;
		XMLNode tActionNode;
		NodeList tActionChildren;
		int tActionNodeCount, tActionIndex;
		String tANodeName;
		int tGameIndex;
		String tGameOptions, tBroadcast, tPlayerOrder;

		System.out.println ("-------- Game Activity [" + aGameActivity + "]");
		tXMLGameActivity = new XMLDocument ();
		tXMLGameActivity = tXMLGameActivity.ParseXMLString (aGameActivity);
		tXMLGameActivityNode = tXMLGameActivity.getDocumentElement ();
		tANodeName = tXMLGameActivityNode.getNodeName ();
		if (JGameClient.EN_GAME_ACTIVITY.equals (tANodeName)) {
			tActionChildren = tXMLGameActivityNode.getChildNodes ();
			tActionNodeCount = tActionChildren.getLength ();
			try {
				for (tActionIndex = 0; tActionIndex < tActionNodeCount; tActionIndex++) {
					tActionNode = new XMLNode (tActionChildren.item (tActionIndex));
					tANodeName = tActionNode.getNodeName ();
					if (JGameClient.EN_GAME_SELECTION.equals (tANodeName)) {
						tGameIndex = tActionNode.getThisIntAttribute (JGameClient.AN_GAME_INDEX);
						tGameOptions = tActionNode.getThisAttribute (JGameClient.AN_GAME_OPTIONS);
						tBroadcast = tActionNode.getThisAttribute (JGameClient.AN_BROADCAST_MESSAGE);
						gamePanel.handleGameSelection (tGameIndex, tGameOptions, tBroadcast);
						jGameClient.updateReadyButton ("READY", true, "Hit when ready to play");
					} else if (JGameClient.EN_PLAYER_ORDER.equals (tANodeName)) {
						tPlayerOrder = tActionNode.getThisAttribute (JGameClient.AN_PLAYER_ORDER);
						tBroadcast = tActionNode.getThisAttribute (JGameClient.AN_BROADCAST_MESSAGE);
						handleResetPlayerOrder (tPlayerOrder, tBroadcast);
					} else {
						handleNetworkAction (tXMLGameActivityNode);
					}
				}
			} catch (Exception tException) {
				System.err.println (tException.getMessage ());
				tException.printStackTrace ();
			}
		}
	}

	private void handleNetworkAction (XMLNode tXMLGameActivityNode) {
		System.out.println ("GameManager - Handle Network Game Action");
		actionManager.handleNetworkAction (tXMLGameActivityNode);
	}

	private void handleResetPlayerOrder (String tPlayerOrder, String tBroadcast) {
		players.handleResetPlayerOrder (tPlayerOrder);
	}

	@Override
	public JGameClient getNetworkJGameClient () {
		return jGameClient;
	}

	@Override
	public int getSelectedGameIndex () {
		return 0;
	}

	@Override
	public void setSelectedGameIndex (int aGameIndex) {
	}

	@Override
	public String getPlayersInOrder () {
		return players.getPlayersInOrder ();
	}

	@Override
	public void randomizePlayerOrder () {
		players.randomizePlayerOrder ();
	}

	@Override
	public void initiateNetworkGame () {
		String tGameName;
		StartNewRoundAction tStartNewRoundAction;
		ActorI tClientActor;
		
		tGameName = gamePanel.getSelectedGame ();
		gameFrame = new GameFrame (tGameName + " Game Frame for " + getClientUserName (), this);
		tClientActor = getActor (getClientUserName ());
		gameFrame.startNewRound (shuffleSeed);
		tStartNewRoundAction = new StartNewRoundAction (tClientActor);
		tStartNewRoundAction.addNewShuffleSeedEffect (tClientActor, shuffleSeed);
		tStartNewRoundAction.addInitiateGameEffect (tClientActor, true);
		System.out.println ("Start New Round Action with Shuffle Seed " + shuffleSeed);
		actionManager.addAction (tStartNewRoundAction);
		actionManager.actionReport ();
	}
	
	@Override
	public boolean gameStarted () {
		return false;
	}

	@Override
	public void addNewFrame (XMLFrame jGameClient) {
	}

	public String getClientUserName () {
		return clientUserName.getText ();
	}

	public boolean isNetworkGame () {
		return true;
	}

	public Player getClientPlayer () {
		Player tPlayer;
		
		tPlayer = players.getPlayer (getClientUserName());
		
		return tPlayer;
	}

	public void updateAllScores () {
		players.updateAllScores ();
	}

	public void handleGameWon () {
		System.out.println ("Game has ended, and XXX has lost");
	}

	public boolean gameOver () {
		boolean tGameWon = false;
		
		System.out.println ("Round Done - has anyone Exceeded 100 Points?");
		
		return tGameWon;
	}

	public void setJGameClient (JGameClient aJGameClient) {
		jGameClient = aJGameClient;
	}

	public int getPlayerCount () {
		return players.getPlayerCount ();
	}

	public void initiateGame (GameInfo aGameInfo) {
		System.out.println ("Ready to Initiate Game of " + aGameInfo.getName ());	
	}

	public void clearOtherPlayers (String tPlayerName) {
		System.out.println ("Clear Other Players with " + tPlayerName + " sent");
	}

	public void setNotifyNetwork (boolean aNotifyNetwork) {
		notifyNetwork = aNotifyNetwork;
	}
	
	public boolean shouldNotifyNetwork () {
		return notifyNetwork;
	}

	public void addGameInfoPanel (JPanel gameInfoPanel) {
		System.out.println ("Game Manager - Add Game Info Panel");
	}

	public ActorI getActor (String aActorName) {
		return players.getActor (aActorName);
	}

	public void setShuffleSeed (Long aShuffleSeed) {
		shuffleSeed = aShuffleSeed;
	}
	
	public Long getShuffleSeed () {
		return shuffleSeed;
	}

	public void setNewShuffleSeed () {
		Long tNewShuffleSeed;
		
		tNewShuffleSeed = randomGenerator.nextLong ();
		setShuffleSeed (tNewShuffleSeed);
	}

	public void appendToGameActivity (String aSimpleActionReport) {
		jGameClient.appendToGameActivity (aSimpleActionReport);
		
	}

	public String getGameName () {
		String tGameName = "UNSPECIFIED";
		if (gamePanel != null) {
			tGameName = gamePanel.getSelectedGame ();
		}
		
		return tGameName;
	}

	public String createFrameTitle (String aTitleSuffix) {
		return getGameName () + " " + aTitleSuffix;
	}

	public void updateAllFrames () {
		System.out.println ("Game Manager - Update all Frames");
	}

	public void passACard (Player aFromPlayer, Player aToPlayer, Card aCard) {
		System.out.println ("Game Manager - Pass the Card " + aCard.getAbbrev () + 
				" from " + aFromPlayer.getName () + " to " + aToPlayer.getName ());
		
	}

	public void addAction (Action aAction) {
		actionManager.addAction (aAction);
	}
}

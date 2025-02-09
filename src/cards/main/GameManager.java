package cards.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

import javax.swing.GroupLayout;
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
import cards.actions.StartNewGameAction;
import cards.config.GameFrameConfig;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;
import geUtilities.xml.XMLDocument;
import geUtilities.xml.XMLNode;
import swingTweaks.KButton;

public class GameManager extends JFrame implements NetworkGameSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	public static final String EMPTY_GAME_ID = "";
	public static final String NO_GAME_ID = "NOID";
	private final String ENTER_USER_NAME = "Must Enter User Name";
	private final String NO_TOOL_TIP = "";
//	private final int SCORE_LIMIT = 10;
	private final int SCORE_LIMIT = 100;
	public GameOverFrame gameOverFrame;
	public PlayerFrame playerFrame;
	public GameFrame gameFrame;
	public Players players;
	public ActionManager actionManager;
	private JTextField clientUserName;
	private JGameClient jGameClient;
	private KButton newGameButton;
	private KButton quitButton;
	private GamePanel gamePanel;
	private boolean notifyNetwork;
	private Long shuffleSeed;
	private Random randomGenerator;
	private int scoreLimit;
	private boolean overLimitWon;
	CardImages cardImages;
	String gameID;
	
	public static void main (String [] aArgs) {
		new GameManager (aArgs);
	}
	
	public GameManager (String [] aArgs) {
		String tTitle;
		Long tSeed;

		
		printArgs (aArgs);
		cardImages = new CardImages ();
		
		tTitle = "Cards Game Startup";
		setTitle (tTitle);
		setGameID ("2021-10-10-1010");
		
		setSize (375, 250);
		
		setFrameContents ();
		setupFrameActions ();
		setVisible (true);
		actionManager = new ActionManager (this);
		randomGenerator = new Random ();
		tSeed = randomGenerator.nextLong ();
		setShuffleSeed (tSeed);
	}

	public void printArgs (String [] aArgs) {
		if (aArgs.length > 0) {
			for (int i = 0; i < aArgs.length; i++) {
				System.out.println ("Arg " + i + " [" + aArgs [i] + "]");		
			}
		} else {
			System.out.println ("No Arguments");
		}
	}
	
	public CardImages getCardImages () {
		return cardImages;
	}
	
	public void setFrameContents () {
		String tVersionLabel = "Version 0.2.1 Alpha";
		JLabel tGameEngineTitle = new JLabel ("Cards Game Manager");

		tGameEngineTitle.setFont (new Font ("Lucida Grande", Font.BOLD, 24));
		tGameEngineTitle.setHorizontalAlignment (SwingConstants.CENTER);
		
		JLabel tGameEngineVersion = new JLabel ("Version: X.X");
		tGameEngineVersion.setText (tVersionLabel);
		tGameEngineVersion.setHorizontalAlignment (SwingConstants.CENTER);
		tGameEngineVersion.setFont (new Font ("Lucida Grande", Font.PLAIN, 20));
		
		JLabel tClientLabel = new JLabel ("User Name:");
		
		clientUserName = new JTextField ();
		clientUserName.setColumns (10);
		clientUserName.setEnabled (true);
		
		newGameButton = new KButton ("New Game");
		quitButton = new KButton("Quit");
		
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
	
		newGameButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent aEvent) {
				newGame ();
			}
		});
		
		newGameButton.addKeyListener (new KeyAdapter () {
			@Override
			public void keyReleased (KeyEvent e) {
				if (e.getKeyCode () == KeyEvent.VK_ENTER){
					newGame ();
				 }
			}
		});
				
		quitButton.addActionListener (new ActionListener () {
			@Override
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
	
	public void startNewGame () {
		hideGameOverFrame ();
		System.out.println ("Start a new Game");
		players.removeAllScores ();
		gameFrame.startNewGame ();
	}
	
	public void endGame () {
		System.out.println ("End the Game - Tell all Players this Player is Quitting");
		removeNetworkPlayer (getClientUserName ());
		System.exit (NORMAL);
	}
	
	public void newGame () {
		String tPlayerName;
		
		tPlayerName = getClientUserName ();
		players = new Players ();
		gameFrame = new GameFrame ("Game Frame for " + tPlayerName, this);
		gameFrame.setPlayers (players);
		players.addNewPlayer (tPlayerName);
		gamePanel = new GamePanel (this, tPlayerName);
		setScoreLimit (SCORE_LIMIT);
		setOverLimitWon (false);
		hideFrame ();
	}

	public void setScoreLimit (int aScoreLimit) {
		scoreLimit = aScoreLimit;
	}
	
	public int getScoreLimit () {
		return scoreLimit;
	}
	
	public void setOverLimitWon (boolean aOverLimitWon) {
		overLimitWon = aOverLimitWon;
	}
	
	public boolean getOverLimitWon () {
		return overLimitWon;
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
		int tActionNodeCount;
		int tActionIndex;
		String tANodeName;
		int tGameIndex;
		String tGameOptions;
		String tBroadcast;
		String tPlayerOrder;

		tXMLGameActivity = new XMLDocument ();
		tXMLGameActivity = tXMLGameActivity.parseXMLString (aGameActivity);
		tXMLGameActivityNode = tXMLGameActivity.getDocumentNodeFromElement ();

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
	
	@Override
	public String getXMLBaseDirectory () {
		String aBaseDirectory;

		aBaseDirectory = "Cards XML Data" + File.separator;
		
		return aBaseDirectory;
	}

	private void handleNetworkAction (XMLNode tXMLGameActivityElement) {
		actionManager.handleNetworkAction (tXMLGameActivityElement);
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
		StartNewGameAction tStartNewGameAction;
		ActorI tClientActor;
		
		tGameName = gamePanel.getSelectedGame ();
		tClientActor = getActor (getClientUserName ());
		gameFrame.setupGameFrame ();
		gameFrame.setGameFrameName (tGameName + " Game Frame for " + getClientUserName ());
		
		startNewRound ();
		
		tStartNewGameAction = new StartNewGameAction (tClientActor);
		tStartNewGameAction.addNewShuffleSeedEffect (tClientActor, shuffleSeed);
		tStartNewGameAction.addInitiateGameEffect (tClientActor, true);
		
		actionManager.addAction (tStartNewGameAction);
		actionManager.actionReport ();
	}
	
	public void finishRound () {
		gameFrame.finishRound ();
	}

	public void startNewRound () {
		gameFrame.startNewRound (shuffleSeed);
	}
	
	@Override
	public boolean gameStarted () {
		return false;
	}

	@Override
	public void addNewFrame (XMLFrame jGameClient) {
	}

	@Override
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
		Player tPlayer;
		
		tPlayer = players.getPlayerOverLimit (scoreLimit);
		if (tPlayer != Players.NO_PLAYER) {
			gameOverFrame = new GameOverFrame ("Game Over Frame", this, tPlayer, players);
			gameOverFrame.setVisible (true);
			System.out.println ("Game has ended, and XXX has lost");
			System.out.println (tPlayer.getName () + " with a score of " + tPlayer.getScore () + " is over the Score Limit of " + scoreLimit);
			if (overLimitWon) {
				System.out.println ("And has Won the Game");
			} else {
				System.out.println ("And has Lost the Game");
			}
		}
	}

	public void hideGameOverFrame () {
		gameOverFrame.setVisible (false);
	}
	
	public boolean gameOver () {
		boolean tGameOver = false;
		
		if (players.anyPlayerOverLimit (scoreLimit)) {
			tGameOver = true;
			System.out.println ("Game Over - Someone has more than " + scoreLimit + " Points");
		}
		
		return tGameOver;
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

	@Override
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
	
	public boolean isClientPlayer (String aPlayerName) {
		return (getClientUserName ().equals (aPlayerName));
	}
	
	public void hideFrame () {
		this.setVisible (false);
	}
	
	public void showFrame () {
		this.setVisible (true);
	}
	
	@Override
	public String getGameID () {
		return gameID;
	}
	
	@Override
	public void resetGameID (String aGameID) {
		if (gameID.equals ("")) {
			setGameID (aGameID);
		}
	}
	
	private void setGameID (String aGameID) {
		gameID = aGameID;
	}

	@Override
	public void enableAllGameButtons (boolean aEnable) {
		gamePanel.enableAllGameButtons (aEnable);
	}

	public void setPlayersNoCardsPlayed () {
		players.setPlayersNoCardsPlayed ();
	}
}

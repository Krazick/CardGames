package cards.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import cards.config.GameFrameConfig;
import cards.network.JGameClient;
import cards.network.NetworkGameSupport;

public class GameManager extends JFrame implements NetworkGameSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GAME_NAME = "NO-NAME";
	private final String ENTER_USER_NAME = "Must Enter User Name";
	private final String NO_TOOL_TIP = "";
	public PlayerFrame playerFrame;
	public GameFrame gameFrame;
	public Players players;
	private JTextField clientUserName;
	private JGameClient jGameClient;
	private JButton newGameButton;
	private JButton loadGameButton;
	private JButton quitButton;
	
	public static void main (String[] args) {
		new GameManager ();
	}
	
	public GameManager () {
		String tTitle;
		
		tTitle = "Hearts Game Startup";
		setTitle (tTitle);
		
//		createActions ();
//		addMenus ();
		
		setSize (375, 250);
		
		setFrameContents ();
		setupFrameActions ();
		setVisible (true);
		jGameClient = new JGameClient ("Hearts Game JGameClient", this);
	}

	public void setFrameContents () {
		JLabel tGameEngineTitle = new JLabel ("Hearts Card Game");
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
		loadGameButton = new JButton ("Load Game...");
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
							.addGap(21)
							.addComponent(loadGameButton)
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
						.addComponent(loadGameButton)
						.addComponent(quitButton))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		disableGameButtons ();
		getContentPane ().setLayout (groupLayout);

	}
	
	private void disableGameButtons () {
		newGameButton.setEnabled (false);
		newGameButton.setToolTipText (ENTER_USER_NAME);
		loadGameButton.setEnabled (false);
		loadGameButton.setToolTipText (ENTER_USER_NAME);
	}
	
	public void enableGameStartItems () {
//		enableNewMenuItem ();
//		enableOpenMenuItem ();
		newGameButton.setEnabled (true);
		newGameButton.setToolTipText (NO_TOOL_TIP);
		loadGameButton.setEnabled (true);
		loadGameButton.setToolTipText (NO_TOOL_TIP);
		clientUserName.setEnabled (true);
	}

	private void setupFrameActions () {
		clientUserName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent aEvent) {
				Object tEventObject = aEvent.getSource ();
				
				if (tEventObject instanceof JTextField) {
//					setupNewGameManager ();
				}

			}
		});
		
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
		
		loadGameButton.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
//				loadGame ();
			}
		});
		
		loadGameButton.addKeyListener (new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent e) {
				if (e.getKeyCode () == KeyEvent.VK_ENTER){
//					loadGame ();
				 }
			}
		});
		
		quitButton.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aEvent) {
				System.exit (0);
			}
		});
	}
	
	public void newGame () {
		String tPlayerName;
		
		tPlayerName = getClientUserName ();
		players = new Players ();
		players.addNewPlayer (tPlayerName);
		jGameClient.addLocalPlayer  (tPlayerName, false);
		jGameClient.setVisible (true);
		gameFrame = new GameFrame ("Game Frame for " + getClientUserName (), this);
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
		players.addNewPlayer (aPlayerName);
	}

	@Override
	public void removeNetworkPlayer (String aPlayerName) {
	}

	@Override
	public void removeAllNetworkPlayers () {
	}

	@Override
	public void handleGameActivity (String aGameActivity) {
	}

	@Override
	public JGameClient getNetworkJGameClient () {
		return null;
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
		return null;
	}

	@Override
	public void randomizePlayerOrder () {
	}

	@Override
	public void initiateNetworkGame () {
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
}

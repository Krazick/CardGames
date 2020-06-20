package cards.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cards.network.JGameClient;

public class GamePanel implements ActionListener {
	public static String NO_GAME_NAME = "NO GAME";
	private int NO_GAME_SELECTED = -1;
	GameManager gameManager;
	JGameClient jGameClient;
	JPanel gamePanel;
	ArrayList<JRadioButton> gameButtons;
	ButtonGroup gameButtonGroup;
	int selectedGameIndex;
	String selectedGameName;
	
	public GamePanel (GameManager aGameManager, String aPlayerName) {
		gameButtons = new ArrayList<JRadioButton> ();
		setGameManager (aGameManager);
		setupJGameClient (aPlayerName);
		gameManager.setJGameClient (jGameClient);
	}
	
	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}
	
	public void setupJGameClient (String aPlayerName) {		
		jGameClient = new JGameClient ("Hearts Game JGameClient", gameManager);
		gameManager.setJGameClient (jGameClient);
		jGameClient.addLocalPlayer (aPlayerName, false);
		setGamePanel (new JPanel ());
		gameButtonGroup = new ButtonGroup ();
		addGameButton ("Hearts");
		addGameButton ("Spades");
		jGameClient.addGamePanel (gamePanel);
		jGameClient.setVisible (true);
	}

	public void addGameButton (String aGameName) {
		JRadioButton tGameButton;
		
		tGameButton = new JRadioButton (aGameName);
		tGameButton.setActionCommand (aGameName);
		tGameButton.addActionListener (this);
		gameButtons.add (tGameButton);
		gameButtonGroup.add (tGameButton);
		gamePanel.add (tGameButton);
	}
	
	public void setGamePanel (JPanel aGamePanel) {
		gamePanel = aGamePanel;
	}
	
	public JPanel getGamePanel () {
		return gamePanel;
	}
	
	public void setGameButtonGroup (ButtonGroup aGameButtonGroup) {
		gameButtonGroup = aGameButtonGroup;
	}
	
	public ButtonGroup getGameButtonGroup () {
		return gameButtonGroup;
	}
	
	public String getSelectedGame () {
		String tSelectedGameName = NO_GAME_NAME;
		
		return tSelectedGameName;
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		int tGameIndex;
		
		tGameIndex = getSelectedGameIndex ();
		setSelectedGame (tGameIndex);
		handleGameSelection (tGameIndex, true);
	}

	public void handleGameSelection (int aGameIndex, boolean aNotify) {
		JGameClient tJGameClient;
		
		setSelectedGame (aGameIndex);
		if (aNotify) {
			tJGameClient = gameManager.getNetworkJGameClient ();
			tJGameClient.handleGameSelection (aGameIndex, selectedGameName);
		}
	}
	
	private void setSelectedGame (int aGameIndex) {
		selectedGameIndex = aGameIndex;
		selectedGameName = gameButtons.get (aGameIndex).getText ();
	}

	public int getSelectedGameIndex () {
		int tIndex, tGameCount, tFoundGame;
		
		tGameCount = gameButtons.size ();
		tFoundGame = NO_GAME_SELECTED;
		for (tIndex = 0; tIndex < tGameCount; tIndex++) {
			if (gameButtons.get (tIndex).isSelected ()) {
				tFoundGame = tIndex;
			}
		}
		
		return tFoundGame;
	}

	public void handleGameSelection (int aGameIndex, String aGameOptions, String aBroadcast) {
		setSelectedGame (aGameIndex);
	}
}

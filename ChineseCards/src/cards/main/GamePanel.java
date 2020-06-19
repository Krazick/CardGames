package cards.main;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cards.network.JGameClient;

public class GamePanel {
	GameManager gameManager;
	JGameClient jGameClient;
	
	public GamePanel (GameManager aGameManager, String aPlayerName) {
		setGameManager (aGameManager);
		setupJGameClient (aPlayerName);
		gameManager.setJGameClient (jGameClient);
	}
	
	public void setGameManager (GameManager aGameManager) {
		gameManager = aGameManager;
	}
	
	public void setupJGameClient (String aPlayerName) {
		JPanel tGamePanel;
		JRadioButton tHeartsGameButton;
		JRadioButton tSpadesGameButton;
		ButtonGroup tGameButtonGroup;
		
		jGameClient = new JGameClient ("Hearts Game JGameClient", gameManager);
		jGameClient.addLocalPlayer (aPlayerName, false);
		tGamePanel = new JPanel ();
		tGameButtonGroup = new ButtonGroup ();
		tHeartsGameButton = new JRadioButton ("Hearts");
		tSpadesGameButton = new JRadioButton ("Spades");
		tGameButtonGroup.add (tHeartsGameButton);
		tGameButtonGroup.add (tSpadesGameButton);
		tGamePanel.add (tHeartsGameButton);
		tGamePanel.add (tSpadesGameButton);
		jGameClient.addGamePanel (tGamePanel);
		jGameClient.setVisible (true);
	}

}

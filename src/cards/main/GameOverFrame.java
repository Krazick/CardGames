package cards.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOverFrame extends XMLFrame {

	private static final long serialVersionUID = 1L;

	public GameOverFrame (String aFrameName, GameManager aGameManager, Player aPlayer, Players aPlayers) {
		super (aFrameName);
		
		JLabel tGameNameLabel;
		JLabel tPlayerOverLabel;
		JLabel tAPlayerLabel;
		JButton tQuitButton;
		JButton tNewGameButton;
		JPanel tButtonsPanel;
		Player tAPlayer;
		String tPlayerOverString;
		int tPlayerCount;
		
		tGameNameLabel = new JLabel ("The " + aGameManager.getGameName() + " Game is Over", JLabel.CENTER);
		tPlayerOverString = aPlayer.getName () + " is over " + aGameManager.getScoreLimit() + 
				" with " + aPlayer.getScore () + " Points and has ";
		if (aGameManager.getOverLimitWon ()) {
			tPlayerOverString += " won the Game";
		} else {
			tPlayerOverString += " lost the Game";
		}
		tPlayerOverLabel = new JLabel (tPlayerOverString, JLabel.CENTER);
		
		setLayout (new BoxLayout (this.getContentPane (), BoxLayout.PAGE_AXIS));
		add (Box.createVerticalStrut (30));
		add (tGameNameLabel);
		add (Box.createVerticalStrut (30));
		add (tPlayerOverLabel);
		add (Box.createVerticalStrut (30));
		
		tPlayerCount = aPlayers.getPlayerCount ();
		for (int tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tAPlayer = aPlayers.getPlayer(tPlayerIndex);
			if (! tAPlayer.getName ().equals(aPlayer.getName ())) {
				tAPlayerLabel = new JLabel (tAPlayer.getName () + " has " + tAPlayer.getScore () + " Points");
				add (tAPlayerLabel);
				add (Box.createVerticalStrut (30));
			}
		}
		tNewGameButton = new JButton ("New Game");
		tNewGameButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent aEvent) {
				aGameManager.startNewGame ();
			}
		});
		
		tQuitButton = new JButton ("Quit");
		tQuitButton.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent aEvent) {
				// Tell Other Players to Quit
				aGameManager.endGame ();
			}
		});

		tButtonsPanel = new JPanel ();
		tButtonsPanel.setLayout (new BoxLayout (tButtonsPanel, BoxLayout.LINE_AXIS));
		tButtonsPanel.add (tNewGameButton);
		tButtonsPanel.add (tQuitButton);
		add (Box.createVerticalStrut (30));
		add (tButtonsPanel);
		add (Box.createVerticalStrut (30));
		setSize (500, 700);
	}

}

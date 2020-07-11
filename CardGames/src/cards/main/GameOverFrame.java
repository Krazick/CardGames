package cards.main;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GameOverFrame extends XMLFrame {

	private static final long serialVersionUID = 1L;

	public GameOverFrame (String aFrameName, GameManager aGameManager, Player aPlayer, Players aPlayers) {
		super (aFrameName);
		
		JLabel tGameNameLabel;
		JLabel tPlayerOverLabel;
		JLabel tAPlayerLabel;
		JButton tQuitButton;
		JButton tNewGameButton;
		Player tAPlayer;
		String tPlayerOverString;
		int tPlayerCount;
		
		tGameNameLabel = new JLabel (aGameManager.getGameName() + " is Over");
		tPlayerOverString = aPlayer.getName () + " is over " + aGameManager.getScoreLimit() + 
				" with " + aPlayer.getScore () + " Points and has ";
		if (aGameManager.getOverLimitWon ()) {
			tPlayerOverString += " won the Game";
		} else {
			tPlayerOverString += " lost the Game";
		}
		tPlayerOverLabel = new JLabel (tPlayerOverString);
		
		setLayout (new BoxLayout (this.getContentPane(), BoxLayout.PAGE_AXIS));
		add (tGameNameLabel);
		add (tPlayerOverLabel);
		
		tPlayerCount = aPlayers.getPlayerCount ();
		for (int tPlayerIndex = 0; tPlayerIndex < tPlayerCount; tPlayerIndex++) {
			tAPlayer = aPlayers.getPlayer(tPlayerIndex);
			if (! tAPlayer.getName ().equals(aPlayer.getName ())) {
				tAPlayerLabel = new JLabel (tAPlayer.getName () + " Score: " + tAPlayer.getScore ());
				add (tAPlayerLabel);
			}
		}
		tNewGameButton = new JButton ("New Game");
		tQuitButton = new JButton ("Quit");
		add (tNewGameButton);
		add (tQuitButton);
	}

}

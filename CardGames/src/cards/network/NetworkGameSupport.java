package cards.network;

import cards.main.XMLFrame;

public interface NetworkGameSupport {
	public final String NO_NAME = "";

	public void updatePlayerCountLabel ();
	public void addNetworkPlayer (String aPlayerName);
	public void removeNetworkPlayer (String aPlayerName);
	public void removeAllNetworkPlayers ();
	
	public void handleGameActivity (String aGameActivity);
	
	public JGameClient getNetworkJGameClient ();
	
	public int getSelectedGameIndex ();
	public void setSelectedGameIndex (int aGameIndex);
	public String getPlayersInOrder ();
	public void randomizePlayerOrder ();
	public void initiateNetworkGame ();
	public boolean gameStarted ();
	public void addNewFrame (XMLFrame jGameClient);
	public void setNotifyNetwork (boolean aNotifyNetwork);
}

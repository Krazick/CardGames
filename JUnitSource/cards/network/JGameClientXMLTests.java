package cards.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("JGameClient XML Tests")
public class JGameClientXMLTests {

	JGameClient jGameClient;
	
	@BeforeEach 
	public void setUp () {
		jGameClient = new JGameClient ("JGameClient Testing Frame", null);
	}
	
	@Test
	@DisplayName ("Test Game Activity Generation")
	public void JGameClient_XMLTests1 () {
		int tSelectedGameIndex = 0;
		String tBroadcastMessage = "This is a Broadcast Message";		
		String tGameActivity2, tGameActivity3;
		String tExpected1 = "<GA><GameSelection broadcast=\"This is a Broadcast Message\" gameIndex=\"0\"/></GA>";
				
		tGameActivity2 = jGameClient.constructGameActivityXML (JGameClient.EN_GAME_SELECTION, 
				JGameClient.AN_GAME_INDEX, tSelectedGameIndex + "",
				JGameClient.AN_BROADCAST_MESSAGE, tBroadcastMessage);
		assertEquals (tExpected1, tGameActivity2);
		
		tGameActivity3 = jGameClient.constructGameActivityXML (JGameClient.EN_GAME_SELECTION, 
				JGameClient.AN_GAME_INDEX, tSelectedGameIndex,
				JGameClient.AN_BROADCAST_MESSAGE, tBroadcastMessage);
		assertEquals (tExpected1, tGameActivity3);
	}
}
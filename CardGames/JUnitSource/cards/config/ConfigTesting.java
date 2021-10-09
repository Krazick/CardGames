package cards.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cards.main.GameManager;

@DisplayName ("Config Class Testing")
class ConfigTesting {

	@Test
	@DisplayName ("Verify Config Constructors")
	void testConfigConstructor () {
		GameManager tGameManager;
		Config tConfig;
		String [] tArgs = new String []  {};
		
		tGameManager = new GameManager (tArgs);
		tConfig = new Config (tGameManager);
		
		assertEquals (0, tConfig.getGameFramesCount());
	}

}

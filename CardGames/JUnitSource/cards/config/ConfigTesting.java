package cards.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cards.main.GameManager;

@DisplayName ("Config Class Testing")
class ConfigTesting {

	@Test
	@DisplayName ("Verify Config Constructors")
	void testConfigConstructor () {
		GameManager tGameManager;
		Config tConfig;
		
		tGameManager = new GameManager ();
		tConfig = new Config (tGameManager);
		
		assertEquals (0, tConfig.getGameFramesCount());
	}

}

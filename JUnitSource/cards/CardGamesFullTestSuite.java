package cards;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName ("Card Games Package Test Suite")
@SelectPackages ({ "cards.actions", "cards.config", "cards.effects", "cards.main", "cards.network" })

class CardGamesFullTestSuite {


}

package io.turing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class StatTrackerTest {
	
	@Test
	void testForStatTrackerObject() {
		String file1 = "/Users/earltstephens/eclipse-workspace/futbol/game_teams.csv";
		String file2 = "/Users/earltstephens/eclipse-workspace/futbol/games.csv";
		String file3 = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		StatTracker statTracker = new StatTracker(file1, file2, file3);
		
		assertNotNull(statTracker);
	}

	@Test
	void testThatParserCreatesAnArrayOfHashes() {
		String file1 = "/Users/earltstephens/eclipse-workspace/futbol/game_teams.csv";
		String file2 = "/Users/earltstephens/eclipse-workspace/futbol/games.csv";
		String file3 = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		StatTracker statTracker = new StatTracker(file1, file2, file3);
		
		assertEquals(32, statTracker.teams.size());
		assertEquals(14882, statTracker.game_teams.size());
		assertEquals(7441, statTracker.games.size());
	}
}

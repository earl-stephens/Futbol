package io.turing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StatTrackerTest {
	
	@Test
	void testForStatTrackerObject() {
		StatTracker statTracker = new StatTracker("File1", "File2", "File3");
		
		assertNotNull(statTracker);
	}

	@Test
	void testThatParserCreatesAnArrayOfHashes() {
		String file1 = "game_teams.csv";
		String file2 = "games.csv";
		String file3 = "teams.csv";
		StatTracker statTracker = new StatTracker(file1, file2, file3);
		
		assertEquals(32, statTracker.teams.size());
	}
}

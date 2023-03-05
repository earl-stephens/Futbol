package io.turing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatTrackerTest {
	static StatTracker statTracker;
	static StatTracker statTracker2;

	@BeforeAll
	static void setUp() {
		String file1 = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2 = "/Users/earltstephens/eclipse-workspace/futbol/games_test.csv";
		String file3 = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker = new StatTracker(file1, file2, file3);
	}

	@Test
	void testForStatTrackerObject() {

		assertNotNull(statTracker);
	}

	@Test
	void testThatParserCreatesAnArrayOfHashes() {
		assertEquals(32, statTracker.teams.size());
		assertEquals(67, statTracker.game_teams.size());
		assertEquals(58, statTracker.games.size());
	}

	@Test
	void testHighestTotalScore() {
		assertEquals(9, statTracker.highestTotalScore());
	}

	@Test
	void testLowestTotalScore() {
		assertEquals(1, statTracker.lowestTotalScore());
	}

	@Test
	void testPercentHomeGamesWon() {
		assertEquals(34.48, statTracker.percentHomeGamesWon(), 0.2);
	}

	@Test
	void testPercentVisitorGamesWon() {
		assertEquals(32.76, statTracker.percentVisitorGamesWon(), 0.2);
	}

	@Test
	void testPercentTieGames() {
		assertEquals(32.76, statTracker.percentTieGames(), 0.2);
	}

	@Test
	void testAverageGoalsPerGame() {
		assertEquals(4.45, statTracker.averageGoalsPerGame(), 0.2);
	}

	@Test
	void testCountOfGamesBySeason() {
		assertEquals(26, Integer.valueOf(statTracker.countOfGamesBySeason().get("20142015")));
		assertEquals(11, Integer.valueOf(statTracker.countOfGamesBySeason().get("20142016")));
		assertEquals(21, Integer.valueOf(statTracker.countOfGamesBySeason().get("20142017")));
	}

	@Test
	void testAverageGoalsBySeason() {
		assertEquals("4.35", statTracker.averageGoalsBySeason().get("20142015"));
		assertEquals("4.73", statTracker.averageGoalsBySeason().get("20142016"));
		assertEquals("4.33", statTracker.averageGoalsBySeason().get("20142017"));
	}
	
	@Test
	void testCountOfTeams() {
		assertEquals(32, statTracker.countOfTeams());
	}
	
	@Test
	void testBestOffense() {
		assertEquals("Los Angeles FC", statTracker.bestOffense());
	}
	
	@Test
	void testWorstOffense() {
		assertEquals("Seattle Sounders FC", statTracker.worstOffense());
	}
	
	@Test
	void testHighestScoringVisitor() {
		assertEquals("Los Angeles FC", statTracker.highestScoringVisitor());
	}
	
	@Test
	void testLowestScoringVisitor() {
		assertEquals("Philadelphia Union", statTracker.lowestScoringVisitor());
	}
	
	@Test
	void testHighestScoringHomeTeam() {
		assertEquals("Houston Dash", statTracker.highestScoringHomeTeam());
	}
	
	@Test
	void testLowestScoringHomeTeam() {
		assertEquals("Portland Timbers", statTracker.lowestScoringHomeTeam());
	}
	
	@Test
	void testWinningestCoach() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("Randy Carlyle", statTracker2.winningestCoach("20162017"));
	}
	
	@Test
	void testWorstCoach() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("Mike Yeo", statTracker2.worstCoach("20172018"));
	}
	
	@Test
	void testMostTackles() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("Real Salt Lake", statTracker2.mostTackles("20162017"));
	}
}

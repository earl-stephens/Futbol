package io.turing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatTrackerTest {
	static StatTracker statTracker;
	static StatTracker statTracker2;
	static StatTracker statTracker3;

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
	
	@Test
	void testFewestTackles() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("Los Angeles FC", statTracker2.fewestTackles("20162017"));
	}
	
	@Test
	void testMostAccurateTeam() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("Philadelphia Union", statTracker2.mostAccurateTeam("20162017"));
	}
	
	@Test
	void testLeastAccurateTeam() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("New York Red Bulls", statTracker2.leastAccurateTeam("20162017"));
	}
	
	@Test
	void testTeamInfo() {
		Map<String, String> expected = new HashMap<>();
		expected.put("team_id", "18");
		expected.put("franchise_id", "34");
		expected.put("team_name", "Minnesota United FC");
		expected.put("abbreviation", "MIN");
		expected.put("link", "/api/v1/teams/18");
		
		assertEquals("34", statTracker.teamInfo("18").get("franchise_id"));
	}
	
	@Test
	void testBestSeason() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("20172018", statTracker2.bestSeason("4"));
		assertEquals("20172018", statTracker2.bestSeason("19"));
	}
	
	@Test
	void testWorstSeason() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_2.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker2 = new StatTracker(file1a, file2a, file3a);
		assertEquals("20152016", statTracker2.worstSeason("4"));
	}
	
	@Test
	void testAverageWinPercentage() {
		assertEquals(0.6, statTracker.averageWinPercentage("30"), 0.2);
	}
	
	@Test
	void testMostGoalsScored() {
		assertEquals(4, statTracker.mostGoalsScored("19"));
		assertEquals(6, statTracker.mostGoalsScored("13"));
		assertNotEquals(5, statTracker.mostGoalsScored("10"));
	}
	
	@Test
	void testFewestGoalsScored() {
		assertEquals(2, statTracker.fewestGoalsScored("4"));
		assertEquals(0, statTracker.fewestGoalsScored("19"));
		assertNotEquals(5, statTracker.mostGoalsScored("10"));
	}
	
	@Test
	void testFavoriteOpponent() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_3.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker3 = new StatTracker(file1a, file2a, file3a);
		assertEquals("DC United", statTracker3.favoriteOpponent("3"));
	}
	
	@Test
	void testRival() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_3.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker3 = new StatTracker(file1a, file2a, file3a);
		assertEquals("FC Dallas", statTracker3.rival("3"));
	}
	
	@Test
	void testBiggestTeamBlowout() {
		String file1a = "/Users/earltstephens/eclipse-workspace/futbol/game_teams_test.csv";
		String file2a = "/Users/earltstephens/eclipse-workspace/futbol/games_test_3.csv";
		String file3a = "/Users/earltstephens/eclipse-workspace/futbol/teams.csv";
		statTracker3 = new StatTracker(file1a, file2a, file3a);
		assertEquals(5, statTracker3.biggestTeamBlowout("3"));
		assertNotEquals(6, statTracker3.biggestTeamBlowout("3"));
	}
}

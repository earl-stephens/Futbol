package io.turing;

public class Futbol {

	public static void main(String[] args) {

		String game_teams = "game_teams.csv";
		String games = "games.csv";
		String teams = "teams.csv";
		
		StatTracker statTracker =  new StatTracker(game_teams, games, teams);
		
	}

}

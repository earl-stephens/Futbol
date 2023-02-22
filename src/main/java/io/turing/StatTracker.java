package io.turing;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class StatTracker {

	// Use List as the outer collection because the size of the csv file can change
	// Use String[] as the inner collection since the number of headers in the
	// csv file is unlikely to change
	public List<String[]> game_teams;
	public List<String[]> games;
	public List<String[]> teams;
	Set<String> seasonNames = new HashSet<>();
	HashMap<String, List<Integer>> intermediateHash = new HashMap<>();

	public StatTracker(String file1Location, String file2Location, String file3Location) {
		game_teams = parseFile(file1Location, game_teams);
		games = parseFile(file2Location, games);
		teams = parseFile(file3Location, teams);
	}

	public List<String[]> parseFile(String fileLocation, List<String[]> listName) {
		try {
			FileReader fileReader = new FileReader(fileLocation);
			CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
			listName = csvReader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listName;
	}

	public int highestTotalScore() {
		int highestTotalScore = 0;
		for (String[] game : games) {
			int sum = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
			if (sum > highestTotalScore) {
				highestTotalScore = sum;
			}
		}
		return highestTotalScore;
	}

	public int lowestTotalScore() {
		int lowestTotalScore = 1000;
		for (String[] game : games) {
			int sum = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
			if (sum < lowestTotalScore) {
				lowestTotalScore = sum;
			}
		}
		return lowestTotalScore;
	}

	public double percentHomeGamesWon() {
		double percentage;
		int totalNumberOfGames = games.size();
		int homeWins = 0;

		for (String[] game : games) {
			if (Integer.valueOf(game[7]) > Integer.valueOf(game[6])) {
				++homeWins;
			}
		}

		percentage = ((double) homeWins / totalNumberOfGames) * 100;
		return percentage;
	}

	public double percentVisitorGamesWon() {
		double percentage;
		int totalNumberOfGames = games.size();
		int visitorWins = 0;

		for (String[] game : games) {
			if (Integer.valueOf(game[6]) > Integer.valueOf(game[7])) {
				++visitorWins;
			}
		}

		percentage = ((double) visitorWins / totalNumberOfGames) * 100;
		return percentage;
	}

	public double percentTieGames() {
		double percentage;
		int totalNumberOfGames = games.size();
		int ties = 0;

		for (String[] game : games) {
			if (Integer.valueOf(game[6]) == Integer.valueOf(game[7])) {
				++ties;
			}
		}

		percentage = ((double) ties / totalNumberOfGames) * 100;
		return percentage;
	}

	public double averageGoalsPerGame() {
		double average;
		int totalNumberOfGames = games.size();
		int totalGoals = 0;

		for (String[] game : games) {
			totalGoals += (Integer.valueOf(game[6]) + Integer.valueOf(game[7]));
		}

		average = (double) totalGoals / totalNumberOfGames;
		return average;
	}

	public Map<String, String> countOfGamesBySeason() {
		Map<String, String> countOfGamesBySeason = new HashMap<>();
		for (String[] game : games) {
			if (countOfGamesBySeason.containsKey(game[1])) {
				int gameCounter = Integer.parseInt(countOfGamesBySeason.get(game[1]));
				gameCounter++;
				countOfGamesBySeason.put(game[1], String.valueOf(gameCounter));
			} else {
				countOfGamesBySeason.put(game[1], "1");
			}
		}
		return countOfGamesBySeason;
	}

	public Map<String, String> averageGoalsBySeason() {
		Map<String, String> averageGoalsBySeason = new HashMap<>();
		getSeasonNamesAndIntermediatehash();
		String[] seasonArray = seasonNames.toArray(new String[seasonNames.size()]);
		
		for (int i = 0; i < seasonArray.length; i++) {
			int sum = 0;
			for (int seasonGoals : intermediateHash.get(seasonArray[i])) {
				sum += seasonGoals;
			}
			double average = sum / (double) intermediateHash.get(seasonArray[i]).size();
			DecimalFormat df = new DecimalFormat("0.##");
			averageGoalsBySeason.put(seasonArray[i], df.format(average));
		}
		return averageGoalsBySeason;
	}
	
	public void getSeasonNamesAndIntermediatehash() {
		for (String[] game : games) {
			seasonNames.add(game[1]);
			if (intermediateHash.containsKey(game[1])) {
				int goals = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
				intermediateHash.get(game[1]).add(goals);
			} else {
				int goals = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
				List<Integer> goalList = new ArrayList<>();
				goalList.add(goals);
				intermediateHash.put(game[1], goalList);
			}
		}
	}
	
	public int countOfTeams() {
		return teams.size();
	}
}

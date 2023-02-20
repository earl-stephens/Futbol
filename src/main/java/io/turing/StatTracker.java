package io.turing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class StatTracker {

	// Use List as the outer collection because the size of the csv file can change
	// Use String[] as the inner collection since the number of headers in the
	// csv file is unlikely to change
	public List<String[]> game_teams;
	public List<String[]> games;
	public List<String[]> teams;

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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listName;
	}
	
	public int highestTotalScore() {
		int highestTotalScore = 0;
		for(String[] game : games) {
			int sum = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
			if(sum > highestTotalScore) {
				highestTotalScore = sum;
			}
		}
		return highestTotalScore;
	}
	
	public int lowestTotalScore() {
		int lowestTotalScore = 1000;
		for(String[] game : games) {
			int sum = Integer.valueOf(game[6]) + Integer.valueOf(game[7]);
			if(sum < lowestTotalScore) {
				lowestTotalScore = sum;
			}
		}
		return lowestTotalScore;
	}
	
	public double percentHomeGamesWon() {
		double percentage;
		int totalNumberOfGames = games.size();
		int homeWins = 0;

		for(String[] game : games) {
			if(Integer.valueOf(game[7]) > Integer.valueOf(game[6])) {
				++homeWins;
			}
		}
		
		percentage = ((double)homeWins / totalNumberOfGames) * 100;
		return percentage;
	}
	
	public double percentVisitorGamesWon() {
		double percentage;
		int totalNumberOfGames = games.size();
		int visitorWins = 0;
		
		for(String[] game : games) {
			if(Integer.valueOf(game[6]) > Integer.valueOf(game[7])) {
				++visitorWins;
			}
		}
		
		percentage = ((double)visitorWins / totalNumberOfGames) * 100;
		return percentage;
	}
	
	public double percentTieGames() {
		double percentage;
		int totalNumberOfGames = games.size();
		int ties = 0;
		
		for(String[] game : games) {
			if(Integer.valueOf(game[6]) == Integer.valueOf(game[7])) {
				++ties;
			}
		}
		
		percentage = ((double)ties / totalNumberOfGames) * 100;
		return percentage;
	}
	
	public double averageGoalsPerGame() {
		double average;
		int totalNumberOfGames = games.size();
		int totalGoals = 0;
		
		for(String[] game : games) {
			totalGoals += (Integer.valueOf(game[6]) + Integer.valueOf(game[7]));
		}
		
		average = (double)totalGoals / totalNumberOfGames;
		return average;
	}
	
	public HashMap<String, String> countOfGamesBySeason() {
		HashMap<String, String> countOfGamesBySeason = new HashMap<>();
		for(String[] game : games) {
			if(countOfGamesBySeason.containsKey(game[1])) {
				int gameCounter = Integer.valueOf(countOfGamesBySeason.get(game[1]));
				gameCounter++;
				countOfGamesBySeason.put(game[1], String.valueOf(gameCounter));
			} else {
				countOfGamesBySeason.put(game[1], "1");
			}
		}
		return countOfGamesBySeason;
	}
}

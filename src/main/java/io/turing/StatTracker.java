package io.turing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
}

package io.turing;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

	public String bestOffense() {
		String name = getBestOffenseTeamID();
		return getTeamNameFromId(name);
	}

	public String worstOffense() {
		String name = getWorstOffenseTeamID();
		return getTeamNameFromId(name);
	}

	private String getBestOffenseTeamID() {
		Map<String, double[]> averageHash = createAverageHash();
		return getBestAndWorst(averageHash, ">");
	}

	private String getWorstOffenseTeamID() {
		Map<String, double[]> averageHash = createAverageHash();
		return getBestAndWorst(averageHash, "<");
	}
	
	private Map<String, double[]> createAverageHash() {
		Map<String, double[]> averageHash = new HashMap<>();
		for (String[] game : game_teams) {
			if (averageHash.containsKey(game[1])) {
				double updatedAverage = calculateUpdatedAverage(averageHash, game);
				double[] tempArray = setTempArray(updatedAverage, averageHash, game);
				averageHash.replace(game[1], tempArray);
			} else {
				double[] newTempArray = setNewTempArray(game);
				averageHash.put(game[1], newTempArray);
			}
		}
		return averageHash;
	}

	private double calculateUpdatedAverage(Map<String, double[]> averageHash, String[] game) {
		return (averageHash.get(game[1])[1] * averageHash.get(game[1])[0] + Integer.parseInt(game[6]))
				/ (averageHash.get(game[1])[1] + 1);
	}

	private double[] setTempArray(double updatedAverage, Map<String, double[]> averageHash, String[] game) {
		double[] tempArray = new double[2];
		tempArray[0] = updatedAverage;
		tempArray[1] = averageHash.get(game[1])[1] + 1;
		return tempArray;
	}

	private double[] setNewTempArray(String[] game) {
		double[] newTempArray = new double[2];
		newTempArray[0] = Double.parseDouble(game[6]);
		newTempArray[1] = 1.0;
		return newTempArray;
	}

	private String getBestAndWorst(Map<String, double[]> averageHash, String bestOrWorst) {
		double highestAverage = 0.0;
		double lowestAverage = 1000.0;
		String teamId = null;
		Set<String> keyMap = averageHash.keySet();
		for (String key : keyMap) {
			if (bestOrWorst.equals(">")) {
				if (averageHash.get(key)[0] > highestAverage) {
					highestAverage = averageHash.get(key)[0];
					teamId = key;
				}
			}
			if (bestOrWorst.equals("<")) {
				if (averageHash.get(key)[0] < lowestAverage) {
					lowestAverage = averageHash.get(key)[0];
					teamId = key;
				}
			}
		}
		return teamId;
	}

	public String highestScoringVisitor() {
		Map<String, double[]> averageHash = createAverageHashForVisitorsAndHome("away");
		String idToSearchForName = getBestAndWorst(averageHash, ">");
		return getTeamNameFromId(idToSearchForName);
	}
	
	public String lowestScoringVisitor() {
		Map<String, double[]> averageHash = createAverageHashForVisitorsAndHome("away");
		String idToSearchForName = getBestAndWorst(averageHash, "<");
		return getTeamNameFromId(idToSearchForName);
	}
	
	private Map<String, double[]> createAverageHashForVisitorsAndHome(String visitorOrHome) {
		Map<String, double[]> averageHash = new HashMap<>();
		for (String[] game : game_teams) {
			if (averageHash.containsKey(game[1]) && game[2].equals(visitorOrHome)) {
				double updatedAverage = calculateUpdatedAverage(averageHash, game);
				double[] tempArray = setTempArray(updatedAverage, averageHash, game);
				averageHash.replace(game[1], tempArray);
			} else if (!averageHash.containsKey(game[1]) && game[2].equals(visitorOrHome)) {
				double[] newTempArray = setNewTempArray(game);
				averageHash.put(game[1], newTempArray);
			}
		}
		return averageHash;
	}

	private String getTeamNameFromId(String id) {
		String teamName = "";
		for (String[] team : teams) {
			if (team[0].equals(id)) {
				teamName = team[2];
			}
		}
		return teamName;
	}
	
	public String highestScoringHomeTeam() {
		Map<String, double[]> averageHash = createAverageHashForVisitorsAndHome("home");
		String idToSearchForName = getBestAndWorst(averageHash, ">");
		return getTeamNameFromId(idToSearchForName);
	}
	
	public String lowestScoringHomeTeam() {
		Map<String, double[]> averageHash = createAverageHashForVisitorsAndHome("home");
		String idToSearchForName = getBestAndWorst(averageHash, "<");
		return getTeamNameFromId(idToSearchForName);
	}
	
	private List<String[]> gamesForSelectedSeason(String season) {
		List<String[]> seasonList = new ArrayList<>();
		for(String[] selectedSeason : games) {
			if(selectedSeason[1].equals(season)) {
				seasonList.add(selectedSeason);
			}
		}
		return seasonList;
	}
	private List<String[]> pullGameTeamsFromGames(List<String[]> seasonList) {
		List<String[]> seasonGames = new ArrayList<>();
		for(String[] gamesInSeason : seasonList) {
			for(String[] game_team : game_teams) {
				if(gamesInSeason[0].equals(game_team[0])) {
					seasonGames.add(game_team);
				}
			}
		}
		return seasonGames;
	}
	
	private Map<String, double[]> createWinLossAverageHash(List<String[]> seasonGames, String winOrLoss) {
		Map<String, double[]> averageHash = new HashMap<>();
		for(String[] selectedGame : seasonGames) {
			if(averageHash.containsKey(selectedGame[1]) && selectedGame[3].equals(winOrLoss)) {
				double updatedAverage = (averageHash.get(selectedGame[1])[0] * averageHash.get(selectedGame[1])[1] + 1) / (averageHash.get(selectedGame[1])[0] + 1);
				double[] tempArray = new double[2];
				tempArray[0] = updatedAverage;
				tempArray[1] = averageHash.get(selectedGame[1])[1] + 1;
				averageHash.replace(selectedGame[1], tempArray);
			} else if(!averageHash.containsKey(selectedGame[1]) && selectedGame[3].equals(winOrLoss)) {
				double[] newTempArray = new double[2];
				newTempArray[0] = 1;
				newTempArray[1] = 1;
				averageHash.put(selectedGame[1], newTempArray);
			}
		}
		return averageHash;
	}
	
	private String getCoachFromTeamId(List<String[]> seasonGames, String teamId) {
		String coach = "";
		for(String[] team : seasonGames) {
			if(team[1].equals(teamId)) {
				coach = team[5];
			}
		}
		return coach;
	}
	
	public String winningestCoach(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		
		//iterate through loop and calculate average
		Map<String, double[]> averageHash = createWinLossAverageHash(seasonGames, "WIN");
		
		//get team id with highest percentage
		String teamId = getBestAndWorst(averageHash, ">");
		
		//pull from seasonGames, since it is only the games for the selected season
		return getCoachFromTeamId(seasonGames, teamId);
	}
	
	public String worstCoach(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		
		//iterate through loop and calculate average
		Map<String, double[]> averageHash = createWinLossAverageHash(seasonGames, "LOSS");

		//get team id with highest percentage
		String teamId = getBestAndWorst(averageHash, ">");
		
		//pull from seasonGames, since it is only the games for the selected season
		return getCoachFromTeamId(seasonGames, teamId);
	}
	
	private String getTeamIdFromNumberOfTackles(Map<String, Integer> hashForTackles, String mostOrFewest) {
		int highestNumberOfTackles = 0;
		int lowestNumberOfTackles = 1000;
		String teamId = "";
		Set<String> keyMap = hashForTackles.keySet();
		for(String individualKey : keyMap) {
			if(mostOrFewest.equals(">")) {
				if(hashForTackles.get(individualKey) > highestNumberOfTackles) {
					highestNumberOfTackles = hashForTackles.get(individualKey);
					teamId = individualKey;
				}
			} else {
				if(hashForTackles.get(individualKey) < lowestNumberOfTackles) {
					lowestNumberOfTackles = hashForTackles.get(individualKey);
					teamId = individualKey;
				}	
			}
		}
		return teamId;
	}
	
	private Map<String, Integer> createHashForTackles(List<String[]> seasonGames) {
		Map<String, Integer> hashForTackles = new HashMap<>();
		for(String[] selectedGame : seasonGames) {
			if(hashForTackles.containsKey(selectedGame[1])) {
				int updatedTackleCount = hashForTackles.get(selectedGame[1]);
				updatedTackleCount += Integer.parseInt(selectedGame[8]);
				hashForTackles.replace(selectedGame[1], updatedTackleCount);
			} else {
				int updatedTackleCount = Integer.parseInt(selectedGame[8]);
				hashForTackles.put(selectedGame[1], updatedTackleCount);
			}
		}
		return hashForTackles;
	}
	
	public String mostTackles(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		
		Map<String, Integer> hashForTackles = createHashForTackles(seasonGames);
		
		String teamId = getTeamIdFromNumberOfTackles(hashForTackles, ">");
		return getTeamNameFromId(teamId);
	}
	
	public String fewestTackles(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		
		Map<String, Integer> hashForTackles = createHashForTackles(seasonGames);
		
		String teamId = getTeamIdFromNumberOfTackles(hashForTackles, "<");
		return getTeamNameFromId(teamId);
	}
	
	private Map<String, Double> createHashForRatios(Map<String, double[]> hashForAccuracy) {
		Map<String, Double> hashForRatios = new HashMap<>();
		Set<String> keyMap = hashForAccuracy.keySet();
		
		for(String key : keyMap) {
			double ratio;
			ratio = hashForAccuracy.get(key)[0] / hashForAccuracy.get(key)[1];
			hashForRatios.put(key, ratio);
		}
		return hashForRatios;
	}
	
	private String getTeamIdFromRatioHash(Map<String, double[]> hashForAccuracy, String highOrLow) {
		Map<String, Double> hashForRatios = createHashForRatios(hashForAccuracy);
		Set<String> keyMap = hashForAccuracy.keySet();
		String teamId = "";
		double minimum = 0.0;
		double maximum = 1000.0;
		
		for(String key : keyMap) {
			if(highOrLow.equals(">")) {
				if(hashForRatios.get(key) > minimum) {
					minimum = hashForRatios.get(key);
					teamId = key;
				}
			} else {
				if(hashForRatios.get(key) < maximum) {
					maximum = hashForRatios.get(key);
					teamId = key;
				}
			}
		}
		
		return teamId;
	}
	
	private Map<String, double[]> createHashForAccuracy(List<String[]> seasonGames) {
		Map<String, double[]> hashForAccuracy = new HashMap<>();
		for(String[] selectedGame : seasonGames) {
			if(hashForAccuracy.containsKey(selectedGame[1])) {
				//create a new element for the double array value assigned to this key
				double[] shotsAndGoalsArray = new double[2];
				
				//get the current values
				double firstValue = hashForAccuracy.get(selectedGame[1])[0];
				double secondValue = hashForAccuracy.get(selectedGame[1])[1];
				
				//update the values
				firstValue = firstValue + Double.parseDouble(selectedGame[7]);
				secondValue = secondValue + Double.parseDouble(selectedGame[6]);
				
				//update the array elements
				shotsAndGoalsArray[0] = firstValue;
				shotsAndGoalsArray[1] = secondValue;
				
				//put the new elements in the hash
				hashForAccuracy.replace(selectedGame[1], shotsAndGoalsArray);
			} else {
				double[] shotsAndGoalsArray = new double[2];
				
				//grab the first set of values and start populating the array
				shotsAndGoalsArray[0] = Double.parseDouble(selectedGame[7]);
				shotsAndGoalsArray[1] = Double.parseDouble(selectedGame[6]); 
				hashForAccuracy.put(selectedGame[1], shotsAndGoalsArray);
			}
		}
		return hashForAccuracy;
	}
	
	public String mostAccurateTeam(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		Map<String, double[]> hashForAccuracy = createHashForAccuracy(seasonGames);
		
		//iterate through and find highest ratio
		String teamId = getTeamIdFromRatioHash(hashForAccuracy, ">");
		return getTeamNameFromId(teamId);
	}

	public String leastAccurateTeam(String season) {
		//create list of games for the selected season
		List<String[]> seasonList = gamesForSelectedSeason(season);
		
		//pick out game_teams from games from the above loop
		List<String[]> seasonGames = pullGameTeamsFromGames(seasonList);
		Map<String, double[]> hashForAccuracy = createHashForAccuracy(seasonGames);
		
		//iterate through and find lowest ratio
		String teamId = getTeamIdFromRatioHash(hashForAccuracy, "<");
		return getTeamNameFromId(teamId);
	}
	
	public Map<String, String> teamInfo(String teamId) {
		Map<String, String> teamInfo = new HashMap<>();
		for(String[] team : teams) {
			if(team[0].equals(teamId)) {
				teamInfo.put("team_id", teamId);
				teamInfo.put("franchise_id", team[1]);
				teamInfo.put("team_name", team[2]);
				teamInfo.put("abbreviation", team[3]);
				teamInfo.put("link", team[5]);
			}
		}
		return teamInfo;
	}
	
	private Map<String, double[]> createPercentageHash(String teamId) {
		Map<String, double[]> percentageHash = new HashMap<>();
		//iterate over game_teams and get rows for the selected team
		for(String[] game_team : game_teams) {
			if(game_team[1].equals(teamId)) {
				//iterate over games and get games that match the selected game_teams row
				//based on game_id
				for(String[] game : games) {
					if(game[0].equals(game_team[0])) {
						//get row data from game and update the percentage
						if(percentageHash.containsKey(game[1])) {
							double result = game_team[3].equals("WIN") ? 1.0 : 0.0;
							double updatedAverage = (percentageHash.get(game[1])[0] * percentageHash.get(game[1])[1] + result)/(percentageHash.get(game[1])[1] + 1);
							double[] updatedAverageArray = {updatedAverage, (percentageHash.get(game[1])[1] + 1)};
							percentageHash.replace(game[1], updatedAverageArray);
						} else {
							double result = game_team[3].equals("WIN") ? 1.0 : 0.0;
							double[] averageArray = {result, 1.0};
							percentageHash.put(game[1], averageArray);
						}
					}
				}
			}
		}
		return percentageHash;
	}
	
	public String bestSeason(String teamId) {
		Map<String, double[]> percentageHash = createPercentageHash(teamId);

		return getBestAndWorst(percentageHash, ">");
	}
	
	public String worstSeason(String teamId) {
		Map<String, double[]> percentageHash = createPercentageHash(teamId);

		return getBestAndWorst(percentageHash, "<");
	}
	
	public double averageWinPercentage(String teamId) {
		double wins = 0.0;
		double tries = 0.0;
		for(String[] game_team : game_teams) {
			if(game_team[1].equals(teamId)) {
				switch (game_team[3]) {
				
				case "WIN":
					wins++;
					break;
				case "TIE":
					wins += 0.5;
					break;
				}
				tries++;
			}
		}
		
		return wins/tries;
	}
	
	public int mostGoalsScored(String teamId) {
		int goalsScored = 0;
		for(String[] game : game_teams) {
			if(game[1].equals(teamId)) {
				if(Integer.parseInt(game[6]) > goalsScored) {
					goalsScored = Integer.parseInt(game[6]);
				}
			}
		}
		return goalsScored;
	}
	
	public int fewestGoalsScored(String teamId) {
		int goalsScored = Integer.MAX_VALUE;
		for(String[] game : game_teams) {
			if(game[1].equals(teamId)) {
				if(Integer.parseInt(game[6]) < goalsScored) {
					goalsScored = Integer.parseInt(game[6]);
				}
			}
		}
		return goalsScored;
	}
	
	private int getAwayWinLoss(String[] game) {
		int winLoss = 0;
		if(Integer.parseInt(game[6]) > Integer.parseInt(game[7])) {
			winLoss = 1;
		}
		return winLoss;
	}
	
	private int getHomeWinLoss(String[] game) {
		int winLoss = 0;
		if(Integer.parseInt(game[7]) > Integer.parseInt(game[6])) {
			winLoss = 1;
		}
		return winLoss;
	}
	
	private double roundedAverage(double updatedAverage) {
		BigDecimal bd = BigDecimal.valueOf(updatedAverage);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
		}
	
	private Map<String, double[]> getAwayTeamHash(Map<String, double[]> opponentHash, String[] game, String teamId) {
		int winLoss = getAwayWinLoss(game);
		if(opponentHash.containsKey(game[5])) {
			double average = opponentHash.get(game[5])[0];
			double numberOfGames = opponentHash.get(game[5])[1];
			double updatedAverage = (average * numberOfGames + winLoss)/(numberOfGames + 1);
			double roundedAverage = roundedAverage(updatedAverage);
			double[] updatedAverageArray = {roundedAverage, numberOfGames + 1};
			opponentHash.replace(game[5], updatedAverageArray);
		} else {
			double[] newAverageArray = {winLoss, 1.0};
			opponentHash.put(game[5], newAverageArray);
		}
	return opponentHash;	
	}
	
	private Map<String, double[]> getHomeTeamHash(Map<String, double[]> opponentHash, String[] game, String teamId) {
		int winLoss = getHomeWinLoss(game);
		if(opponentHash.containsKey(game[4])) {
			double average = opponentHash.get(game[4])[0];
			double numberOfGames = opponentHash.get(game[4])[1];
			double updatedAverage = (average * numberOfGames + winLoss)/(numberOfGames + 1);
			double roundedAverage = roundedAverage(updatedAverage);
			double[] updatedAverageArray = {roundedAverage, numberOfGames + 1};
			opponentHash.replace(game[4], updatedAverageArray);
		} else {
			double[] newAverageArray = {winLoss, 1.0};
			opponentHash.put(game[4], newAverageArray);
		}
		return opponentHash;	
	}
	
	private Map<String, double[]> generateOpponentHash(String teamId) {
		Map<String, double[]> opponentHash = new HashMap<>();
		for(String[] game : games) {
			//team is the away team
			if(game[4].equals(teamId)) {
				opponentHash = getAwayTeamHash(opponentHash, game, teamId);
			} else if(game[5].equals(teamId)) {
				//team is the home team
				opponentHash = getHomeTeamHash(opponentHash, game, teamId);
			}
		}
		return opponentHash;
	}
	
	public String favoriteOpponent(String teamId) {
		Map<String, double[]> opponentHash = generateOpponentHash(teamId);
		String opponent = getBestAndWorst(opponentHash, ">");
		return getTeamNameFromId(opponent);
	}
	
	public String rival(String teamId) {
		Map<String, double[]> opponentHash = generateOpponentHash(teamId);
		String opponent = getBestAndWorst(opponentHash, "<");
		return getTeamNameFromId(opponent);
	}
	
	public int biggestTeamBlowout(String teamId) {
		int biggestBlowout = 0;
		for(String[] game : games) {
			if(game[4].equals(teamId)) {
				int difference = Integer.parseInt(game[6]) - Integer.parseInt(game[7]);
				if(difference > biggestBlowout) {
					biggestBlowout = difference;
				}
			} else if(game[5].equals(teamId)) {
				int difference = Integer.parseInt(game[7]) - Integer.parseInt(game[6]);
				if(difference > biggestBlowout) {
					biggestBlowout = difference;
				}
			}
		}
		return biggestBlowout;
	}
	
	public int worstLoss(String teamId) {
		int worstLoss = 0;
		for(String[] game : games) {
			if(game[4].equals(teamId)) {
				int difference = Integer.parseInt(game[7]) - Integer.parseInt(game[6]);
				if(difference > worstLoss) {
					worstLoss = difference;
				}
			} else if(game[5].equals(teamId)) {
				int difference = Integer.parseInt(game[6]) - Integer.parseInt(game[7]);
				if(difference > worstLoss) {
					worstLoss = difference;
				}
			}
		}
		return worstLoss;
	}
	
	public Map<String, Double> headToHead(String teamId) {
		Map<String, Double> headToHead = new HashMap<>();
		Map<String, double[]> winningHash = generateOpponentHash(teamId);
		Set<String> keyMap = winningHash.keySet();
		for(String key : keyMap) {
			headToHead.put(key, winningHash.get(key)[0]);
		}
		return headToHead;
	}
	
	public Map<String, Map<String, Double>> seasonalSummary(String teamId) {
		Map<String, Map<String, Double>> seasonalSummary = new HashMap<>();
		return seasonalSummary;
	}
}

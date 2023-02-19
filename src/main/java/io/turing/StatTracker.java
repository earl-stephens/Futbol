package io.turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTracker {
	private String file1;
	private String file2;
	private String file3;
	public List<HashMap<String, String>> game_teams = new ArrayList<>();
	public List<HashMap<String, String>> games = new ArrayList<>();
	public List<HashMap<String, String>> teams = new ArrayList<>();
	
	public StatTracker(String file1Location, String file2Location, String file3Location) {
		file1 = file1Location;
		file2 = file2Location;
		file3 = file3Location;
	}
	
	
}

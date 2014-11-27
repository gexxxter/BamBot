package tsBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.TS3Api;

public class RessourceManager {
	private static ArrayList<LogEntry> log = new ArrayList<>();
	private static Map<String, ArrayList<String>> voteList = new HashMap<>();
	private static ArrayList<AccesLogEntry> accesLog = new ArrayList<>();
	private static TS3Api api;

	
	public static Map<String, ArrayList<String>> getVoteList() {
		return voteList;
	}


	public static TS3Api getApi() {
		return api;
	}


	public static void setApi(TS3Api api) {
		RessourceManager.api = api;
	}


	public static ArrayList<AccesLogEntry> getAccesLog() {
		return accesLog;
	}


	public static ArrayList<LogEntry> getLog() {
		return log;
	}

	
}

package tsBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class RessourceManager {
	private static ArrayList<LogEntry> log = new ArrayList<>();
	private static Map<String, ArrayList<String>> voteList = new HashMap<>();
	private static ArrayList<AccesLogEntry> accesLog = new ArrayList<>();
	private static TS3Api api;
	public final static int TEAM_SIZE = 3;
	private static String myNickname = "BamBot";

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

	public static void logMessage(TextMessageEvent e, String message) {
		LogEntry logEntry = new LogEntry();
		ArrayList<LogEntry> log = RessourceManager.getLog();
		logEntry.setMessage(message);
		logEntry.setUserId(e.getInvokerUserId());
		logEntry.setUserName(e.getInvokerName());
		log.add(logEntry);
	}

	public static void showVotes(int clientId) {
		Map<String, ArrayList<String>> voteList = RessourceManager
				.getVoteList();
		for (String key : voteList.keySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(key).append("=>");
			for (String clientName : voteList.get(key)) {
				builder.append(clientName).append(", ");
			}
			api.sendPrivateMessage(clientId, builder.toString());
		}
	}

	public static void showAccesLog(int clientId) {
		ArrayList<AccesLogEntry> accesLog = RessourceManager.getAccesLog();
		for (AccesLogEntry entry : accesLog) {
			StringBuilder builder = new StringBuilder();
			builder.append(entry.getUserName()).append(" ");
			builder.append(entry.getValue()).append(" ");
			String date = Utils.createDateString(entry.getTimestamp(),
					"dd.MM.YYYY hh:mm:ss");
			builder.append(" at ").append(date);
			MessageService.sendPrivatMessage(builder.toString(),clientId);
		}
	}
	
	public static void logClientJoin(ClientJoinEvent e) {
		String value = "";
		ArrayList<AccesLogEntry> accesLog = RessourceManager.getAccesLog();
		AccesLogEntry accesLogEntry = new AccesLogEntry();
		value = "connected";
		accesLogEntry.setUserName(e.getClientNickname());
		accesLogEntry.setValue(value);
		if (!accesLog.contains(accesLogEntry)) {
			accesLog.add(accesLogEntry);
		}
	}

	public static void vote(String game, String clientName) {
		ArrayList<String> clientNames = null;
		Map<String, ArrayList<String>> voteList = RessourceManager
				.getVoteList();
		if (null == voteList.get(game)) {
			clientNames = new ArrayList<>();
			voteList.put(game, clientNames);
		} else {
			clientNames = voteList.get(game);
		}

		if (!clientNames.contains(clientName)) {
			clientNames.add(clientName);
			for (String client : clientNames) {
				MessageService.sendPrivatMessage("[B]" + clientName
						+ "[/B] voted also on " + game + "("
						+ clientNames.size() + "/" + TEAM_SIZE + ")",client);
			}
		} else {
			MessageService.sendPrivatMessage("You already voted!",clientName);
		}

		if (clientNames.size() >= TEAM_SIZE) {
			HashMap<ChannelProperty, String> properties = new HashMap<>();
			properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
			int channelId = UserOperationService
					.createChannel(properties, game);
			for (String name : clientNames) {
				int clientId = UserOperationService.getClientIdByUsername(name);
				api.moveClient(clientId, channelId);
			}
			clientNames.clear();
		}
	}

	public static String getMyNickname() {
		return myNickname;
	}

	public static void setMyNickname(String myNickname) {
		RessourceManager.myNickname = myNickname;
	}

}

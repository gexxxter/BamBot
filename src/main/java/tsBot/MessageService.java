package tsBot;

import java.util.ArrayList;
import com.github.theholywaffle.teamspeak3.TS3Api;

public class MessageService {

	public static void sendChannelMessage(String message) {
		TS3Api api = RessourceManager.getApi();
		api.sendChannelMessage(message);
	}

	public static void sendPrivatMessage(String message, int clientId) {
		TS3Api api = RessourceManager.getApi();
		api.sendPrivateMessage(clientId, message);
	}

	public static void sendPrivatMessage(String message, String userName) {
		TS3Api api = RessourceManager.getApi();
		int clientId = api.getClientByName(userName).get(0).getId();
		sendPrivatMessage(message, clientId);
	}

	public static void displayLog(String searchText) {
		ArrayList<LogEntry> log = RessourceManager.getLog();
		for (LogEntry entry : log) {
			if (entry.getMessage().contains(searchText)) {
				String date = Utils.createDateString(entry.getTimestamp());
				sendChannelMessage(entry.getUserName() + " : "
						+ entry.getMessage() + "(" + date + ")");
			}
		}
	}

	public static void openCmdConsole(int clientId) {
		StringBuilder builder = new StringBuilder();
		builder.append("===Command Console BamBot 1.0===\n");
		builder.append("vote cs and vote lol are supported\n");
		sendPrivatMessage(builder.toString(), clientId);
	}

	public static void showCmdHelp(int clientId) {
		StringBuilder builder = new StringBuilder();
		TS3Api api = RessourceManager.getApi();
		builder.append("===Available Commands===").append("\n");
		builder.append(
				"vote [game] -If the enough players voted for the same game you will be moved to an own channel")
				.append("\n");
		builder.append("showvotes -Shows the current vote statistic").append(
				"\n");
		api.sendPrivateMessage(clientId, builder.toString());
	}

	public static void displayHelp() {
		sendChannelMessage("===COMMANDS AVAILABLE===\nping\ncya\nfindmsg\nhelp\nhello\nprivate");
	}

}

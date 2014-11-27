package tsBot;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import com.github.theholywaffle.teamspeak3.TS3Api;

public class MessageService {
	
	public static void showVotes(int clientId) {
		Map<String, ArrayList<String>> voteList = RessourceManager.getVoteList();
		TS3Api api = RessourceManager.getApi();
		for (String key : voteList.keySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(key).append("=>");
			for (String clientName : voteList.get(key)) {
				builder.append(clientName).append(", ");
			}
			api.sendPrivateMessage(clientId, builder.toString());

		}
	}
	
	public static void sendChannelMessage(String message){
		TS3Api api = RessourceManager.getApi();
		api.sendChannelMessage(message);
	}
}

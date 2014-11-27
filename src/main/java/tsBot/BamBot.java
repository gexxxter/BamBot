package tsBot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.w3c.dom.Entity;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Wrapper;

public class BamBot implements TS3Listener {

	private TS3Api api;
	private String myNickname;
	private ArrayList<LogEntry> log = new ArrayList<>();
	private Map<String, ArrayList<String>> voteList = new HashMap<>();
	private ArrayList<AccesLogEntry> accesLog = new ArrayList<>();

	private int myChannelId = 1;
	public static int TEAM_SIZE = 3;

	public BamBot(String user, String pass) {
		final TS3Config config = new TS3Config();
		config.setHost("127.0.0.1");
		config.setDebugLevel(Level.OFF);
		config.setFloodRate(FloodRate.UNLIMITED);
		config.setLoginCredentials(user, pass);

		final TS3Query query = new TS3Query(config);
		query.connect();
		api = query.getApi();
		api.selectVirtualServerById(1);
		myNickname = "BamBot";
		api.setNickname(myNickname);
		api.sendChannelMessage(myNickname + " is online!");
		api.registerAllEvents();
		api.moveClient(1);
		api.addTS3Listeners(this);

	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		if (e.getTargetMode() == TextMessageTargetMode.CHANNEL
				&& !e.getInvokerName().equals(myNickname)) {
			String message = e.getMessage().toLowerCase();
			String[] commands = message.split(" ");
			switch (commands[0]) {
			case "ping":
				api.sendChannelMessage("pong");
				break;
			case "cya":
				api.sendChannelMessage("Hauste rein  " + e.getInvokerName());
				api.kickClientFromServer(e.getInvokerId());
				break;
			case "findmsg":
				displayLog(commands[1]);
				break;
			case "help":
				displayHelp();
				break;
			case "hello":
				api.sendChannelMessage("Hello " + e.getInvokerName());
				break;
			case "suicide":
				api.sendChannelMessage("Ok... killing myself now...");
				System.exit(0);
				break;
			case "private":
				if (commands.length > 1) {
					privateMode(e.getInvokerName(), commands[1]);
				} else {
					api.sendChannelMessage("Parameter missind!");
				}
				break;
			default:
				logMessage(e, message);
				break;
			}
		}

		if (e.getTargetMode() == TextMessageTargetMode.CLIENT
				&& !e.getInvokerName().equals(myNickname)) {
			String message = e.getMessage().toLowerCase();
			String[] commands = message.split(" ");
			switch (commands[0]) {
			case "vote":
				if (commands.length > 1) {
					vote(commands[1], e.getInvokerName());
				} else {
					api.sendPrivateMessage(e.getInvokerId(),
							"Parameter missing");
				}
				break;
			case "showvotes":
				showVotes(e.getInvokerId());
				break;
			case "help":
				showCmdHelp(e.getInvokerId());
				break;
			case "showacceslog":
				showAccesLog(e.getInvokerId());
				break;
			}
		}
	}

	private void showCmdHelp(int clientId) {
		StringBuilder builder = new StringBuilder();
		builder.append("===Available Commands===").append("\n");
		builder.append(
				"vote [game] -If the enough players voted for the same game you will be moved to an own channel")
				.append("\n");
		builder.append("showvotes -Shows the current vote statistic").append(
				"\n");
		api.sendPrivateMessage(clientId, builder.toString());
	}

	private void showVotes(int clientId) {
		for (String key : voteList.keySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(key).append("=>");
			for (String clientName : voteList.get(key)) {
				builder.append(clientName).append(", ");
			}
			api.sendPrivateMessage(clientId, builder.toString());

		}
	}

	private void showAccesLog(int clientId) {
		for (AccesLogEntry entry : accesLog) {
			StringBuilder builder = new StringBuilder();
			builder.append(entry.getUserName()).append(" ");
			builder.append(entry.getValue()).append(" ");
			String date = createDateString(entry.getTimestamp(),
					"dd.MM.YYYY hh:mm:ss");
			builder.append(" at ").append(date);
			api.sendPrivateMessage(clientId, builder.toString());
		}
	}

	private void vote(String game, String clientName) {
		ArrayList<String> clientNames = null;
		if (null == voteList.get(game)) {
			clientNames = new ArrayList<>();
			voteList.put(game, clientNames);
		} else {
			clientNames = voteList.get(game);
		}

		if (!clientNames.contains(clientName)) {
			clientNames.add(clientName);
			for (String client : clientNames) {
				api.sendPrivateMessage(api.getClientByName(client).get(0)
						.getId(), "[B]" + clientName + "[/B] voted also on "
						+ game + "(" + clientNames.size() + "/" + TEAM_SIZE
						+ ")");
			}
		} else {
			api.sendPrivateMessage(api.getClientByName(clientName).get(0)
					.getId(), "You already voted!");
		}

		if (clientNames.size() >= TEAM_SIZE) {
			HashMap<ChannelProperty, String> properties = new HashMap<>();
			properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
			int channelId = createChannel(properties, game);
			for (String name : clientNames) {
				int clientId = api.getClientByName(name).get(0).getId();
				api.moveClient(clientId, channelId);
			}
			clientNames.clear();
		}
	}

	private int createChannel(HashMap<ChannelProperty, String> properties,
			String channelName) {
		int i = 0;
		while (api.getChannelByName(channelName) != null) {
			channelName += i;
			i++;
		}

		return api.createChannel(channelName, properties);

	}

	private void privateMode(String client1, String client2) {
		String channelName = "private";
		int i = 0;
		while (api.getChannelByName(channelName) != null) {
			channelName += i;
			i++;
		}
		HashMap<ChannelProperty, String> properties = new HashMap<>();
		properties.put(ChannelProperty.CHANNEL_PASSWORD, "geheim");
		properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
		int channelId = api.createChannel(channelName, properties);
		int clientId1 = api.getClientByName(client1).get(0).getId();
		int clientId2 = api.getClientByName(client2).get(0).getId();
		api.moveClient(clientId1, channelId);
		api.moveClient(clientId2, channelId);
		api.moveClient(myChannelId);

	}

	private void displayHelp() {
		api.sendChannelMessage("===COMMANDS AVAILABLE===\nping\ncya\nfindmsg\nhelp\nhello\nprivate");
	}

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		StringBuilder builder = new StringBuilder();
		builder.append("===Command Console BamBot 1.0===\n");
		builder.append("vote cs and vote lol are supported\n");
		api.sendPrivateMessage(e.getClientId(), builder.toString());
		logClientJoin(e);

	}

	private void logClientJoin(ClientJoinEvent e) {
		String value = "";
		AccesLogEntry accesLogEntry = new AccesLogEntry();
		value = "connected";
		accesLogEntry.setUserName(e.getClientNickname());
		accesLogEntry.setValue(value);
		if (!accesLog.contains(accesLogEntry)) {
			accesLog.add(accesLogEntry);
		}
	}

	String getUserNameById(int clientId) {
		return api.getClientInfo(clientId).getNickname();
	}

	@Override
	public void onClientLeave(ClientLeaveEvent e) {
		String value = "";
		AccesLogEntry accesLogEntry = new AccesLogEntry();
		value = "disconnected";
		accesLogEntry.setUserName(getUserNameById(e.getClientId()));
		accesLogEntry.setValue(value);
		if (!accesLog.contains(accesLogEntry)) {
			accesLog.add(accesLogEntry);
		}
	}

	@Override
	public void onServerEdit(ServerEditedEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelEdit(ChannelEditedEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientMoved(ClientMovedEvent e) {
		ClientInfo clientInfo = api.getClientInfo(e.getClientId());
		if (clientInfo.getNickname().toLowerCase().contains("slaxxx")) {
			api.moveClient(clientInfo.getChannelId());
			api.sendPrivateMessage(e.getClientId(), "Following my Master to: "
					+ api.getChannelInfo(clientInfo.getChannelId()).getName());
			myChannelId = clientInfo.getChannelId();
		}

	}

	@Override
	public void onChannelCreate(ChannelCreateEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelDeleted(ChannelDeletedEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelMoved(ChannelMovedEvent e) {
		// TODO Auto-generated method stub

	}

	private void logMessage(TextMessageEvent e, String message) {
		LogEntry logEntry = new LogEntry();
		logEntry.setMessage(message);
		logEntry.setUserId(e.getInvokerUserId());
		logEntry.setUserName(e.getInvokerName());
		log.add(logEntry);
	}

	private void displayLog(String searchWord) {
		for (LogEntry entry : log) {
			if (entry.getMessage().contains(searchWord)) {
				String date = createDateString(entry.getTimestamp());
				api.sendChannelMessage(entry.getUserName() + " : "
						+ entry.getMessage() + "(" + date + ")");
			}
		}
	}

	private String createDateString(Timestamp timestamp, String pattern) {
		Date date = (Date) timestamp;
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);

	}

	private String createDateString(Timestamp timestamp) {
		return createDateString(timestamp, "dd.MM.yyyy");
	}

	@Override
	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
		// TODO Auto-generated method stub

	}

}

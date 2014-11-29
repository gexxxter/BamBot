package tsBot;

import java.util.ArrayList;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
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
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class BamBot implements TS3Listener {

	private TS3Api api;
	public String myNickname="BamBot";

	private int myChannelId = 1;

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

		api.setNickname(myNickname);
		api.sendChannelMessage(myNickname + " is online!");
		api.registerAllEvents();
		api.moveClient(1);
		api.addTS3Listeners(this);
		RessourceManager.setApi(api);
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		if (!e.getInvokerName().equals(RessourceManager.getMyNickname())) {
			switch (e.getTargetMode()) {
			case CHANNEL:
				ChannelMessageReciever.handleMessage(e);
				break;
			case CLIENT:
				PrivateMessageReciever.handleMessage(e);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		MessageService.openCmdConsole(e.getClientId());
		RessourceManager.logClientJoin(e);
	}

	@Override
	public void onClientLeave(ClientLeaveEvent e) {
		String value = "";
		ArrayList<AccesLogEntry> accesLog = RessourceManager.getAccesLog();
		AccesLogEntry accesLogEntry = new AccesLogEntry();
		value = "disconnected";
		accesLogEntry.setUserName(UserOperationService.getUsernameById(e.getClientId()));
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

	@Override
	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
		// TODO Auto-generated method stub

	}

}

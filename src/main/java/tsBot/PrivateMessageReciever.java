package tsBot;

import javax.inject.Inject;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class PrivateMessageReciever {

	RessourceManager ressourceManager;
	
	public void handleMessage(TextMessageEvent e) {

		String message = e.getMessage().toLowerCase();
		String[] commands = message.split(" ");
		switch (commands[0]) {
		case "vote":
			if (commands.length > 1) {
				vote(commands[1], e.getInvokerName());
			} else {
				api.sendPrivateMessage(e.getInvokerId(), "Parameter missing");
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

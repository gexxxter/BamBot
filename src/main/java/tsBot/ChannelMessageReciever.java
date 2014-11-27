package tsBot;

import javax.inject.Inject;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class ChannelMessageReciever {

	@Inject
	RessourceManager ressourceManager;

	public void handleMessage(TextMessageEvent e) {

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
}

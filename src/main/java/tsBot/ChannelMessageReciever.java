package tsBot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class ChannelMessageReciever {


	public static void handleMessage(TextMessageEvent e) {

		String message = e.getMessage().toLowerCase();
		String[] commands = message.split(" ");
		switch (commands[0]) {
		case "ping":
			MessageService.sendChannelMessage("pong");
			break;
		case "cya":
			MessageService.sendChannelMessage("Hauste rein  " + e.getInvokerName());
			UserOperationService.kickUserFromChannel(e.getInvokerId());
			break;
		case "findmsg":
			MessageService.displayLog(commands[1]);
			break;
		case "help":
			MessageService.displayHelp();
			break;
		case "hello":
			MessageService.sendChannelMessage("Hello " + e.getInvokerName());
			break;
		case "suicide":
			MessageService.sendChannelMessage("Ok... killing myself now...");
			System.exit(0);
			break;
		case "private":
			if (commands.length > 1) {
				UserOperationService.privateMode(e.getInvokerName(), commands[1]);
			} else {
				MessageService.sendChannelMessage("Parameter missind!");
			}
			break;
		default:
			RessourceManager.logMessage(e, message);
			break;
		}

	}
}

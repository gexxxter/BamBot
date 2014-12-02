package tsBot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class PrivateMessageReciever {

	RessourceManager ressourceManager;
	
	public static void handleMessage(TextMessageEvent e) {

		String message = e.getMessage().toLowerCase();
		String[] commands = message.split(" ");
		switch (commands[0]) {
		case "vote":
			if (commands.length > 1) {
				RessourceManager.vote(commands[1], e.getInvokerName());
			} else {
				MessageService.sendPrivatMessage("Parameter missing", e.getInvokerId());
			}
			break;
		case "showvotes":
			RessourceManager.showVotes(e.getInvokerId());
			break;
		case "help":
			MessageService.showCmdHelp(e.getInvokerId());
			break;
		case "showacceslog":
			RessourceManager.showAccesLog(e.getInvokerId());
			break;
		}

	}
}

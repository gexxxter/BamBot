package tsBot;

import java.util.HashMap;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;

public class UserOperationService {

	
	
	public static void kickUserFromChannel(String userName){
		TS3Api api = RessourceManager.getApi();
		int clientId = api.getClientByName(userName).get(0).getId();
		kickUserFromChannel(clientId);
	}
	
	public static void kickUserFromChannel(int clientId){
		TS3Api api = RessourceManager.getApi();
		api.kickClientFromChannel(clientId);
	}
	
	public static void privateMode(String client1, String client2) {
		TS3Api api = RessourceManager.getApi();
		String channelName = "private";
		int i = 0;
		while (api.getChannelByName(channelName) != null) {
			channelName += i;
			i++;
		}
		HashMap<ChannelProperty, String> properties = new HashMap<>();
		properties.put(ChannelProperty.CHANNEL_PASSWORD, "geheim");
		properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
		int channelId = createChannel(properties, channelName); 
		int clientId1 = api.getClientByName(client1).get(0).getId();
		int clientId2 = api.getClientByName(client2).get(0).getId();
		api.moveClient(clientId1, channelId);
		api.moveClient(clientId2, channelId);
	}
	
	public static int createChannel(HashMap<ChannelProperty, String> properties,
			String channelName) {
		TS3Api api = RessourceManager.getApi();
		int i = 0;
		String channelNameNew=channelName;
		while (api.getChannelByName(channelNameNew) != null) {
			channelNameNew = channelName+i;
			i++;
		}
		return api.createChannel(channelName, properties);
	}
	
	public static String getUsernameById(int clientId){
		TS3Api api = RessourceManager.getApi();
		return api.getClientInfo(clientId).getNickname();
	}
	
	public static int getClientIdByUsername(String userName){
		TS3Api api = RessourceManager.getApi();
		return api.getClientByName(userName).get(0).getId();
	}
}

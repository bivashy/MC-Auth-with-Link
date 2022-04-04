package me.mastercapexd.auth.vk.commandhandler;

import java.util.List;
import java.util.Random;

import com.ubivashka.vk.bungee.VKAPI;
import com.ubivashka.vk.bungee.events.VKMessageEvent;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;

public abstract class VKCommandExecutor {
	public static final VkApiClient vk = VKAPI.getInstance().getVK();
	public static final GroupActor actor = VKAPI.getInstance().getActor();
	public static final Random random = new Random();

	public abstract void execute(VKMessageEvent event, String[] args);

	public abstract String getKey();

	public boolean sendMessage(Integer peerId, String message) {
		try {
			vk.messages().send(actor).randomId(random.nextInt()).peerId(peerId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isChat(Integer peerId) {
		if (peerId == null)
			return false;
		try {
			List<Conversation> conversations = VKAPI.getInstance().getVK().messages()
					.getConversationsById(VKAPI.getInstance().getActor(), peerId).execute().getItems();
			if (conversations.isEmpty())
				return false;
			return conversations.get(0).getPeer().getType() == ConversationPeerType.CHAT;
		} catch (ApiException | ClientException e) {
			return false;
		}
	}
}

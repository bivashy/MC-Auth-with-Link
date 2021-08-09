package me.mastercapexd.auth.vk.utils;

import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.PluginConfig;

public class VKUtils {
	private static final VkApiClient vk = VKAPI.getInstance().getVK();
	private static final GroupActor actor = VKAPI.getInstance().getActor();
	private static final Random random = new Random();
	private static final Gson gson = new Gson();

	private PluginConfig config;

	public VKUtils(PluginConfig config) {
		this.config = config;
	}

	public boolean isChat(Integer peerId) {
		if (peerId == null)
			return false;
		try {
			List<Conversation> conversations = vk.messages().getConversationsById(actor, peerId).execute().getItems();
			if (conversations.isEmpty())
				return false;
			return conversations.get(0).getPeer().getType() == ConversationPeerType.CHAT;
		} catch (ApiException | ClientException e) {
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message) {
		try {
			vk.messages().send(actor).randomId(random.nextInt()).userId(userId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message, Keyboard keyboard) {
		try {
			vk.messages().send(actor).randomId(random.nextInt()).userId(userId).message(message).keyboard(keyboard)
					.execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer getVKIdFromScreenName(String screenName) {
		try {
			return vk.utils().resolveScreenName(actor, screenName).execute().getObjectId();
		} catch (Exception e) {
			return -1;
		}

	}

	public KeyboardButton buildCallbackButton(String labelPath, String payload, KeyboardButtonColor color) {
		return new KeyboardButton()
				.setAction(new KeyboardButtonAction().setLabel(config.getVKButtonLabels().getButtonLabel(labelPath))
						.setType(TemplateActionTypeNames.CALLBACK).setPayload(gson.toJson(payload)))
				.setColor(color);
	}

	public KeyboardButton buildCallbackButton(String labelPath, Account account, String payload,
			KeyboardButtonColor color) {
		return new KeyboardButton().setAction(
				new KeyboardButtonAction().setLabel(config.getVKButtonLabels().getButtonLabel(labelPath, account))
						.setType(TemplateActionTypeNames.CALLBACK).setPayload(gson.toJson(payload)))
				.setColor(color);
	}

}

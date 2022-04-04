package me.mastercapexd.auth.vk.utils;

import java.util.List;
import java.util.Optional;
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
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;

public class VKUtils {
	private static final VkApiClient VK = VKAPI.getInstance().getVK();
	private static final GroupActor ACTOR = VKAPI.getInstance().getActor();
	private static final Random RANDOM = new Random();
	private static final Gson GSON = new Gson();

	private PluginConfig config;

	public VKUtils(PluginConfig config) {
		this.config = config;
	}

	public boolean isChat(Integer peerId) {
		if (peerId == null)
			return false;
		try {
			List<Conversation> conversations = VK.messages().getConversationsById(ACTOR, peerId).execute().getItems();
			if (conversations.isEmpty())
				return false;
			return conversations.get(0).getPeer().getType() == ConversationPeerType.CHAT;
		} catch (ApiException | ClientException e) {
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message) {
		try {
			VK.messages().send(ACTOR).randomId(RANDOM.nextInt()).userId(userId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message, Keyboard keyboard) {
		try {
			VK.messages().send(ACTOR).randomId(RANDOM.nextInt()).userId(userId).message(message).keyboard(keyboard)
					.execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Optional<GetResponse> fetchUserFromIdentificator(String vkIdentificator) {
		try {
			return VK.users().get(ACTOR).userIds(vkIdentificator).execute().stream().findFirst();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Optional<Integer> fetchIdFromScreenName(String screenName) {
		try {
			return Optional.of(VK.utils().resolveScreenName(ACTOR, screenName).execute().getObjectId());
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return Optional.empty();

	}

	public KeyboardButton buildCallbackButton(String labelPath, String payload, KeyboardButtonColor color) {
		return new KeyboardButton().setAction(new KeyboardButtonAction()
				.setLabel(config.getVKSettings().getVKButtonLabels().getButtonLabel(labelPath))
				.setType(TemplateActionTypeNames.CALLBACK).setPayload(GSON.toJson(payload))).setColor(color);
	}

	public KeyboardButton buildCallbackButton(String labelPath, Account account, String payload,
			KeyboardButtonColor color) {
		return new KeyboardButton().setAction(new KeyboardButtonAction()
				.setLabel(config.getVKSettings().getVKButtonLabels().getButtonLabel(labelPath, account))
				.setType(TemplateActionTypeNames.CALLBACK).setPayload(GSON.toJson(payload))).setColor(color);
	}

}

package me.mastercapexd.auth.vk.utils;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
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
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKUtils {
	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final VkApiClient VK = VK_HOOK.getClient();
	private static final GroupActor ACTOR = VK_HOOK.getActor();
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
			VK.messages().send(ACTOR).randomId(ThreadLocalRandom.current().nextInt()).userId(userId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message, Keyboard keyboard) {
		try {
			VK.messages().send(ACTOR).randomId(ThreadLocalRandom.current().nextInt()).userId(userId).message(message).keyboard(keyboard)
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
	
	/**
	 * Uploads photo to the vk api server, and retrieves as format:
	 * photo{owner_id}_{media_id}
	 * 
	 * @param Photo. Photo that need to upload
	 * @return VK api attachment
	 */
	public static String getPhotoAttachment(File file) {
		try {
			String uploadUrl = VK.photos().getMessagesUploadServer(ACTOR).execute().getUploadUrl().toString();
			PhotoUploadResponse photoUploadResponse = VK.upload().photo(uploadUrl, file).execute();

			SaveMessagesPhotoResponse savePhotoResponse = VK.photos()
					.saveMessagesPhoto(ACTOR, photoUploadResponse.getPhoto()).server(photoUploadResponse.getServer())
					.hash(photoUploadResponse.getHash()).execute().get(0);
			return "photo" + savePhotoResponse.getOwnerId() + "_" + savePhotoResponse.getId();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return null;
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

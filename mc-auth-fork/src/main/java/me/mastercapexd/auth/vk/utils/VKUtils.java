package me.mastercapexd.auth.vk.utils;

import java.io.File;
import java.util.Optional;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKUtils {
	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final VkApiClient VK = VK_HOOK.getClient();
	private static final GroupActor ACTOR = VK_HOOK.getActor();

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
}

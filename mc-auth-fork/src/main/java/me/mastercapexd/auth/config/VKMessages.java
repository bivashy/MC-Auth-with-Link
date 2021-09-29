package me.mastercapexd.auth.config;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Messages;
import net.md_5.bungee.config.Configuration;

public class VKMessages implements Messages {

	private final Map<String, String> strings = Maps.newHashMap();

	public VKMessages(Configuration messages) {
		for (String key : messages.getKeys())
			strings.put(key, messages.getString(key));
	}

	@Override
	public String colorMessage(String message) {
		return message;
	}

	@Override
	public String getLegacyMessage(String key) {
		return strings.get(key);
	}

	public String getMessage(String key, Integer userID, Account account) {
		String message = getLegacyMessage(key).replaceAll("(?i)%name%", account.getName())
				.replaceAll("(?i)%nick%", account.getName()).replaceAll("(?i)%account_ip%", account.getLastIpAddress())
				.replaceAll("(?i)%vk_id%", String.valueOf(account.getVKId()));
		try {
			List<GetResponse> getResponses = VKAPI.getInstance().getVK().users().get(VKAPI.getInstance().getActor())
					.userIds(String.valueOf(userID)).execute();
			if (!getResponses.isEmpty()) {
				GetResponse getResponse = getResponses.get(0);
				message = message.replaceAll("(?i)%user_screen_name%", getResponse.getScreenName())
						.replaceAll("(?i)%user_first_name%", getResponse.getFirstName())
						.replaceAll("(?i)%user_last_name%", getResponse.getLastName())
						.replaceAll("(?i)%user_id%", String.valueOf(userID));
			}
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return message;
	}

}

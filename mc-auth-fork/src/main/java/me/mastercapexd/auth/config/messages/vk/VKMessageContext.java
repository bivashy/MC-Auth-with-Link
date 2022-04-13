package me.mastercapexd.auth.config.messages.vk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ubivashka.vk.api.providers.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.messages.MessageContext;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;

import static me.mastercapexd.auth.utils.CollectionUtils.newEntry;

public class VKMessageContext implements MessageContext {
	private static final VkApiProvider VK_API_PROVIDER = BungeeVkApiPlugin.getInstance().getVkApiProvider();
	private static final VkApiClient CLIENT = VK_API_PROVIDER.getVkApiClient();
	private static final GroupActor ACTOR = VK_API_PROVIDER.getActor();
	private static final String IGNORE_CASE_REGEX = "(?i)";

	private final Integer userID;
	private final Account account;

	public VKMessageContext(Integer userId, Account account) {
		this.userID = userId;
		this.account = account;
	}

	@Override
	public Map<String, String> getPlaceholders() {
		LinkUserInfo vkLinkUserInfo = account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).get().getLinkUserInfo();

		Map<String, String> accountPlaceholders = Stream
				.of(newEntry("%name%", account.getName()), newEntry("%nick%", account.getName()),
						newEntry("%vk_id%", vkLinkUserInfo.getIdentificator().asString()),
						newEntry("%account_name%", account.getName()), newEntry("%account_nick%", account.getName()),
						newEntry("%account_ip%", account.getLastIpAddress()),
						newEntry("%account_last_ip%", account.getLastIpAddress()),
						newEntry("%account_vk_id%", vkLinkUserInfo.getIdentificator().asString()))
				.collect(Collectors.toMap((entry) -> IGNORE_CASE_REGEX + entry.getKey(), Entry::getValue));

		Map<String, String> vkUserPlaceholders = new HashMap<>();

		try {
			List<GetResponse> userInfoResponses = CLIENT.users().get(ACTOR).userIds(String.valueOf(userID)).execute();
			if (!userInfoResponses.isEmpty()) {
				GetResponse userInfoResponse = userInfoResponses.get(0);
				vkUserPlaceholders = Stream.of(newEntry("%user_screen_name%", userInfoResponse.getScreenName()),
						newEntry("%user_first_name%", userInfoResponse.getFirstName()),
						newEntry("%user_last_name%", userInfoResponse.getLastName()),
						newEntry("%user_id%", String.valueOf(userID))).map(entry -> {
							if (entry.getValue() == null)
								return newEntry(entry.getKey(), "");
							return entry;
						}).collect(Collectors.toMap((entry) -> IGNORE_CASE_REGEX + entry.getKey(), Entry::getValue));
			}
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}

		accountPlaceholders.putAll(vkUserPlaceholders);
		return accountPlaceholders;
	}

}

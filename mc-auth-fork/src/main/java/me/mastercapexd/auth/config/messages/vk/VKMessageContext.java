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
import me.mastercapexd.auth.utils.CollectionUtils;

public class VKMessageContext implements MessageContext {
	private static final VkApiProvider VK_API_PROVIDER = BungeeVkApiPlugin.getInstance().getVkApiProvider();
	private static final VkApiClient CLIENT = VK_API_PROVIDER.getVkApiClient();
	private static final GroupActor ACTOR = VK_API_PROVIDER.getActor();
	private static final String IGNORE_CASE_REGEX = "(?i)";

	private final Integer userID;
	private final Account account;

	private VKMessageContext(Integer userId, Account account) {
		this.userID = userId;
		this.account = account;
	}

	public static VKMessageContext newContext(Integer userId, Account account) {
		return new VKMessageContext(userId, account);
	}

	@Override
	public Map<String, String> getPlaceholders() {
		LinkUserInfo vkLinkUserInfo = account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null)
				.getLinkUserInfo();

		Map<String, String> accountPlaceholders = Stream
				.of(CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%name%", account.getName()),
						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%nick%", account.getName()),
						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%account_ip%", account.getLastIpAddress()),
						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%account_last_ip%", account.getLastIpAddress()),
						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%vk_id%",
								String.valueOf(vkLinkUserInfo.getLinkUserId())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		Map<String, String> vkUserPlaceholders = new HashMap<>();

		try {
			List<GetResponse> userInfoResponses = CLIENT.users().get(ACTOR).userIds(String.valueOf(userID)).execute();
			if (!userInfoResponses.isEmpty()) {
				GetResponse userInfoResponse = userInfoResponses.get(0);
				vkUserPlaceholders = Stream
						.of(CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%user_screen_name%",
								userInfoResponse.getScreenName()),
								CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%user_first_name%",
										userInfoResponse.getFirstName()),
								CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%user_last_name%",
										userInfoResponse.getLastName()),
								CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%user_id%", String.valueOf(userID)))
						.map(entry -> {
							if (entry.getValue() == null)
								return CollectionUtils.newEntry(entry.getKey(), "");
							return entry;
						}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
			}
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}

		accountPlaceholders.putAll(vkUserPlaceholders);
		return accountPlaceholders;
	}

}

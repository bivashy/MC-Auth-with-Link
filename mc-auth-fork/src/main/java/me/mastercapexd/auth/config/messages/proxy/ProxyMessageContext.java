package me.mastercapexd.auth.config.messages.proxy;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.messages.MessageContext;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.utils.CollectionUtils;

public class ProxyMessageContext implements MessageContext {
	private static final String IGNORE_CASE_REGEX = "(?i)";
	private final Account account;

	public ProxyMessageContext(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public Map<String, String> getPlaceholders() {
		LinkUserInfo vkLinkUserInfo = account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null)
				.getLinkUserInfo();

		Map<String, String> placeholders = Stream
				.of(CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%account_name%", account.getName()),

						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%account_vk_id%",
								String.valueOf(vkLinkUserInfo.getIdentificator().asNumber())),

						CollectionUtils.newEntry(IGNORE_CASE_REGEX + "%account_last_ip%", account.getLastIpAddress()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		return placeholders;
	}

}

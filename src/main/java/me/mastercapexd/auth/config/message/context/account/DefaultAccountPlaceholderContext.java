package me.mastercapexd.auth.config.message.context.account;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.context.placeholder.MessagePlaceholderContext;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;

public class DefaultAccountPlaceholderContext extends MessagePlaceholderContext {
	protected final Account account;

	public DefaultAccountPlaceholderContext(Account account) {
		this.account = account;
		registerPlaceholderProvider(
				PlaceholderProvider.of(account.getName(), "%name%", "%nick%", "%account_name%", "%account_nick%", "%correct%"));
		registerPlaceholderProvider(
				PlaceholderProvider.of(account.getLastIpAddress(), "%account_ip%", "%account_last_ip%"));
	}
}

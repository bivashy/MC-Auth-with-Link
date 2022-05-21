package me.mastercapexd.auth.config.message.messenger.context;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.context.account.DefaultAccountPlaceholderContext;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;

public class MessengerPlaceholderContext extends DefaultAccountPlaceholderContext {
	protected LinkUser linkUser;
	public MessengerPlaceholderContext(Account account, LinkType linkType, String linkName) {
		super(account);
		linkUser = account.findFirstLinkUser((user) -> user.getLinkType().equals(linkType))
				.orElseThrow(() -> new NullPointerException());
		registerPlaceholderProvider(PlaceholderProvider.of(linkUser.getLinkUserInfo().getIdentificator().asString(),
				"%" + linkName + "_id%"));
	}
}

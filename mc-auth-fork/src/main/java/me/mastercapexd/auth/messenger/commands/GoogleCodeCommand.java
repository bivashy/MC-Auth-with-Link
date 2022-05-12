package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleCodeCommand implements OrphanCommand {
	@Dependency
	private ProxyPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@GoogleUse
	@Default
	public void googleCode(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account, Integer code) {
		LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
			GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, AccountFactory.DEFAULT_GOOGLE_KEY);
			account.addLinkUser(googleLinkUser);
			return googleLinkUser;
		});

		String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();
		if (linkUserKey == null || linkUserKey.equals(AccountFactory.DEFAULT_GOOGLE_KEY) || linkUserKey.isEmpty()) {
			actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-account-not-have-google"));
			return;
		}

		if (!Auth.getLinkEntryAuth().hasLinkUser(account.getId(), GoogleLinkType.getInstance())) {
			actorWrapper.reply(linkType.getLinkMessages().getStringMessage("code-not-need-enter"));
			return;
		}

		if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(), code)) {
			actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-valid"));
			Auth.removeAccount(account.getId());
			account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryDealership().createContext(account));
			return;
		}
		actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-not-valid"));
	}
}

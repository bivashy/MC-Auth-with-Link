package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleUnlinkCommand implements OrphanCommand {
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@GoogleUse
	@Default
	public void unlink(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
		LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).get();

		String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();
		if (linkUserKey == null || linkUserKey.equals(AccountFactory.DEFAULT_GOOGLE_KEY) || linkUserKey.isEmpty()) {
			actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlink-not-have-google"));
			return;
		}
		actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlinked"));
		linkUser.getLinkUserInfo().setIdentificator(GoogleLinkType.getInstance().getDefaultIdentificator());
		accountStorage.saveOrUpdateAccount(account);
	}
}

package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "googleunlink", "google unlink", "gunlink" })
public class GoogleUnlinkCommand {
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@GoogleUse
	@Default
	public void unlink(ProxyPlayer player) {
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}

			LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
			if (linkUser == null || linkUser.getLinkUserInfo().getIdentificator().asString().isEmpty()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-unlink-not-exists"));
				return;
			}
			player.sendMessage(config.getProxyMessages().getStringMessage("google-unlinked"));
			linkUser.getLinkUserInfo().getIdentificator().setString(GoogleLinkType.NULL_KEY);
			accountStorage.saveOrUpdateAccount(account);
		});
	}
}

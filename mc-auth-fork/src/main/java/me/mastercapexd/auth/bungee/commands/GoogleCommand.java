package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command("google")
public class GoogleCommand {
	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@GoogleUse
	public void linkGoogle(ProxyPlayer player) {
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}
			String key = plugin.getGoogleAuthenticator().createCredentials().getKey();
			LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER)
					.orElse(new GoogleLinkUser(account, key));
			if (linkUser == null || linkUser.getLinkUserInfo().getIdentificator().asString().isEmpty()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-generated")
						.replaceAll("(?i)%google_key%", key));
				linkUser.getLinkUserInfo().getIdentificator().setString(key);
			} else {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-regenerated")
						.replace("(?i)%google_key%", key));
				linkUser.getLinkUserInfo().getIdentificator().setString(key);
			}
			accountStorage.saveOrUpdateAccount(account);
		});
	}
}

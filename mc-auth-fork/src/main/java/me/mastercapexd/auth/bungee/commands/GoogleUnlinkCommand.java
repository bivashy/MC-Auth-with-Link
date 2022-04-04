package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
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
				player.sendMessage(config.getBungeeMessages().getStringMessage("account-not-found"));
				return;
			}

			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				player.sendMessage(config.getBungeeMessages().getStringMessage("google-unlink-not-exists"));
				return;
			}
			player.sendMessage(config.getBungeeMessages().getStringMessage("google-unlinked"));
			account.setGoogleKey(null);
			accountStorage.saveOrUpdateAccount(account);
		});
	}
}

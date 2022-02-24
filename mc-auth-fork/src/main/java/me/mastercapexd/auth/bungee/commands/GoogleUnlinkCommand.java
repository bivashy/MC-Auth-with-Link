package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
	public void unlink(ProxiedPlayer player) {
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}

			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				player.sendMessage(config.getBungeeMessages().getMessage("google-unlink-not-exists"));
				return;
			}
			player.sendMessage(config.getBungeeMessages().getMessage("google-unlinked"));
			account.setGoogleKey(null);
			accountStorage.saveOrUpdateAccount(account);
		});
	}
}

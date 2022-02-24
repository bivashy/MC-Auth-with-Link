package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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

	@SuppressWarnings("deprecation")
	@Default
	@GoogleUse
	public void linkGoogle(ProxiedPlayer player) {
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			String key = plugin.getGoogleAuthenticator().createCredentials().getKey();
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				player.sendMessage(config.getBungeeMessages().getStringMessage("google-generated")
						.replaceAll("(?i)%google_key%", key));
				account.setGoogleKey(key);
			} else {
				player.sendMessage(config.getBungeeMessages().getStringMessage("google-regenerated")
						.replace("(?i)%google_key%", key));
				account.setGoogleKey(key);
			}
			accountStorage.saveOrUpdateAccount(account);
		});
	}
}

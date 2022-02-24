package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "googlecode", "gcode" })
public class GoogleCodeCommand {
	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void defaultCommand(ProxiedPlayer player) {
		player.sendMessage(config.getBungeeMessages().getMessage("google-code-not-enough-arguments"));
	}

	@GoogleUse
	@Command({ "googlecode", "gcode", "google code" })
	public void googleCode(ProxiedPlayer player, Integer code) {
		String playerId = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(playerId).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				player.sendMessage(config.getBungeeMessages().getMessage("google-code-not-exists"));
				return;
			}
			if (!Auth.hasGoogleAuthAccount(playerId)) {
				player.sendMessage(config.getBungeeMessages().getMessage("google-code-not-need-enter"));
				return;
			}
			if (plugin.getGoogleAuthenticator().authorize(account.getGoogleKey(), code)) {
				player.sendMessage(config.getBungeeMessages().getMessage("google-code-entered"));
				Auth.removeGoogleAuthAccount(playerId);
				Auth.removeAccount(account.getId());
				Connector.connectOrKick(player, config.findServerInfo(config.getGameServers()),
						config.getBungeeMessages().getMessage("game-servers-connection-refused"));
			} else {
				player.sendMessage(config.getBungeeMessages().getMessage("google-code-wrong-code"));
			}
		});
	}
}

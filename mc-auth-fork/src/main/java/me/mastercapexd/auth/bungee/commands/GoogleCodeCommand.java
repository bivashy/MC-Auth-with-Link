package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
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
	public void defaultCommand(ProxyPlayer player) {
		player.sendMessage(config.getProxyMessages().getStringMessage("google-code-not-enough-arguments"));
	}

	@GoogleUse
	@Command({ "googlecode", "gcode", "google code" })
	public void googleCode(ProxyPlayer player, Integer code) {
		String playerId = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(playerId).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-code-not-exists"));
				return;
			}
			if (!Auth.hasGoogleAuthAccount(playerId)) {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-code-not-need-enter"));
				return;
			}
			if (plugin.getGoogleAuthenticator().authorize(account.getGoogleKey(), code)) {
				player.sendMessage(config.getProxyMessages().getStringMessage("google-code-entered"));
				Auth.removeGoogleAuthAccount(playerId);
				Auth.removeAccount(account.getId());
				player.sendTo(config.findServerInfo(config.getGameServers()).asProxyServer());
				return;
			}
			player.sendMessage(config.getProxyMessages().getStringMessage("google-code-wrong-code"));
		});
	}
}

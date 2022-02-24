package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.commands.exception.BungeeSendableException;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command("logout")
public class LogoutCommand {
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void logout(ProxiedPlayer player) {
		String id = config.getActiveIdentifierType().getId(player);
		if (Auth.hasAccount(id)) 
			throw new BungeeSendableException(config.getBungeeMessages().getStringMessage("already-logged-out"));

		accountStorage.getAccount(id).thenAccept(account -> {
			account.logout(config.getSessionDurability());
			accountStorage.saveOrUpdateAccount(account);
			Auth.addAccount(account);
			player.sendMessage(config.getBungeeMessages().getMessage("logout-success"));
			Connector.connectOrKick(player, config.findServerInfo(config.getAuthServers()),
					config.getBungeeMessages().getMessage("auth-servers-connection-refused"));
		});
	}
}
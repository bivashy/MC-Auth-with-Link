
package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.events.LoginEvent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class AuthCommand extends Command {

	private final Plugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public AuthCommand(Plugin plugin, PluginConfig config, AccountStorage accountStorage) {
		super("auth");
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("auth.admin")) {
			sender.sendMessage(config.getBungeeMessages().getMessage("no-permission"));
			return;
		}
		if (args.length == 0) {
			accountStorage.getAllAccounts().thenAccept(accounts -> {
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("info-registered").replace("%players%",
						accounts.size() + ""));
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("info-auth").replace("%players%",
						Auth.getAccountIds().size() + ""));
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("info-version").replace("%version%",
						plugin.getDescription().getVersion()));
			});
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("force")) {
				String playerName = args[1];
				ProxiedPlayer p = ProxyServer.getInstance().getPlayer(playerName);
				if (p == null) {
					sender.sendMessage(config.getBungeeMessages().getMessage("player-offline"));
					return;
				}
				String id = config.getActiveIdentifierType().getId(p);
				accountStorage.getAccount(id).thenAccept(account -> {
					if (account == null || !account.isRegistered()) {
						sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
						return;
					}
					ServerInfo gameServer = config.findServerInfo(config.getGameServers());
					LoginEvent loginEvent = new LoginEvent(account, true);
					ProxyServer.getInstance().getPluginManager().callEvent(loginEvent);
					if (loginEvent.isCancelled())
						return;
					Auth.removeAccount(id);
					Connector.connectOrKick(p, gameServer,
							config.getBungeeMessages().getMessage("game-servers-connection-refused"));
					sender.sendMessage(config.getBungeeMessages().getMessage("force-connect-success"));
				});
			}
		}
	}
}
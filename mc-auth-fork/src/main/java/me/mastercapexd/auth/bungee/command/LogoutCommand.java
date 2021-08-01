package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LogoutCommand extends Command {

	private final PluginConfig config;
	private final AccountStorage accountStorage;
	
	public LogoutCommand(PluginConfig config, AccountStorage accountStorage) {
		super("logout");
		this.config = config;
		this.accountStorage = accountStorage;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("players-only"));
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player);
		if (Auth.hasAccount(id)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("already-logged-out"));
			return;
		}
		
		accountStorage.getAccount(id)
		.thenAccept(account -> {
			account.logout(config.getSessionDurability());
			accountStorage.saveOrUpdateAccount(account);
			Auth.addAccount(account);
			sender.sendMessage(config.getBungeeMessages().getMessage("logout-success"));
			Connector.connectOrKick(player, config.findServerInfo(config.getAuthServers()), config.getBungeeMessages().getMessage("auth-servers-connection-refused"));
		});
	}
}
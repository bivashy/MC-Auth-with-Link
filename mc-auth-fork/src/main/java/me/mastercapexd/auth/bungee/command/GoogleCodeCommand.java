package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GoogleCodeCommand extends Command {
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public GoogleCodeCommand(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage) {
		super("googlecode", null, "gcode");
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("players-only"));
			return;
		}

		if (!config.getGoogleAuthenticatorSettings().isEnabled()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("google-disabled"));
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(config.getBungeeMessages().getMessage("google-code-not-enough-arguments"));
			return;
		}
		if (!isInteger(args[0]) || args[0].length() != 6) {
			sender.sendMessage(config.getBungeeMessages().getMessage("google-code-wrong-code"));
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player);
		Integer code = Integer.parseInt(args[0]);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("google-code-not-exists"));
				return;
			}
			if (!Auth.hasGoogleAuthAccount(id)) {
				sender.sendMessage(config.getBungeeMessages().getMessage("google-code-not-need-enter"));
				return;
			}
			if (plugin.getGoogleAuthenticator().authorize(account.getGoogleKey(), code)) {
				sender.sendMessage(config.getBungeeMessages().getMessage("google-code-entered"));
				Auth.removeGoogleAuthAccount(id);
				Auth.removeAccount(account.getId());
				Connector.connectOrKick(player, config.findServerInfo(config.getGameServers()),
						config.getBungeeMessages().getMessage("game-servers-connection-refused"));
			} else {
				sender.sendMessage(config.getBungeeMessages().getMessage("google-code-wrong-code"));
			}
		});
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

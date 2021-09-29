package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GoogleCommand extends Command {
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public GoogleCommand(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage) {
		super("google", null);
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
	}

	@SuppressWarnings("deprecation")
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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			String key = plugin.getGoogleAuthenticator().createCredentials().getKey();
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("google-generated")
						.replaceAll("(?i)%google_key%", key));
				account.setGoogleKey(key);
			} else {
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("google-regenerated")
						.replace("(?i)%players%", key));
				account.setGoogleKey(key);
			}
			accountStorage.saveOrUpdateAccount(account);
		});
	}

}

package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GoogleUnlinkCommand extends Command {
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public GoogleUnlinkCommand(PluginConfig config, AccountStorage accountStorage) {
		super("googleunlink", null, "gunlink");
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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("google-unlink-not-exists"));
				return;
			}
			sender.sendMessage(config.getBungeeMessages().getMessage("google-unlinked"));
			account.setGoogleKey(null);
			accountStorage.saveOrUpdateAccount(account);
		});
	}

}

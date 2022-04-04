package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.commands.parameters.DoublePassword;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "passchange", "changepass", "changepassword" })
public class ChangePasswordCommand {

	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void changePlayerPassword(ProxyPlayer sender, DoublePassword password) {
		String id = config.getActiveIdentifierType().getId(sender);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getStringMessage("account-not-found"));
				return;
			}

			if (!account.getHashType().checkHash(password.getOldPassword(), account.getPasswordHash())) {
				sender.sendMessage(config.getBungeeMessages().getStringMessage("wrong-old-password"));
				return;
			}

			if (account.getHashType() != config.getActiveHashType())
				account.setHashType(config.getActiveHashType());

			account.setPasswordHash(account.getHashType().hash(password.getNewPassword()));
			accountStorage.saveOrUpdateAccount(account);
			sender.sendMessage(config.getBungeeMessages().getStringMessage("change-success"));
		});
	}
}
package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.commands.annotations.Password;
import me.mastercapexd.auth.bungee.commands.exception.BungeeSendableException;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({ "passchange", "changepass", "changepassword" })
public class ChangePasswordCommand {

	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void changePlayerPassword(ProxiedPlayer sender, @Optional String oldPassword,
			@Optional @Password String newPassword) {
		if (oldPassword == null || newPassword == null)
			throw new BungeeSendableException(config.getBungeeMessages().getStringMessage("enter-new-password"));

		String id = config.getActiveIdentifierType().getId(sender);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}

			if (!account.getHashType().checkHash(oldPassword, account.getPasswordHash())) {
				sender.sendMessage(config.getBungeeMessages().getMessage("wrong-old-password"));
				return;
			}
			if (oldPassword.equals(newPassword)) {
				sender.sendMessage(config.getBungeeMessages().getMessage("nothing-to-change"));
				return;
			}
			if (account.getHashType() != config.getActiveHashType())
				account.setHashType(config.getActiveHashType());

			account.setPasswordHash(account.getHashType().hash(newPassword));
			accountStorage.saveOrUpdateAccount(account);
			sender.sendMessage(config.getBungeeMessages().getMessage("change-success"));
		});
	}
}
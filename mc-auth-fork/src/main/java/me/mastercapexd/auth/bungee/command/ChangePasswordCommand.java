package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.BungeeAccount;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChangePasswordCommand extends Command {

	private final PluginConfig config;
	private final AccountStorage accountStorage;
	
	public ChangePasswordCommand(PluginConfig config, AccountStorage accountStorage) {
		super("changepassword", null, "changepass");
		this.config = config;
		this.accountStorage = accountStorage;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("players-only"));
			return;
		}
		
		if (args.length < 2) {
			sender.sendMessage(config.getBungeeMessages().getMessage("enter-new-password"));
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player), oldPassword = args[0], newPassword = args[1];
		
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			
			if (oldPassword.equals(newPassword)) {
				sender.sendMessage(config.getBungeeMessages().getMessage("nothing-to-change"));
				return;
			}
			
			if (!account.getHashType().checkHash(oldPassword, account.getPasswordHash())) {
				sender.sendMessage(config.getBungeeMessages().getMessage("wrong-old-password"));
				return;
			}
			
			if (newPassword.length() < config.getPasswordMinLength()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("password-too-short"));
				return;
			}

			if (newPassword.length() > config.getPasswordMaxLength()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("password-too-long"));
				return;
			}
			
			BungeeAccount bungeeAccount = (BungeeAccount) account;
			if (account.getHashType() != config.getActiveHashType())
				bungeeAccount.setHashType(config.getActiveHashType());
			bungeeAccount.setPasswordHash(account.getHashType().hash(newPassword));
			accountStorage.saveOrUpdateAccount(account);
			sender.sendMessage(config.getBungeeMessages().getMessage("change-success"));
		});
	}
}
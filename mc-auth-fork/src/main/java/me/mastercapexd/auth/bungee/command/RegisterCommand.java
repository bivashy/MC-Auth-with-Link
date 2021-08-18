package me.mastercapexd.auth.bungee.command;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.SessionResult;
import me.mastercapexd.auth.bungee.events.RegisterEvent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RegisterCommand extends Command {

	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public RegisterCommand(PluginConfig config, AccountStorage accountStorage) {
		super("register", null, "reg");
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
		if (!Auth.hasAccount(id)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("already-logged-in"));
			return;
		}

		Account account = Auth.getAccount(id);
		if (account.isRegistered()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("account-exists"));
			return;
		}

		String password = null;
		if (args.length < 1) {
			sender.sendMessage(config.getBungeeMessages().getMessage("enter-password"));
			return;
		}

		password = args[0];

		if (config.isPasswordConfirmationEnabled()) {
			if (args.length < 2) {
				sender.sendMessage(config.getBungeeMessages().getMessage("confirm-password"));
				return;
			}

			if (!args[1].equals(password)) {
				sender.sendMessage(config.getBungeeMessages().getMessage("confirm-failed"));
				return;
			}
		}

		if (password.length() < config.getPasswordMinLength()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("password-too-short"));
			return;
		}

		if (password.length() > config.getPasswordMaxLength()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("password-too-long"));
			return;
		}

		if (!config.getPasswordPattern().matcher(password).matches()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("illegal-password-chars"));
			return;
		}

		SessionResult result = account.newSession(config.getActiveHashType(), password);
		if (result != SessionResult.REGISTER_SUCCESS)
			return;
		RegisterEvent registerEvent = new RegisterEvent(account);
		ProxyServer.getInstance().getPluginManager().callEvent(registerEvent);
		if (registerEvent.isCancelled())
			return;
		Auth.removeAccount(id);
		accountStorage.saveOrUpdateAccount(account);
		sender.sendMessage(config.getBungeeMessages().getMessage("register-success"));
		Connector.connectOrKick(player, config.findServerInfo(config.getGameServers()),
				config.getBungeeMessages().getMessage("game-servers-connection-refused"));
	}
}
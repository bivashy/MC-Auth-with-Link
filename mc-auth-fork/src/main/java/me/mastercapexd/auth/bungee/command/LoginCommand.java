package me.mastercapexd.auth.bungee.command;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.SessionResult;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.vk.builders.ConfirmationMessageBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LoginCommand extends Command {

	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public LoginCommand(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage) {
		super("login", null, "l");
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

		ProxiedPlayer player = (ProxiedPlayer) sender;
		String id = config.getActiveIdentifierType().getId(player);
		if (!Auth.hasAccount(id)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("already-logged-in"));
			return;
		}

		Account account = Auth.getAccount(id);
		if (!account.isRegistered()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
			return;
		}

		String password = null;
		if (args.length < 1) {
			sender.sendMessage(config.getBungeeMessages().getMessage("enter-password"));
			return;
		}

		password = args[0];

		SessionResult result = account.newSession(config.getActiveHashType(), password);
		if (result == SessionResult.LOGIN_WRONG_PASSWORD) {
			if (config.getPasswordAttempts() < 1) {
				sender.sendMessage(config.getBungeeMessages().getMessage("wrong-password"));
				return;
			}
			Auth.incrementAttempts(id);
			int attempts = Auth.getPlayerAttempts(id);
			if (attempts < config.getPasswordAttempts())
				sender.sendMessage(config.getBungeeMessages().getLegacyMessage("wrong-password").replace("%attempts%",
						(config.getPasswordAttempts() - attempts) + ""));
			else
				player.disconnect(config.getBungeeMessages().getMessage("attempts-limit"));
			return;
		}

		if (result == SessionResult.NEED_VK_CONFIRM)
			new ConfirmationMessageBuilder(Auth.getEntryAccount(id), plugin.getVKReceptioner()).execute();

		if (result != SessionResult.LOGIN_SUCCESS)
			return;
		accountStorage.saveOrUpdateAccount(account);
		sender.sendMessage(config.getBungeeMessages().getMessage("login-success"));
		Auth.removeAccount(id);
	}
}

package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.OtherPlayer;
import me.mastercapexd.auth.bungee.commands.annotations.Password;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command({ "auth", "authadmin", "adminauth" })
public class AuthCommand {

	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@SuppressWarnings("deprecation")
	@Default
	@CommandPermission("auth.admin")
	public void accountInfos(CommandSender sender) {
		accountStorage.getAllAccounts().thenAccept(accounts -> {
			sender.sendMessage(config.getBungeeMessages().getStringMessage("info-registered").replace("%players%",
					String.valueOf(accounts.size())));
			sender.sendMessage(config.getBungeeMessages().getStringMessage("info-auth").replaceAll("%players%",
					String.valueOf(Auth.getAccountIds().size())));
			sender.sendMessage(config.getBungeeMessages().getStringMessage("info-version").replace("%version%",
					plugin.getDescription().getVersion()));
		});
	}

	@Subcommand({ "force", "forcejoin", "fjoin" })
	@CommandPermission("auth.admin")
	public void forceEnter(CommandSender sender, @OtherPlayer ProxiedPlayer proxiedPlayer) {
		if (proxiedPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxiedPlayer);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			AuthenticationStepContext context = new DefaultAuthenticationStepContext(account);
			EnterServerAuthenticationStep enterServerAuthenticationStep = new EnterServerAuthenticationStep(context);
			enterServerAuthenticationStep.enterServer();
			sender.sendMessage(config.getBungeeMessages().getMessage("force-connect-success"));
		});
	}

	@Subcommand({ "changepassword", "changepass" })
	@CommandPermission("auth.admin")
	public void changePassword(CommandSender sender, @OtherPlayer ProxiedPlayer proxiedPlayer,
			@Password String newPlayerPassword) {
		if (proxiedPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxiedPlayer);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				sender.sendMessage(config.getBungeeMessages().getMessage("account-not-found"));
				return;
			}
			account.setPasswordHash(account.getHashType().hash(newPlayerPassword));
			accountStorage.saveOrUpdateAccount(account);
			sender.sendMessage(config.getBungeeMessages().getMessage("auth-change-success"));
		});
	}

	@Subcommand({ "reset", "resetaccount" })
	@CommandPermission("auth.admin")
	public void resetAccount(CommandSender sender, @OtherPlayer ProxiedPlayer proxiedPlayer) {
		if (proxiedPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxiedPlayer);
		accountStorage.deleteAccount(id);
		sender.sendMessage(config.getBungeeMessages().getMessage("auth-delete-success"));
	}

	@Subcommand("reload")
	@CommandPermission("auth.admin")
	public void reload(CommandSender sender) {
		plugin.getConfig().reload();
		sender.sendMessage(config.getBungeeMessages().getMessage("auth-reloaded"));
	}
}
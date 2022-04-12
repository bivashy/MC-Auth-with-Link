
package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.parameters.ArgumentProxyPlayer;
import me.mastercapexd.auth.bungee.commands.parameters.NewPassword;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.annotation.CommandPermission;
import revxrsal.commands.command.CommandActor;

@Command({ "auth", "authadmin", "adminauth" })
@CommandPermission("auth.admin")
public class AuthCommand {

	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void accountInfos(CommandActor commandActor) {
		accountStorage.getAllAccounts().thenAccept(accounts -> {
			commandActor.reply(config.getProxyMessages().getStringMessage("info-registered").replace("%players%",
					String.valueOf(accounts.size())));
			commandActor.reply(config.getProxyMessages().getStringMessage("info-auth").replaceAll("%players%",
					String.valueOf(Auth.getAccountIds().size())));
			commandActor.reply(config.getProxyMessages().getStringMessage("info-version").replace("%version%",
					plugin.getDescription().getVersion()));
		});
	}

	@Subcommand({ "force", "forcejoin", "fjoin" })
	public void forceEnter(CommandActor commandActor, ArgumentProxyPlayer proxyPlayer) {
		if (proxyPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxyPlayer);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				commandActor.reply(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}
			AuthenticationStepContext context = new DefaultAuthenticationStepContext(account);
			EnterServerAuthenticationStep enterServerAuthenticationStep = new EnterServerAuthenticationStep(context);
			enterServerAuthenticationStep.enterServer();
			commandActor.reply(config.getProxyMessages().getStringMessage("force-connect-success"));
		});
	}

	@Subcommand({ "changepassword", "changepass" })
	public void changePassword(CommandActor actor, ArgumentProxyPlayer proxyPlayer,
			NewPassword newPlayerPassword) {
		if (proxyPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxyPlayer);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				actor.reply(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}
			account.setPasswordHash(account.getHashType().hash(newPlayerPassword.getNewPassword()));
			accountStorage.saveOrUpdateAccount(account);
			actor.reply(config.getProxyMessages().getStringMessage("auth-change-success"));
		});
	}

	@Subcommand({ "reset", "resetaccount", "deleteaccount" })
	public void resetAccount(CommandActor actor, ArgumentProxyPlayer proxyPlayer) {
		if (proxyPlayer == null)
			return;
		String id = config.getActiveIdentifierType().getId(proxyPlayer);
		accountStorage.deleteAccount(id);
		actor.reply(config.getProxyMessages().getStringMessage("auth-delete-success"));
	}

	@Subcommand("reload")
	public void reload(CommandActor actor) {
		plugin.getConfig().reload();
		actor.reply(config.getProxyMessages().getStringMessage("auth-reloaded"));
	}
}
package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.bungee.message.BungeeMultiProxyComponent;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "login", "l" })
public class LoginCommand {
	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@SuppressWarnings("deprecation")
	@Default
	@AuthenticationStepCommand(stepName = LoginAuthenticationStep.STEP_NAME)
	public void login(ProxyPlayer player, Account account, String password) {
		String id = account.getId();
		AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();

		if (!account.getHashType().checkHash(password, account.getPasswordHash())) {
			if (config.getPasswordAttempts() < 1) {
				player.sendMessage(config.getBungeeMessages().getStringMessage("wrong-password"));
				return;
			}
			Auth.incrementAttempts(id);
			int attempts = Auth.getPlayerAttempts(id);
			if (attempts < config.getPasswordAttempts()) {
				player.sendMessage(config.getBungeeMessages().getStringMessage("wrong-password")
						.replaceAll("%attempts%", String.valueOf(config.getPasswordAttempts() - attempts)));
				return;
			}
			player.disconnect(config.getBungeeMessages().getStringMessage("attempts-limit"));
			return;
		}

		if (account.getHashType() != config.getActiveHashType()) {
			account.setHashType(config.getActiveHashType());
			account.setPasswordHash(config.getActiveHashType().hash(password));
		}

		currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
		String stepName = plugin.getConfig()
				.getAuthenticationStepName(account.getCurrentConfigurationAuthenticationStepCreatorIndex());
		account.nextAuthenticationStep(
				plugin.getAuthenticationContextFactoryDealership().createContext(stepName, account));
		player.sendMessage(config.getBungeeMessages().getStringMessage("login-success"));
	}
}
package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "login", "l" })
public class LoginCommand {
	@Dependency
	private ProxyPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@AuthenticationStepCommand(stepName = LoginAuthenticationStep.STEP_NAME)
	public void login(ProxyPlayer player, Account account, String password) {
		String id = account.getId();
		AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();

		if (!account.getHashType().checkHash(password, account.getPasswordHash())) {
			if (config.getPasswordAttempts() < 1) {
				player.sendMessage(config.getProxyMessages().getStringMessage("wrong-password"));
				return;
			}
			Auth.incrementAttempts(id);
			int attempts = Auth.getPlayerAttempts(id);
			if (attempts < config.getPasswordAttempts()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("wrong-password").replaceAll("%attempts%",
						String.valueOf(config.getPasswordAttempts() - attempts)));
				return;
			}
			player.disconnect(config.getProxyMessages().getStringMessage("attempts-limit"));
			return;
		}

		if (account.getHashType() != config.getActiveHashType()) {
			account.setHashType(config.getActiveHashType());
			account.setPasswordHash(config.getActiveHashType().hash(password));
		}

		currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
		account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryDealership().createContext(account));
		player.sendMessage(config.getProxyMessages().getStringMessage("login-success"));
	}
}
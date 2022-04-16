package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "reg", "register" })
public class RegisterCommand {

	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@AuthenticationStepCommand(stepName = RegisterAuthenticationStep.STEP_NAME)
	public void register(ProxyPlayer player, @AuthenticationAccount Account account,
			me.mastercapexd.auth.proxy.commands.parameters.RegisterPassword password) {
		AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();
		currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
		String stepName = plugin.getConfig()
				.getAuthenticationStepName(account.getCurrentConfigurationAuthenticationStepCreatorIndex());

		if (account.getHashType() != config.getActiveHashType())
			account.setHashType(config.getActiveHashType());
		account.setPasswordHash(account.getHashType().hash(password.getPassword()));

		accountStorage.saveOrUpdateAccount(account);

		account.nextAuthenticationStep(
				plugin.getAuthenticationContextFactoryDealership().createContext(stepName, account));

		player.sendMessage(config.getProxyMessages().getStringMessage("register-success"));
	}
}
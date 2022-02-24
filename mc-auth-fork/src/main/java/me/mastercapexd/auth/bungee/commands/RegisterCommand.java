package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.bungee.commands.annotations.Password;
import me.mastercapexd.auth.bungee.commands.annotations.RegisterAccount;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({"reg","register"})
public class RegisterCommand {

	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@AuthenticationStepCommand(stepName = RegisterAuthenticationStep.STEP_NAME)
	public void register(ProxiedPlayer player, @RegisterAccount Account account, @Password String password) {
		AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();
		currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
		String stepName = plugin.getConfig()
				.getAuthenticationStepName(account.getCurrentConfigurationAuthenticationStepCreatorIndex());
		account.nextAuthenticationStep(
				plugin.getAuthenticationContextFactoryDealership().createContext(stepName, account));
		accountStorage.saveOrUpdateAccount(account);
		player.sendMessage(config.getBungeeMessages().getMessage("register-success"));
	}
}
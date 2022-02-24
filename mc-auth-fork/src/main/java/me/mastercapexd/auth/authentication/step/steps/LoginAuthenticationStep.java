package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.AuthPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LoginAuthenticationStep extends AbstractAuthenticationStep {
	
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	public static final String STEP_NAME = "LOGIN";

	public LoginAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldPassToNextStep() {
		boolean canPass = authenticationStepContext.canPassToNextStep();
		if (canPass) {
			Account account = authenticationStepContext.getAccount();
			ProxiedPlayer player = account.getIdentifierType().getPlayer(account.getId());
			account.setLastIpAddress(player.getAddress().getHostString());
			account.setLastSessionStart(System.currentTimeMillis());
		}
		return canPass;
	}

	@Override
	public boolean shouldSkip() {
		return !Auth.hasAccount(authenticationStepContext.getAccount().getId()) || authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability());
	}

	public static class LoginAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public LoginAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public LoginAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new LoginAuthenticationStep(context);
		}
	}

}

package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RegisterAuthenticationStep extends AbstractAuthenticationStep {

	public static final String STEP_NAME = "REGISTER";
	private final boolean isRegistered;

	public RegisterAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
		isRegistered = context.getAccount().isRegistered();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldPassToNextStep() {
		boolean isCurrentAccountRegistered = authenticationStepContext.getAccount().isRegistered();
		if (!isRegistered && isCurrentAccountRegistered) {
			Account account = authenticationStepContext.getAccount();
			ProxiedPlayer player = account.getIdentifierType().getPlayer(account.getId());
			
			Auth.removeAccount(account.getId());;
			account.setLastIpAddress(player.getAddress().getHostString());
			account.setLastSessionStart(System.currentTimeMillis());
		}
		return isCurrentAccountRegistered;
	}

	@Override
	public boolean shouldSkip() {
		return authenticationStepContext.getAccount().isRegistered();
	}

	public static class RegisterAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public RegisterAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new RegisterAuthenticationStep(context);
		}
	}

}

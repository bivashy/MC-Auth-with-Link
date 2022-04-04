package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.AuthPlugin;

public class LoginAuthenticationStep extends AbstractAuthenticationStep {

	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	public static final String STEP_NAME = "LOGIN";

	public LoginAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
	}

	@Override
	public boolean shouldPassToNextStep() {
		return authenticationStepContext.canPassToNextStep();
	}

	@Override
	public boolean shouldSkip() {
		return !Auth.hasAccount(authenticationStepContext.getAccount().getId())
				|| authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability());
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

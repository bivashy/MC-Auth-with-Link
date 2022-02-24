package me.mastercapexd.auth.authentication.step;

import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

public abstract class AbstractAuthenticationStep implements AuthenticationStep {
	protected final String stepName;
	protected final AuthenticationStepContext authenticationStepContext;

	public AbstractAuthenticationStep(String stepName, AuthenticationStepContext authenticationStepContext) {
		this.stepName = stepName;
		this.authenticationStepContext = authenticationStepContext;
	}

	@Override
	public String getStepName() {
		return stepName;
	}

	@Override
	public AuthenticationStepContext getAuthenticationStepContext() {
		return authenticationStepContext;
	}
}

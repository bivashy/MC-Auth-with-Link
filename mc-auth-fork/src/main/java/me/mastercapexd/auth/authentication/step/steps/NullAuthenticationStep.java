package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;

public class NullAuthenticationStep extends AbstractAuthenticationStep {

	private static final String STEP_NAME = "NULL";

	public NullAuthenticationStep() {
		super(STEP_NAME, null);
	}

	@Override
	public boolean shouldPassToNextStep() {
		return true;
	}

	@Override
	public boolean shouldSkip() {
		return true;
	}

	public static class NullAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public NullAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public NullAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new NullAuthenticationStep();
		}
	}
}

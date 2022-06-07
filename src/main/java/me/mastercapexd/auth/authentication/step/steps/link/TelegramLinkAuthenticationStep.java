package me.mastercapexd.auth.authentication.step.steps.link;

import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.link.entryuser.telegram.TelegramLinkEntryUser;

public class TelegramLinkAuthenticationStep extends MessengerAuthenticationStep {
	public static final String STEP_NAME = "TELEGRAM_LINK";

	public TelegramLinkAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context, new TelegramLinkEntryUser(context.getAccount()));
	}

	public static class TelegramLinkAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public TelegramLinkAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new TelegramLinkAuthenticationStep(context);
		}
	}
}

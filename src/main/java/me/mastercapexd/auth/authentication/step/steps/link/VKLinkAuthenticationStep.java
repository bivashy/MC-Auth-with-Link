package me.mastercapexd.auth.authentication.step.steps.link;

import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.link.entryuser.vk.VKLinkEntryUser;

public class VKLinkAuthenticationStep extends MessengerAuthenticationStep {
	public static final String STEP_NAME = "VK_LINK";

	public VKLinkAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context, new VKLinkEntryUser(context.getAccount()));
	}

	public static class VKLinkAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public VKLinkAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new VKLinkAuthenticationStep(context);
		}
	}
}

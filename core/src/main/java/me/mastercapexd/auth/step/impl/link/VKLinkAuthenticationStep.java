package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class VKLinkAuthenticationStep extends MessengerAuthenticationStep {
    public static final String STEP_NAME = "VK_LINK";

    public VKLinkAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context, VKLinkType.getInstance(), VKLinkType.LINK_USER_FILTER);
    }

    public static class VKLinkAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public VKLinkAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new VKLinkAuthenticationStep(context);
        }
    }
}

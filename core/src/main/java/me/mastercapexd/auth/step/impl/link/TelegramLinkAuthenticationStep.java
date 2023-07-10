package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class TelegramLinkAuthenticationStep extends MessengerAuthenticationStep {
    public static final String STEP_NAME = "TELEGRAM_LINK";

    public TelegramLinkAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context, TelegramLinkType.getInstance(), TelegramLinkType.LINK_USER_FILTER);
    }

    public static class TelegramLinkAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public TelegramLinkAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new TelegramLinkAuthenticationStep(context);
        }
    }
}

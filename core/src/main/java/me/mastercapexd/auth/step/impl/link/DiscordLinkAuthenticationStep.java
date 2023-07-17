package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class DiscordLinkAuthenticationStep extends MessengerAuthenticationStep {
    public static final String STEP_NAME = "DISCORD_LINK";

    public DiscordLinkAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context, DiscordLinkType.getInstance(), DiscordLinkType.LINK_USER_FILTER);
    }

    @Override
    protected void sendConfirmationMessage(Account account, LinkType linkType, LinkUserInfo linkUserInfo) {
        // TODO: Send confirmation message to channel or private messages, depending on configuration
    }

    public static class DiscordLinkAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public DiscordLinkAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new DiscordLinkAuthenticationStep(context);
        }
    }
}


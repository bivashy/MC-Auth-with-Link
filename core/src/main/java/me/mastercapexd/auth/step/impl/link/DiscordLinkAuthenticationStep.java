package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;
import com.bivashy.messenger.common.keyboard.Keyboard;
import com.bivashy.messenger.discord.message.DiscordMessage;

import me.mastercapexd.auth.hooks.DiscordHook;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class DiscordLinkAuthenticationStep extends MessengerAuthenticationStep {
    public static final String STEP_NAME = "DISCORD_LINK";
    private final DiscordHook discordHook = AuthPlugin.instance().getHook(DiscordHook.class);

    public DiscordLinkAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context, DiscordLinkType.getInstance(), DiscordLinkType.LINK_USER_FILTER);
    }

    @Override
    protected void sendConfirmationMessage(Account account, LinkType linkType, LinkUserInfo linkUserInfo) {
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("confirmation", "%name%", account.getName());

        linkType.newMessageBuilder(linkType.getSettings().getMessages().getMessage("enter-message", linkType.newMessageContext(account)))
                .keyboard(keyboard)
                .build()
                .as(DiscordMessage.class)
                .send(builder -> discordHook.getJDA()
                        .openPrivateChannelById(linkUserInfo.getIdentificator().asNumber())
                        .queue(channel -> channel.sendMessage(builder.build()).queue()));
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


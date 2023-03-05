package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;
import com.bivashy.auth.api.step.MessageableAuthenticationStep;

import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.link.google.GoogleLinkEntryUser;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.step.AuthenticationStepTemplate;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class GoogleCodeAuthenticationStep extends AuthenticationStepTemplate implements MessageableAuthenticationStep {
    public static final String STEP_NAME = "GOOGLE_LINK";
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private final LinkEntryUser entryUser;

    public GoogleCodeAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
        entryUser = new GoogleLinkEntryUser(context.getAccount());
    }

    @Override
    public boolean shouldPassToNextStep() {
        return entryUser.isConfirmed();
    }

    @Override
    public boolean shouldSkip() {
        Account account = authenticationStepContext.getAccount();

        if (!PLUGIN.getConfig().getGoogleAuthenticatorSettings().isEnabled())
            return true;

        if (PLUGIN.getLinkEntryBucket().hasLinkUser(account.getPlayerId(), GoogleLinkType.getInstance()))
            return true;

        if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability()))
            return true;

        return account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).map(linkUser -> {
            LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

            if (!linkUserInfo.isConfirmationEnabled() || linkUser.isIdentifierDefaultOrNull())
                return true;

            PLUGIN.getLinkEntryBucket().addLinkUser(entryUser);
            return false;
        }).orElse(true);
    }

    @Override
    public void process(ServerPlayer player) {
        Account account = authenticationStepContext.getAccount();
        PluginConfig config = AuthPlugin.instance().getConfig();
        Messages<ServerComponent> googleMessages = config.getServerMessages().getSubMessages("google");
        player.sendMessage(googleMessages.getMessage("need-code-chat", new ServerMessageContext(account)));
        AuthPlugin.instance()
                .getCore()
                .createTitle(googleMessages.getMessage("need-code-title"))
                .subtitle(googleMessages.getMessage("need-code-subtitle"))
                .stay(120)
                .send(player);
    }

    public static class GoogleLinkAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public GoogleLinkAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new GoogleCodeAuthenticationStep(context);
        }
    }
}

package me.mastercapexd.auth.step.impl;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStepContext;
import com.bivashy.auth.api.step.MessageableAuthenticationStep;

import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.step.AuthenticationStepTemplate;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class LoginAuthenticationStep extends AuthenticationStepTemplate implements MessageableAuthenticationStep {
    public static final String STEP_NAME = "LOGIN";
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();

    public LoginAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return authenticationStepContext.canPassToNextStep();
    }

    @Override
    public boolean shouldSkip() {
        return !PLUGIN.getAuthenticatingAccountBucket().isAuthenticating(authenticationStepContext.getAccount()) ||
                authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability());
    }

    @Override
    public void process(ServerPlayer player) {
        Account account = authenticationStepContext.getAccount();
        PluginConfig config = PLUGIN.getConfig();
        player.sendMessage(config.getServerMessages().getMessage("login-chat", new ServerMessageContext(account)));
        AuthPlugin.instance()
                .getCore()
                .createTitle(config.getServerMessages().getMessage("login-title"))
                .subtitle(config.getServerMessages().getMessage("login-subtitle"))
                .stay(120)
                .send(player);
    }

    public static class LoginAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public LoginAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public LoginAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new LoginAuthenticationStep(context);
        }
    }
}

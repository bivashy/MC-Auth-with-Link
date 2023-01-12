package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.MessageableAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class LoginAuthenticationStep extends AbstractAuthenticationStep implements MessageableAuthenticationStep {

    public static final String STEP_NAME = "LOGIN";
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    public LoginAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return authenticationStepContext.canPassToNextStep();
    }

    @Override
    public boolean shouldSkip() {
        return !Auth.hasAccount(authenticationStepContext.getAccount().getPlayerId()) || authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability());
    }

    @Override
    public void process(ProxyPlayer player) {
        Account account = authenticationStepContext.getAccount();
        PluginConfig config = ProxyPlugin.instance().getConfig();
        player.sendMessage(config.getProxyMessages().getMessage("login-chat", new ProxyMessageContext(account)));
        ProxyPlugin.instance().getCore().createTitle(config.getProxyMessages().getStringMessage("login-title")).subtitle(config.getProxyMessages().getStringMessage("login-subtitle")).stay(120).send(player);
    }

    public static class LoginAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
        public LoginAuthenticationStepCreator() {
            super(STEP_NAME);
        }

        @Override
        public LoginAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new LoginAuthenticationStep(context);
        }
    }

}

package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class EnterAuthServerAuthenticationStep extends AbstractAuthenticationStep {
    public static final String STEP_NAME = "ENTER_AUTH_SERVER";
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    public EnterAuthServerAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return true;
    }

    @Override
    public boolean shouldSkip() {
        if (!Auth.hasAccount(authenticationStepContext.getAccount().getId()) ||
                authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability()))
            return true;
        authenticationStepContext.getAccount()
                .getPlayer().ifPresent(PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getAuthServers()).asProxyServer()::sendPlayer);
        return true;
    }

    public static class EnterAuthServerAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
        public EnterAuthServerAuthenticationStepCreator() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new EnterAuthServerAuthenticationStep(context);
        }
    }
}

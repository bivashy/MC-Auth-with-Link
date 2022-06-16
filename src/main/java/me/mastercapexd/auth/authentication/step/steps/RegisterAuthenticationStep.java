package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class RegisterAuthenticationStep extends AbstractAuthenticationStep {

    public static final String STEP_NAME = "REGISTER";
    private final boolean isRegistered;

    public RegisterAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
        isRegistered = context.getAccount().isRegistered();
    }

    @Override
    public boolean shouldPassToNextStep() {
        boolean isCurrentAccountRegistered = authenticationStepContext.getAccount().isRegistered();
        if (!isRegistered && isCurrentAccountRegistered) {
            Account account = authenticationStepContext.getAccount();
            ProxyPlayer player = account.getPlayer().get();

            Auth.removeAccount(account.getId());
            account.setLastIpAddress(player.getRemoteAddress().getHostString());
            account.setLastSessionStart(System.currentTimeMillis());
        }
        return isCurrentAccountRegistered;
    }

    @Override
    public boolean shouldSkip() {
        return authenticationStepContext.getAccount().isRegistered();
    }

    public static class RegisterAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
        public RegisterAuthenticationStepCreator() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new RegisterAuthenticationStep(context);
        }
    }

}

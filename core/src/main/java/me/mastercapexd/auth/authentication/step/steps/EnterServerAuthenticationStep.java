package me.mastercapexd.auth.authentication.step.steps;

import java.util.Optional;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

public class EnterServerAuthenticationStep extends AbstractAuthenticationStep {
    public static final String STEP_NAME = "ENTER_SERVER";
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    public EnterServerAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return true;
    }

    @Override
    public boolean shouldSkip() {
        enterServer();
        return true;
    }

    public void enterServer() {
        Account account = authenticationStepContext.getAccount();
        PLUGIN.getAuthenticatingAccountBucket().removeAuthorizingAccount(account);
        Optional<ProxyPlayer> playerOptional = account.getPlayer();
        account.setLastSessionStartTimestamp(System.currentTimeMillis());
        playerOptional.map(ProxyPlayer::getPlayerIp).ifPresent(account::setLastIpAddress);
        PLUGIN.getAccountStorage().saveOrUpdateAccount(account);
        if (!playerOptional.isPresent())
            return;
        ProxyPlayer player = playerOptional.get();
        Server connectServer = PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getGameServers()).asProxyServer();
        player.sendTo(connectServer);
    }

    public static class EnterServerAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
        public EnterServerAuthenticationStepCreator() {
            super(STEP_NAME);
        }

        @Override
        public EnterServerAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new EnterServerAuthenticationStep(context);
        }
    }

}

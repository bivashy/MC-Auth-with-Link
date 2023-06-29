package me.mastercapexd.auth.step.impl;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.step.AuthenticationStepTemplate;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class EnterServerAuthenticationStep extends AuthenticationStepTemplate {
    public static final String STEP_NAME = "ENTER_SERVER";
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();

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
        PLUGIN.getAuthenticatingAccountBucket().removeAuthenticatingAccount(account);
        Optional<ServerPlayer> playerOptional = account.getPlayer();
        account.setLastSessionStartTimestamp(System.currentTimeMillis());
        playerOptional.map(ServerPlayer::getPlayerIp).ifPresent(account::setLastIpAddress);
        PLUGIN.getAccountDatabase().saveOrUpdateAccount(account);
        if (!playerOptional.isPresent())
            return;
        ServerPlayer player = playerOptional.get();
        PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getGameServers()).asProxyServer().sendPlayer(player);
    }

    public static class EnterServerAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public EnterServerAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public EnterServerAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new EnterServerAuthenticationStep(context);
        }
    }
}

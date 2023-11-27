package me.mastercapexd.auth.step.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.server.ConfigurationServer;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.step.AuthenticationStepTemplate;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class EnterAuthServerAuthenticationStep extends AuthenticationStepTemplate {
    public static final String STEP_NAME = "ENTER_AUTH_SERVER";
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();

    public EnterAuthServerAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return true;
    }

    @Override
    public boolean shouldSkip() {
        if (!PLUGIN.getAuthenticatingAccountBucket().isAuthenticating(authenticationStepContext.getAccount()) ||
                authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability()) ||
                PLUGIN.getPendingPremiumAccountBucket().isPendingPremium(authenticationStepContext.getAccount()) ||
                authenticationStepContext.getAccount().isPremium())
            return true;
        tryToConnect(true);
        return true;
    }

    private void tryToConnect(boolean shouldTryAgain) {
        Optional<ServerPlayer> playerOptional = authenticationStepContext.getAccount().getPlayer();
        if (!playerOptional.isPresent())
            return;
        ServerPlayer player = playerOptional.get();
        ConfigurationServer foundServer = PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getAuthServers());
        Optional<ProxyServer> currentServerOptional = player.getCurrentServer();
        if (!currentServerOptional.isPresent()) {
            if (shouldTryAgain)
                PLUGIN.getCore().schedule(() -> tryToConnect(false), 3, TimeUnit.SECONDS);
            return;
        }
        if (currentServerOptional.get().getServerName().equals(foundServer.getId()))
            return;
        foundServer.asProxyServer().sendPlayer(player);
    }

    public static class EnterAuthServerAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public EnterAuthServerAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new EnterAuthServerAuthenticationStep(context);
        }
    }
}

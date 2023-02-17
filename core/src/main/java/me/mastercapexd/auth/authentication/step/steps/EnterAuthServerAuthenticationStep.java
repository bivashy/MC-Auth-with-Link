package me.mastercapexd.auth.authentication.step.steps;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

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
        if (!Auth.hasAccount(authenticationStepContext.getAccount().getPlayerId()) ||
                authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability()))
            return true;
        tryToConnect(true);
        return true;
    }

    private void tryToConnect(boolean shouldTryAgain) {
        Optional<ProxyPlayer> playerOptional = authenticationStepContext.getAccount().getPlayer();
        if (!playerOptional.isPresent())
            return;
        ProxyPlayer player = playerOptional.get();
        ConfigurationServer foundServer = PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getAuthServers());
        Optional<Server> currentServerOptional = player.getCurrentServer();
        if (!currentServerOptional.isPresent()) {
            if (shouldTryAgain)
                PLUGIN.getCore().schedule(PLUGIN, () -> tryToConnect(false), 3, TimeUnit.SECONDS);
            return;
        }
        if (currentServerOptional.get().getServerName().equals(foundServer.getId()))
            return;
        foundServer.asProxyServer().sendPlayer(player);
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

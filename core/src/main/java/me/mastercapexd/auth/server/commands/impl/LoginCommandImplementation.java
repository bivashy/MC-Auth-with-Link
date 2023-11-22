package me.mastercapexd.auth.server.commands.impl;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.event.AccountTryLoginEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;

public class LoginCommandImplementation {
    private final AuthPlugin plugin;
    private final PluginConfig config;

    public LoginCommandImplementation(AuthPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void performLogin(ServerPlayer player, Account account, String password) {
        AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();

        HashInput passwordInput = HashInput.of(password);
        boolean isWrongPassword = !account.getCryptoProvider().matches(passwordInput, account.getPasswordHash());
        plugin.getEventBus().publish(AccountTryLoginEvent.class, account, isWrongPassword, !isWrongPassword).thenAccept(tryLoginEventPostResult -> {
            if (tryLoginEventPostResult.getEvent().isCancelled())
                return;

            if (!account.getCryptoProvider().getIdentifier().equals(config.getActiveHashType().getIdentifier())) {
                account.setCryptoProvider(config.getActiveHashType());
                account.setPasswordHash(config.getActiveHashType().hash(passwordInput));
            }

            currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
            account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
            player.sendMessage(config.getServerMessages().getMessage("login-success"));
        });
    }
}

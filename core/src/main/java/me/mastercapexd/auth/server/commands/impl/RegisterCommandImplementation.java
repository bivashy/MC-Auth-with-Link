package me.mastercapexd.auth.server.commands.impl;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountRegisterEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.server.commands.parameters.RegisterPassword;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegisterCommandImplementation {
    private final AuthPlugin plugin;
    private final PluginConfig config;
    private final AccountDatabase accountStorage;

    public RegisterCommandImplementation(AuthPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.accountStorage = plugin.getAccountDatabase();
    }

    public void performRegister(ServerPlayer player, @AuthenticationAccount Account account, RegisterPassword password) {
        AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();
        plugin.getEventBus().publish(AccountRegisterEvent.class, account, false).thenAccept(registerEventPostResult -> {
            if (registerEventPostResult.getEvent().isCancelled())
                return;
            currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);

            setAccountPassword(account, password.getPassword());

            account.nextAuthenticationStep(account.isPremium() ?
                    plugin.getPremiumAuthenticationContextFactoryBucket().createContext(account) :
                    plugin.getAuthenticationContextFactoryBucket().createContext(account)
            );

            player.sendMessage(config.getServerMessages().getMessage("register-success"));
        });
    }

    public static CompletableFuture<Account> setAccountPassword(Account account, String password) {
        PluginConfig config = AuthPlugin.instance().getConfig();
        AccountDatabase accountStorage = AuthPlugin.instance().getAccountDatabase();

        if (!account.getCryptoProvider().getIdentifier().equals(config.getActiveHashType().getIdentifier()))
            account.setCryptoProvider(config.getActiveHashType());
        account.setPasswordHash(account.getCryptoProvider().hash(HashInput.of(password)));

        return accountStorage.saveOrUpdateAccount(account);
    }
}

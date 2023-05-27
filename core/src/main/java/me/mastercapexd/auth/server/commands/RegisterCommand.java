package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountRegisterEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;

import me.mastercapexd.auth.server.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.server.commands.parameters.RegisterPassword;
import me.mastercapexd.auth.step.impl.RegisterAuthenticationStep;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command({"reg", "register"})
public class RegisterCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @AuthenticationStepCommand(stepName = RegisterAuthenticationStep.STEP_NAME)
    @DefaultFor({"reg", "register"})
    public void register(ServerPlayer player, @AuthenticationAccount Account account, RegisterPassword password) {
        AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();
        plugin.getEventBus().publish(AccountRegisterEvent.class, account, false).thenAccept(registerEventPostResult -> {
            if (registerEventPostResult.getEvent().isCancelled())
                return;
            currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);

            if (!account.getCryptoProvider().getIdentifier().equals(config.getActiveHashType().getIdentifier()))
                account.setCryptoProvider(config.getActiveHashType());
            account.setPasswordHash(account.getCryptoProvider().hash(HashInput.of(password.getPassword())));

            accountStorage.saveOrUpdateAccount(account);

            account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));

            player.sendMessage(config.getServerMessages().getMessage("register-success"));
        });
    }
}
package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountTryLoginEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;

import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.step.impl.LoginAuthenticationStep;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command({"l", "login"})
public class LoginCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @AuthenticationStepCommand(stepName = LoginAuthenticationStep.STEP_NAME)
    @DefaultFor({"l", "login"})
    public void login(ServerPlayer player, Account account, String password) {
        String id = account.getPlayerId();
        AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();

        boolean isWrongPassword = !account.getHashType().matches(password, account.getPasswordHash());
        plugin.getEventBus().publish(AccountTryLoginEvent.class, account, isWrongPassword, !isWrongPassword).thenAccept(tryLoginEventPostResult -> {
            if (tryLoginEventPostResult.getEvent().isCancelled())
                return;

            if (account.getHashType() != config.getActiveHashType()) {
                account.setHashType(config.getActiveHashType());
                account.setPasswordHash(config.getActiveHashType().hash(password));
            }

            currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);
            account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
            player.sendMessage(config.getServerMessages().getMessage("login-success"));
        });
    }
}
package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.server.commands.impl.LoginCommandImplementation;
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
        LoginCommandImplementation impl = new LoginCommandImplementation(plugin, config);
        impl.performLogin(player, account, password);
    }
}
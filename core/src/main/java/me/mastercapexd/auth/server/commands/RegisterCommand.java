package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.server.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.server.commands.impl.RegisterCommandImplementation;
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
        RegisterCommandImplementation impl = new RegisterCommandImplementation(plugin, config, accountStorage);
        impl.performRegister(player, account, password);
    }
}
package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.server.commands.annotations.Permission;
import me.mastercapexd.auth.server.commands.parameters.ArgumentServerPlayer;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import me.mastercapexd.auth.step.context.BaseAuthenticationStepContext;
import me.mastercapexd.auth.step.impl.EnterServerAuthenticationStep;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;

@Command({"auth", "authadmin", "adminauth"})
@Permission("auth.admin")
public class AuthCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    @Default
    public void accountInfos(ServerCommandActor commandActor) {
        accountDatabase.getAllAccounts().thenAccept(accounts -> {
            commandActor.reply(config.getServerMessages().getMessage("info-registered", MessageContext.of("%players%", Integer.toString(accounts.size()))));
            commandActor.reply(config.getServerMessages()
                    .getMessage("info-auth",
                            MessageContext.of("%players%", Integer.toString(plugin.getAuthenticatingAccountBucket().getAccountIdEntries().size()))));
            commandActor.reply(config.getServerMessages().getMessage("info-version", MessageContext.of("%version%", plugin.getVersion())));
        });
    }

    @Subcommand({"force", "forcejoin", "fjoin"})
    public void forceEnter(ServerCommandActor commandActor, ArgumentServerPlayer proxyPlayer) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountDatabase.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                commandActor.reply(config.getServerMessages().getMessage("account-not-found"));
                return;
            }
            AuthenticationStepContext context = new BaseAuthenticationStepContext(account);
            EnterServerAuthenticationStep enterServerAuthenticationStep = new EnterServerAuthenticationStep(context);
            enterServerAuthenticationStep.enterServer();
            commandActor.reply(config.getServerMessages().getMessage("force-connect-success"));
        });
    }

    @Subcommand({"changepassword", "changepass"})
    public void changePassword(ServerCommandActor actor, ArgumentServerPlayer proxyPlayer, NewPassword newPlayerPassword) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountDatabase.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                actor.reply(config.getServerMessages().getMessage("account-not-found", MessageContext.of("%account_name%", proxyPlayer.getNickname())));
                return;
            }
            account.setPasswordHash(account.getHashType().hash(newPlayerPassword.getNewPassword()));
            accountDatabase.saveOrUpdateAccount(account);
            actor.reply(config.getServerMessages().getMessage("auth-change-success"));
        });
    }

    @Subcommand({"reset", "resetaccount", "deleteaccount"})
    public void resetAccount(ServerCommandActor actor, ArgumentServerPlayer proxyPlayer) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountDatabase.deleteAccount(id);
        actor.reply(config.getServerMessages().getMessage("auth-delete-success"));
    }

    @Subcommand("reload")
    public void reload(ServerCommandActor actor) {
        plugin.getConfig().reload();
        actor.reply(config.getServerMessages().getMessage("auth-reloaded"));
    }
}
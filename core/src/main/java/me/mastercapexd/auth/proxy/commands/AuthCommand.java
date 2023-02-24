package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.Permission;
import me.mastercapexd.auth.proxy.commands.parameters.ArgumentProxyPlayer;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;

@Command({"auth", "authadmin", "adminauth"})
@Permission("auth.admin")
public class AuthCommand {
    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    public void accountInfos(ProxyCommandActor commandActor) {
        accountStorage.getAllAccounts().thenAccept(accounts -> {
            commandActor.reply(config.getProxyMessages().getMessage("info-registered", MessageContext.of("%players%", Integer.toString(accounts.size()))));
            commandActor.reply(config.getProxyMessages()
                    .getMessage("info-auth",
                            MessageContext.of("%players%", Integer.toString(plugin.getAuthenticatingAccountBucket().getAccountIdEntries().size()))));
            commandActor.reply(config.getProxyMessages().getMessage("info-version", MessageContext.of("%version%", plugin.getVersion())));
        });
    }

    @Subcommand({"force", "forcejoin", "fjoin"})
    public void forceEnter(ProxyCommandActor commandActor, ArgumentProxyPlayer proxyPlayer) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountStorage.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                commandActor.reply(config.getProxyMessages().getMessage("account-not-found"));
                return;
            }
            AuthenticationStepContext context = new DefaultAuthenticationStepContext(account);
            EnterServerAuthenticationStep enterServerAuthenticationStep = new EnterServerAuthenticationStep(context);
            enterServerAuthenticationStep.enterServer();
            commandActor.reply(config.getProxyMessages().getMessage("force-connect-success"));
        });
    }

    @Subcommand({"changepassword", "changepass"})
    public void changePassword(ProxyCommandActor actor, ArgumentProxyPlayer proxyPlayer, NewPassword newPlayerPassword) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountStorage.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                actor.reply(config.getProxyMessages().getMessage("account-not-found"));
                return;
            }
            account.setPasswordHash(account.getHashType().hash(newPlayerPassword.getNewPassword()));
            accountStorage.saveOrUpdateAccount(account);
            actor.reply(config.getProxyMessages().getMessage("auth-change-success"));
        });
    }

    @Subcommand({"reset", "resetaccount", "deleteaccount"})
    public void resetAccount(ProxyCommandActor actor, ArgumentProxyPlayer proxyPlayer) {
        if (proxyPlayer == null)
            return;
        String id = config.getActiveIdentifierType().getId(proxyPlayer);
        accountStorage.deleteAccount(id);
        actor.reply(config.getProxyMessages().getMessage("auth-delete-success"));
    }

    @Subcommand("reload")
    public void reload(ProxyCommandActor actor) {
        plugin.getConfig().reload();
        actor.reply(config.getProxyMessages().getMessage("auth-reloaded"));
    }
}
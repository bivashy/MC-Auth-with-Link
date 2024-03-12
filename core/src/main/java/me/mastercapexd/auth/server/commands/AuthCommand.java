package me.mastercapexd.auth.server.commands;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.asset.resource.Resource;
import com.bivashy.auth.api.asset.resource.impl.FolderResource;
import com.bivashy.auth.api.asset.resource.impl.FolderResourceReader;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.database.importing.ImportExecutor;
import me.mastercapexd.auth.database.importing.ImportSource;
import me.mastercapexd.auth.database.importing.source.ImportSourceType;
import me.mastercapexd.auth.server.commands.annotations.Admin;
import me.mastercapexd.auth.server.commands.annotations.Permission;
import me.mastercapexd.auth.server.commands.exception.SendComponentException;
import me.mastercapexd.auth.server.commands.parameters.ArgumentAccount;
import me.mastercapexd.auth.server.commands.parameters.ArgumentServerPlayer;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import me.mastercapexd.auth.step.context.BaseAuthenticationStepContext;
import me.mastercapexd.auth.step.impl.EnterServerAuthenticationStep;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import ru.vyarus.yaml.updater.YamlUpdater;

@Command({"authadmin", "adminauth", "auth"})
@Permission("auth.admin")
@Admin
public class AuthCommand {

    private final AtomicBoolean importing = new AtomicBoolean(false);
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private ImportExecutor importExecutor;

    @DefaultFor({"authadmin", "adminauth", "auth"})
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
    public void changePassword(ServerCommandActor actor, ArgumentAccount account, NewPassword newPlayerPassword) {
        account.future().thenAccept(foundAccount -> {
            foundAccount.setPasswordHash(foundAccount.getCryptoProvider().hash(HashInput.of(newPlayerPassword.getNewPassword())));
            accountDatabase.saveOrUpdateAccount(foundAccount);
            actor.reply(config.getServerMessages().getMessage("auth-change-success"));
        });
    }

    @Subcommand({"reset", "resetaccount", "deleteaccount"})
    public void resetAccount(ServerCommandActor actor, ArgumentAccount account) {
        account.future().thenAccept(foundAccount -> accountDatabase.deleteAccount(foundAccount.getPlayerId()));
        actor.reply(config.getServerMessages().getMessage("auth-delete-success"));
    }

    @Subcommand("reload")
    public void reload(ServerCommandActor actor) {
        plugin.getConfig().reload();
        actor.reply(config.getServerMessages().getMessage("auth-reloaded"));
    }

    @Subcommand("migrateconfig")
    public void migrateConfig(ServerCommandActor actor) throws IOException, URISyntaxException {
        FolderResource folderResource = new FolderResourceReader(plugin.getClass().getClassLoader(), "configurations").read();
        for (Resource resource : folderResource.getResources()) {
            String realConfigurationName = resource.getName().substring(folderResource.getName().length() + 1);
            File resourceConfiguration = new File(plugin.getFolder(), realConfigurationName);
            if (!resourceConfiguration.exists())
                continue;
            YamlUpdater.create(resourceConfiguration, resource.getStream()).backup(true).update();
        }
        actor.reply(config.getServerMessages().getMessage("config-migrated"));
    }

    @Subcommand("import")
    public void importFromPlugin(ServerCommandActor actor, ImportSourceType sourceType) {
        Messages<ServerComponent> messages = config.getServerMessages().getSubMessages("importing");
        if (importing.get())
            throw new SendComponentException(messages.getMessage("already-performing"));

        actor.reply(messages.getMessage("started"));
        importing.set(true);
        ImportSource importSource = sourceType.create(config.getImportingSettings());
        importExecutor.performImport(importSource).whenComplete((statistic, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                statistic.fail();
            }
            actor.reply(ServerComponent.fromPlain(statistic.toString()));
            importing.set(false);
        });
    }

}
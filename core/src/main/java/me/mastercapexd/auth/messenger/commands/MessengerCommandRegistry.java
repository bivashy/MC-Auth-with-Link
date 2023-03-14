package me.mastercapexd.auth.messenger.commands;

import java.util.List;
import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.exception.MessengerExceptionHandler;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.orphan.Orphans;

public abstract class MessengerCommandRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private final CommandHandler commandHandler;
    private final LinkType linkType;

    public MessengerCommandRegistry(CommandHandler commandHandler, LinkType linkType) {
        this.commandHandler = commandHandler;
        this.linkType = linkType;
        register();
    }

    private void register() {
        registerContexts();
        registerDependencies();
    }

    private void registerContexts() {
        commandHandler.setExceptionHandler(new MessengerExceptionHandler(linkType));

        commandHandler.registerContextValue(LinkType.class, linkType);

        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!command.hasAnnotation(GoogleUse.class))
                return;
            if (!PLUGIN.getConfig().getGoogleAuthenticatorSettings().isEnabled())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("google-disabled"));
        });

        commandHandler.registerValueResolver(NewPassword.class, context -> {
            String newRawPassword = context.pop();
            if (newRawPassword.length() < PLUGIN.getConfig().getPasswordMinLength())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("changepass-password-too-short"));

            if (newRawPassword.length() > PLUGIN.getConfig().getPasswordMaxLength())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("changepass-password-too-long"));
            return new NewPassword(newRawPassword);
        });

        commandHandler.registerValueResolver(MessengerLinkContext.class, (context) -> {
            String code = context.popForParameter();
            LinkCommandActorWrapper commandActor = context.actor().as(LinkCommandActorWrapper.class);

            List<LinkConfirmationUser> confirmationUsers = PLUGIN.getLinkConfirmationBucket()
                    .getLinkUsers(
                            linkUser -> linkUser.getLinkType().equals(linkType) && linkUser.getLinkUserInfo().getIdentificator().equals(commandActor.userId()));

            if (confirmationUsers.isEmpty())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-no-code"));

            LinkConfirmationUser confirmationUser = confirmationUsers.stream()
                    .filter(user -> user.getConfirmationInfo().getConfirmationCode().equals(code))
                    .findFirst()
                    .orElse(null);

            if (confirmationUser == null)
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-error"));

            if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutMillis())
                throw new SendMessageException(
                        linkType.getSettings().getMessages().getMessage("confirmation-timed-out", linkType.newMessageContext(confirmationUser.getAccount())));

            LinkUser linkUser = confirmationUser.getAccount()
                    .findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);

            if (!linkUser.isIdentifierDefaultOrNull())
                throw new SendMessageException(linkType.getSettings()
                        .getMessages()
                        .getMessage("confirmation-already-linked", linkType.newMessageContext(confirmationUser.getAccount())));

            return new MessengerLinkContext(code, confirmationUser);
        });

        commandHandler.registerValueResolver(Account.class, (context) -> {
            String playerName = context.popForParameter();
            LinkUserIdentificator userId = context.actor().as(LinkCommandActorWrapper.class).userId();
            Account account = PLUGIN.getAccountDatabase().getAccountFromName(playerName).get();
            if (account == null || !account.isRegistered())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("account-not-found"));

            Optional<LinkUser> linkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
            if (!linkUser.isPresent())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("not-your-account"));

            if (!linkUser.get().getLinkUserInfo().getIdentificator().equals(userId) && !linkType.getSettings().isAdministrator(userId))
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("not-your-account", linkType.newMessageContext(account)));
            return account;
        });
    }

    private void registerDependencies() {
        commandHandler.registerDependency(AccountDatabase.class, PLUGIN.getAccountDatabase());
        commandHandler.registerDependency(PluginConfig.class, PLUGIN.getConfig());
        commandHandler.registerDependency(AuthPlugin.class, PLUGIN);
        commandHandler.registerDependency(LinkType.class, linkType);
    }

    protected void registerCommands() {
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("code").getCommandPaths()).handler(new LinkCodeCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("confirmation-toggle").getCommandPaths())
                .handler(new ConfirmationToggleCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("accounts").getCommandPaths()).handler(new AccountsListCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("account-control").getCommandPaths()).handler(new AccountCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("enter-accept").getCommandPaths())
                .handler(new AccountEnterAcceptCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("enter-decline").getCommandPaths())
                .handler(new AccountEnterDeclineCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("kick").getCommandPaths()).handler(new KickCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("restore").getCommandPaths()).handler(new RestoreCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("unlink").getCommandPaths()).handler(new UnlinkCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("change-pass").getCommandPaths()).handler(new ChangePasswordCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google-remove").getCommandPaths()).handler(new GoogleUnlinkCommand()));
        commandHandler.register(Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google").getCommandPaths()).handler(new GoogleCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google-code").getCommandPaths()).handler(new GoogleCodeCommand()));
        commandHandler.register(
                Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("admin-panel").getCommandPaths()).handler(new AdminPanelCommand()));
    }
}

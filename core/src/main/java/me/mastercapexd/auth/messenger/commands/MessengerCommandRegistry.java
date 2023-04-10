package me.mastercapexd.auth.messenger.commands;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.exception.MessengerExceptionHandler;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import me.mastercapexd.auth.shared.commands.LinkCodeCommand;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.orphan.OrphanCommand;
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

        commandHandler.registerContextResolver(MessageableCommandActor.class, context -> context.actor().as(LinkCommandActorWrapper.class));

        commandHandler.registerValueResolver(PlayerIdSupplier.class, context -> PlayerIdSupplier.of(context.pop()));

        commandHandler.registerContextResolver(LinkUserIdentificator.class, context -> context.actor().as(LinkCommandActorWrapper.class).userId());

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

            Optional<LinkConfirmationUser> confirmationUserOptional = PLUGIN.getLinkConfirmationBucket()
                    .findFirst(user -> user.getConfirmationCode().equals(code) && user.getLinkType().equals(linkType));

            if (!confirmationUserOptional.isPresent())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-no-code"));

            LinkConfirmationUser confirmationUser = confirmationUserOptional.get();

            if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutTimestamp())
                throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-timed-out"));

            LinkUser linkUser = confirmationUser.getLinkTarget().findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);

            if (!linkUser.isIdentifierDefaultOrNull())
                throw new SendMessageException(linkType.getSettings()
                        .getMessages()
                        .getMessage("confirmation-already-linked", linkType.newMessageContext(confirmationUser.getLinkTarget())));

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
        commandHandler.registerDependency(LinkConfirmationBucket.class, PLUGIN.getLinkConfirmationBucket());
        commandHandler.registerDependency(AccountDatabase.class, PLUGIN.getAccountDatabase());
        commandHandler.registerDependency(PluginConfig.class, PLUGIN.getConfig());
        commandHandler.registerDependency(AuthPlugin.class, PLUGIN);
        commandHandler.registerDependency(LinkType.class, linkType);
    }

    protected void registerCommands() {
        if (confirmationTypeEnabled(LinkConfirmationType.FROM_LINK))
            registerCommand(linkPath(LinkCodeCommand.CONFIGURATION_KEY), new LinkCodeCommand());
        if (confirmationTypeEnabled(LinkConfirmationType.FROM_GAME))
            registerCommand(linkPath(MessengerLinkCommandTemplate.CONFIGURATION_KEY), createLinkCommand());

        registerCommand(linkPath(ConfirmationToggleCommand.CONFIGURATION_KEY), new ConfirmationToggleCommand());
        registerCommand(linkPath(AccountsListCommand.CONFIGURATION_KEY), new AccountsListCommand());
        registerCommand(linkPath(AccountCommand.CONFIGURATION_KEY), new AccountCommand());
        registerCommand(linkPath(AccountEnterAcceptCommand.CONFIGURATION_KEY), new AccountEnterAcceptCommand());
        registerCommand(linkPath(AccountEnterDeclineCommand.CONFIGURATION_KEY), new AccountEnterDeclineCommand());
        registerCommand(linkPath(KickCommand.CONFIGURATION_KEY), new KickCommand());
        registerCommand(linkPath(RestoreCommand.CONFIGURATION_KEY), new RestoreCommand());
        registerCommand(linkPath(UnlinkCommand.CONFIGURATION_KEY), new UnlinkCommand());
        registerCommand(linkPath(ChangePasswordCommand.CONFIGURATION_KEY), new ChangePasswordCommand());
        registerCommand(linkPath(GoogleUnlinkCommand.CONFIGURATION_KEY), new GoogleUnlinkCommand());
        registerCommand(linkPath(GoogleCommand.CONFIGURATION_KEY), new GoogleCommand());
        registerCommand(linkPath(GoogleCodeCommand.CONFIGURATION_KEY), new GoogleCodeCommand());
        registerCommand(linkPath(AdminPanelCommand.CONFIGURATION_KEY), new AdminPanelCommand());
    }

    private void registerCommand(Orphans path, OrphanCommand commandInstance) {
        commandHandler.register(path.handler(commandInstance));
    }

    private boolean confirmationTypeEnabled(LinkConfirmationType confirmationType) {
        return linkType.getSettings().getLinkConfirmationTypes().contains(confirmationType);
    }

    private Orphans linkPath(String key) {
        return Orphans.path(commandPath(key));
    }

    private String[] commandPath(String key) {
        return linkType.getSettings().getCommandPaths().getCommandPath(key).getCommandPaths();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    protected abstract MessengerLinkCommandTemplate createLinkCommand();
}

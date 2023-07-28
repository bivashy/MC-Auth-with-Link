package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.link.user.confirmation.BaseLinkConfirmationUser;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.server.commands.annotations.DiscordUse;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(MessengerLinkCommandTemplate.CONFIGURATION_KEY)
public class DiscordLinkCommand extends MessengerLinkCommandTemplate implements OrphanCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    public DiscordLinkCommand(Messages<?> messages) {
        super(messages, DiscordLinkType.getInstance());
    }

    @DiscordUse
    @DefaultFor("~")
    public void discordLink(MessageableCommandActor commandActor, PlayerIdSupplier idSupplier, @Optional LinkUserIdentificator linkUserIdentificator) {
        String accountId = idSupplier.getPlayerId();

        accountDatabase.getAccountFromName(accountId).thenAccept(account -> {
            if (isInvalidAccount(account, commandActor, DiscordLinkType.LINK_USER_FILTER))
                return;
            String code = generateCode(() -> config.getDiscordSettings().getConfirmationSettings().generateCode());

            LinkConfirmationType linkConfirmationType = getLinkConfirmationType(commandActor);
            long timeoutTimestamp =
                    System.currentTimeMillis() + config.getDiscordSettings().getConfirmationSettings().getRemoveDelay().getMillis();
            sendLinkConfirmation(commandActor, linkConfirmationType.bindLinkConfirmationUser(
                    new BaseLinkConfirmationUser(linkConfirmationType, timeoutTimestamp, DiscordLinkType.getInstance(), account, code),
                    linkUserIdentificator));
        });
    }
}

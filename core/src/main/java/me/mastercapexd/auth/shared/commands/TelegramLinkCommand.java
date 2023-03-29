package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.confirmation.BaseLinkConfirmationUser;
import me.mastercapexd.auth.server.commands.annotations.TelegramUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

public class TelegramLinkCommand extends MessengerLinkCommandTemplate implements OrphanCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    public TelegramLinkCommand(LinkConfirmationType linkConfirmationType, Messages<?> messages) {
        super(linkConfirmationType, messages, TelegramLinkType.getInstance());
    }

    @Default
    @TelegramUse
    public void telegramLink(MessageableCommandActor commandActor, PlayerIdSupplier idSupplier, @Optional LinkUserIdentificator linkUserIdentificator) {
        String accountId = idSupplier.getPlayerId();

        accountDatabase.getAccountFromName(accountId).thenAccept(account -> {
            if (isInvalidAccount(account, commandActor, TelegramLinkType.LINK_USER_FILTER))
                return;
            String code = generateCode(() -> config.getTelegramSettings().getConfirmationSettings().generateCode());

            long timeoutTimestamp =
                    System.currentTimeMillis() + TelegramLinkType.getInstance().getSettings().getConfirmationSettings().getRemoveDelay().getMillis();
            sendLinkConfirmation(commandActor, getLinkConfirmationType().bindLinkConfirmationUser(
                    new BaseLinkConfirmationUser(getLinkConfirmationType(), timeoutTimestamp, TelegramLinkType.getInstance(), account, code),
                    linkUserIdentificator));
        });
    }
}

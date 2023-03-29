package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.telegram.TelegramConfirmationUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.confirmation.BaseConfirmationInfo;
import me.mastercapexd.auth.server.commands.annotations.TelegramUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class TelegramLinkCommand extends MessengerLinkCommandTemplate implements OrphanCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;
    private Messages<?> messages;

    public TelegramLinkCommand(LinkConfirmationType linkConfirmationType, Messages<?> messages) {
        super(linkConfirmationType, messages, TelegramLinkType.getInstance());
        this.messages = messages;
    }

    @Default
    @TelegramUse
    public void telegramLink(MessageableCommandActor commandActor, PlayerIdSupplier idSupplier, UserNumberIdentificator identificator) {
        if (identificator == null) {
            commandActor.replyWithMessage(messages.getMessage("usage"));
            return;
        }

        String accountId = idSupplier.getPlayerId();

        accountDatabase.getAccount(accountId).thenAccept(account -> {
            if (isInvalidAccount(account, commandActor, TelegramLinkType.LINK_USER_FILTER))
                return;
            accountDatabase.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                if (isInvalidLinkAccounts(accounts, commandActor))
                    return;
                String code = config.getTelegramSettings().getConfirmationSettings().generateCode();

                sendLinkConfirmation(identificator, commandActor,
                        new TelegramConfirmationUser(account, new BaseConfirmationInfo(identificator, code, getLinkConfirmationType())), accountId);
            });
        });
    }
}

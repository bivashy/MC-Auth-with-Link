package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.config.message.server.BaseServerMessages;
import me.mastercapexd.auth.link.telegram.TelegramConfirmationUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.confirmation.BaseConfirmationInfo;
import me.mastercapexd.auth.server.commands.annotations.TelegramUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

public class TelegramLinkCommand extends MessengerLinkCommandTemplate implements OrphanCommand {
    private static final String TELEGRAM_MESSAGES_KEY = "telegram";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private ServerMessages messages;

    public TelegramLinkCommand() {
        super(TELEGRAM_MESSAGES_KEY, TelegramLinkType.getInstance());
    }

    @Default
    @TelegramUse
    public void telegramLink(ServerPlayer player, @Optional Long telegramIdentificator) {
        if (telegramIdentificator == null) {
            player.sendMessage(messages.getSubMessages(TELEGRAM_MESSAGES_KEY).getMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        accountDatabase.getAccount(accountId).thenAccept(account -> {
            if (isInvalidAccount(account, player, TelegramLinkType.LINK_USER_FILTER))
                return;
            LinkUserIdentificator identificator = new UserNumberIdentificator(telegramIdentificator);
            accountDatabase.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                if (isInvalidLinkAccounts(accounts, player))
                    return;
                String code = config.getTelegramSettings().getConfirmationSettings().generateCode();

                sendLinkConfirmation(identificator, player,
                        new TelegramConfirmationUser(account, new BaseConfirmationInfo(identificator, code, LinkConfirmationType.FROM_GAME)), accountId);
            });
        });
    }
}

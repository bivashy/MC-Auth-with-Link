package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.config.message.server.BaseServerMessages;
import me.mastercapexd.auth.link.telegram.TelegramConfirmationUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.confirmation.BaseConfirmationInfo;
import me.mastercapexd.auth.server.commands.annotations.TelegramUse;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({"addtelegram", "addtg", "telegramadd", "tgadd", "telegramlink", "tglink", "linktelegram", "linktg"})
public class TelegramLinkCommand {
    private static final String TELEGRAM_SUBMESSAGES_KEY = "telegram";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;
    @Dependency
    private BaseServerMessages messages;

    @Default
    @TelegramUse
    public void telegramLink(ServerPlayer player, @Optional Long telegramIdentificator) {
        if (telegramIdentificator == null) {
            player.sendMessage(messages.getSubMessages(TELEGRAM_SUBMESSAGES_KEY).getMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        accountStorage.getAccount(accountId).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                return;
            }
            LinkUser linkUser = account.findFirstLinkUserOrNew(TelegramLinkType.LINK_USER_FILTER, TelegramLinkType.getInstance());
            if (!linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(messages.getSubMessages(TELEGRAM_SUBMESSAGES_KEY).getMessage("already-linked"));
                return;
            }
            accountStorage.getAccountsFromLinkIdentificator(new UserNumberIdentificator(telegramIdentificator)).thenAccept(accounts -> {
                if (config.getTelegramSettings().getMaxLinkCount() != 0 && accounts.size() >= config.getTelegramSettings().getMaxLinkCount()) {
                    player.sendMessage(messages.getSubMessages(TELEGRAM_SUBMESSAGES_KEY).getMessage("link-limit-reached"));
                    return;
                }
                String code = config.getTelegramSettings().getConfirmationSettings().generateCode();
                UserNumberIdentificator userIdentificator = new UserNumberIdentificator(telegramIdentificator);
                plugin.getLinkConfirmationBucket()
                        .removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                                linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(userIdentificator));
                plugin.getLinkConfirmationBucket().addLinkUser(new TelegramConfirmationUser(account, new BaseConfirmationInfo(userIdentificator, code)));
                player.sendMessage(messages.getSubMessages(TELEGRAM_SUBMESSAGES_KEY).getMessage("confirmation-sent", MessageContext.of("%code%", code)));
            });
        });
    }

    @TelegramUse
    @Command({"link tg", "tg link", "add tg", "tg add", "link telegram", "telegram link", "add telegram", "telegram add"})
    public void link(ServerPlayer player, @Optional Long telegramIdentificator) {
        telegramLink(player, telegramIdentificator);
    }
}

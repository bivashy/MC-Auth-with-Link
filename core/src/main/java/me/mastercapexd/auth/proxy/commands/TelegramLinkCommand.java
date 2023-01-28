package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.link.telegram.TelegramConfirmationUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.confirmation.info.DefaultConfirmationInfo;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.TelegramUse;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({"addtelegram", "addtg", "telegramadd", "tgadd", "telegramlink", "tglink", "linktelegram", "linktg"})
public class TelegramLinkCommand {
    private static final Messages<ProxyComponent> TELEGRAM_MESSAGES = ProxyPlugin.instance().getConfig().getProxyMessages().getSubMessages("telegram");
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @TelegramUse
    public void telegramLink(ProxyPlayer player, @Optional Long telegramIdentificator) {
        if (telegramIdentificator == null) {
            player.sendMessage(TELEGRAM_MESSAGES.getMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        accountStorage.getAccount(accountId).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getProxyMessages().getMessage("account-not-found"));
                return;
            }
            LinkUser linkUser = account.findFirstLinkUserOrNew(TelegramLinkType.LINK_USER_FILTER, TelegramLinkType.getInstance());
            if (!linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(TELEGRAM_MESSAGES.getMessage("already-linked"));
                return;
            }
            accountStorage.getAccountsFromLinkIdentificator(new UserNumberIdentificator(telegramIdentificator)).thenAccept(accounts -> {
                if (config.getTelegramSettings().getMaxLinkCount() != 0 && accounts.size() >= config.getTelegramSettings().getMaxLinkCount()) {
                    player.sendMessage(TELEGRAM_MESSAGES.getMessage("link-limit-reached"));
                    return;
                }
                String code = config.getTelegramSettings().getConfirmationSettings().generateCode();
                UserNumberIdentificator userIdentificator = new UserNumberIdentificator(telegramIdentificator);

                Auth.getLinkConfirmationAuth()
                        .removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                                linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(userIdentificator));
                Auth.getLinkConfirmationAuth().addLinkUser(new TelegramConfirmationUser(account, new DefaultConfirmationInfo(userIdentificator, code)));
                player.sendMessage(TELEGRAM_MESSAGES.getMessage("confirmation-sent", MessageContext.of("%code%", code)));
            });
        });
    }

    @TelegramUse
    @Command({"link tg", "tg link", "add tg", "tg add", "link telegram", "telegram link", "add telegram", "telegram add"})
    public void link(ProxyPlayer player, @Optional Long telegramIdentificator) {
        telegramLink(player, telegramIdentificator);
    }
}

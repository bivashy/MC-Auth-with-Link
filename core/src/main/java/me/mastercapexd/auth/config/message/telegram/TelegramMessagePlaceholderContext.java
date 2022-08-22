package me.mastercapexd.auth.config.message.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;
import me.mastercapexd.auth.config.message.messenger.context.MessengerPlaceholderContext;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;

public class TelegramMessagePlaceholderContext extends MessengerPlaceholderContext {
    public TelegramMessagePlaceholderContext(Account account) {
        super(account, TelegramLinkType.getInstance(), "telegram");
        registerPlaceholderProvider(PlaceholderProvider.of(Long.toString(linkUser.getLinkUserInfo().getIdentificator().asNumber()), "%telegram_id%"));
    }
}

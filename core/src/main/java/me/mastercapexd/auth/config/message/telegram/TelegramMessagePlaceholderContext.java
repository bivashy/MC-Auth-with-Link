package me.mastercapexd.auth.config.message.telegram;

import com.bivashy.auth.api.account.Account;

import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;
import me.mastercapexd.auth.config.message.link.context.LinkPlaceholderContext;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;

public class TelegramMessagePlaceholderContext extends LinkPlaceholderContext {
    public TelegramMessagePlaceholderContext(Account account) {
        super(account, TelegramLinkType.getInstance(), "telegram");
        getLinkUser().ifPresent(linkUser -> {
            if (linkUser.isIdentifierDefaultOrNull() || !linkUser.getLinkUserInfo().getIdentificator().isNumber())
                return;
            registerPlaceholderProvider(PlaceholderProvider.of(Long.toString(linkUser.getLinkUserInfo().getIdentificator().asNumber()), "%telegram_id%"));
        });
    }
}

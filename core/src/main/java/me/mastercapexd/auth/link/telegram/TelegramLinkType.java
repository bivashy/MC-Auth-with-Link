package me.mastercapexd.auth.link.telegram;

import java.util.function.Predicate;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.link.LinkSettings;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.messenger.telegram.MessengerTelegram;

import me.mastercapexd.auth.config.message.link.context.LinkPlaceholderContext;
import me.mastercapexd.auth.config.message.telegram.TelegramMessagePlaceholderContext;

public class TelegramLinkType implements LinkType, MessengerTelegram {
    private static final TelegramLinkType INSTANCE = new TelegramLinkType();
    public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserNumberIdentificator(Long.valueOf(-1));

    private TelegramLinkType() {
    }

    public static TelegramLinkType getInstance() {
        return INSTANCE;
    }

    @Override
    public Messages<ServerComponent> getServerMessages() {
        return PLUGIN.getConfig().getServerMessages().getSubMessages("telegram");
    }

    @Override
    public Messages<String> getLinkMessages() {
        return PLUGIN.getConfig().getTelegramSettings().getMessages();
    }

    @Override
    public LinkSettings getSettings() {
        return PLUGIN.getConfig().getTelegramSettings();
    }

    @Override
    public LinkUserIdentificator getDefaultIdentificator() {
        return DEFAULT_IDENTIFICATOR;
    }

    @Override
    public LinkPlaceholderContext newMessageContext(Account account) {
        return new TelegramMessagePlaceholderContext(account);
    }
}

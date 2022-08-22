package me.mastercapexd.auth.link.telegram;

import java.util.function.Predicate;

import com.ubivashka.messenger.telegram.MessengerTelegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.messenger.context.MessengerPlaceholderContext;
import me.mastercapexd.auth.config.message.telegram.TelegramMessagePlaceholderContext;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class TelegramLinkType implements LinkType, MessengerTelegram {
    private static final TelegramLinkType INSTANCE = new TelegramLinkType();
    public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserNumberIdentificator(Long.valueOf(-1));

    private TelegramLinkType() {
    }

    public static TelegramLinkType getInstance() {
        return INSTANCE;
    }

    @Override
    public Messages<ProxyComponent> getProxyMessages() {
        return PLUGIN.getConfig().getProxyMessages().getSubMessages("telegram");
    }

    @Override
    public Messages<String> getLinkMessages() {
        return PLUGIN.getConfig().getTelegramSettings().getMessages();
    }

    @Override
    public MessengerSettings getSettings() {
        return PLUGIN.getConfig().getTelegramSettings();
    }

    @Override
    public LinkUserIdentificator getDefaultIdentificator() {
        return DEFAULT_IDENTIFICATOR;
    }

    @Override
    public MessengerPlaceholderContext newMessageContext(Account account) {
        return new TelegramMessagePlaceholderContext(account);
    }
}

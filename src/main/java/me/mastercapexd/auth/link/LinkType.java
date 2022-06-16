package me.mastercapexd.auth.link;

import com.ubivaska.messenger.common.Messenger;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.messenger.context.MessengerPlaceholderContext;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public interface LinkType extends Messenger {
    /**
     * Returns section of messages in proxy messages
     *
     * @return messages in proxy messages
     */
    Messages<ProxyComponent> getProxyMessages();

    /**
     * Returns link messages that uses in social site, for example vk message, or
     * telegram messages
     *
     * @return
     */
    Messages<String> getLinkMessages();

    MessengerSettings getSettings();

    LinkUserIdentificator getDefaultIdentificator();

    MessengerPlaceholderContext newMessageContext(Account account);
}

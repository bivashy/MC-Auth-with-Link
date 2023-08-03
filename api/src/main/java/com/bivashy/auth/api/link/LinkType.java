package com.bivashy.auth.api.link;

import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.link.LinkSettings;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.messenger.common.Messenger;

public interface LinkType extends Messenger {
    /**
     * Returns section of server-side messages
     *
     * @return server-side messages
     */
    Messages<ServerComponent> getServerMessages();

    /**
     * Returns link messages that uses in social site, for example vk message, or
     * telegram messages
     *
     * @return messages in vk-config.yml, telegram-config.yml etc.
     */
    Messages<String> getLinkMessages();

    LinkSettings getSettings();

    default Optional<LinkSettings> findSettings() {
        try {
            return Optional.ofNullable(getSettings());
        } catch(UnsupportedOperationException ignored) {
            return Optional.empty();
        }
    }

    LinkUserIdentificator getDefaultIdentificator();

    MessageContext newMessageContext(Account account);
}

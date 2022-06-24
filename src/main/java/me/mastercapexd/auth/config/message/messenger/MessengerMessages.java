package me.mastercapexd.auth.config.message.messenger;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;

public class MessengerMessages extends AbstractMessages<String> {
    private final String delimiter;

    public MessengerMessages(ConfigurationSectionHolder configurationSection, String delimiter) {
        super(configurationSection, delimiter);
        this.delimiter = delimiter;
    }

    @Override
    public String getMessageNullable(String key) {
        return getStringMessage(key);
    }

    @Override
    public String getMessage(String key, MessageContext context) {
        return context.apply(getMessageNullable(key));
    }

    @Override
    protected Messages<String> createMessages(ConfigurationSectionHolder configurationSection) {
        return new MessengerMessages(configurationSection, delimiter);
    }

    @Override
    public String fromText(String text) {
        return text;
    }
}

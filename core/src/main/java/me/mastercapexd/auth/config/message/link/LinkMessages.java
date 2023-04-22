package me.mastercapexd.auth.config.message.link;

import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.MessagesTemplate;

public class LinkMessages extends MessagesTemplate<String> {
    private final String delimiter;

    public LinkMessages(ConfigurationSectionHolder configurationSection, String delimiter) {
        super(delimiter);
        initializeMessages(configurationSection);
        this.delimiter = delimiter;
    }

    @Override
    public String getMessage(String key, MessageContext context) {
        return context.apply(getMessage(key));
    }

    @Override
    protected Messages<String> createMessages(ConfigurationSectionHolder configurationSection) {
        return new LinkMessages(configurationSection, delimiter);
    }

    @Override
    public String fromText(String text) {
        return text;
    }
}

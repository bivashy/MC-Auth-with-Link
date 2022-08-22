package me.mastercapexd.auth.config.message;

import java.util.HashMap;
import java.util.Map;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.context.MessageContext;

public abstract class AbstractMessages<T> implements Messages<T>, ConfigurationHolder {
    protected static final String DEFAULT_DELIMITER = "\n";
    private String nullMessage = "Message with key %s not found!";

    protected Map<String, String> messages = new HashMap<>();
    protected Map<String, Messages<T>> subMessages = new HashMap<>();

    public AbstractMessages(ConfigurationSectionHolder configurationSectionHolder, CharSequence delimeter, String nullMessage) {
        this(configurationSectionHolder, delimeter);
        this.nullMessage = nullMessage;
    }

    public AbstractMessages(ConfigurationSectionHolder configurationSection, CharSequence delimiter) {
        for (String key : configurationSection.keys()) {
            if (configurationSection.isList(key)) {
                addMessage(key, String.join(delimiter, configurationSection.getList(key).toArray(new String[0])));
                continue;
            }

            if (configurationSection.isSection(key)) {
                subMessages.put(key, createMessages(configurationSection.section(key)));
                continue;
            }

            addMessage(key, configurationSection.getString(key));
        }
    }

    public AbstractMessages(ConfigurationSectionHolder configurationSection) {
        this(configurationSection, DEFAULT_DELIMITER);
    }

    @Override
    public Messages<T> getSubMessages(String key) {
        return subMessages.getOrDefault(key, null);
    }

    @Override
    public String getStringMessage(String key) {
        return getStringMessage(key, String.format(nullMessage, key));
    }

    @Override
    public String getStringMessage(String key, String defaultValue) {
        return messages.getOrDefault(key, defaultValue);
    }

    @Override
    public T getMessage(String key, MessageContext context) {
        return fromText(context.apply(getStringMessage(key)));
    }

    @Override
    public T getMessage(String key) {
        String message = getStringMessage(key, Messages.NULL_STRING);
        return fromText(message);
    }

    public void addMessage(String path, String message) {
        String formattedMessage = formatString(message);
        messages.put(path, formattedMessage);
    }

    protected abstract Messages<T> createMessages(ConfigurationSectionHolder configurationSection);
}

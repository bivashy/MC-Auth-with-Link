package me.mastercapexd.auth.config.message;

import java.util.HashMap;
import java.util.Map;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.context.MessageContext;

public abstract class AbstractMessages<T> implements Messages<T>, ConfigurationHolder {
    public static final String DEFAULT_DELIMITER = "\n";
    private final CharSequence delimiter;
    private Map<String, String> messages = new HashMap<>();
    private Map<String, Messages<T>> subMessages = new HashMap<>();
    private String nullMessage = "Message with key %s not found!";

    public AbstractMessages(CharSequence delimeter, String nullMessage) {
        this(delimeter);
        this.nullMessage = nullMessage;
    }

    public AbstractMessages(CharSequence delimiter) {
        this.delimiter = delimiter;
    }

    protected void initializeMessages(ConfigurationSectionHolder sectionHolder){
        for (String key : sectionHolder.keys()) {
            if (sectionHolder.isList(key)) {
                addMessage(key, String.join(delimiter, sectionHolder.getList(key).toArray(new String[0])));
                continue;
            }

            if (sectionHolder.isSection(key)) {
                subMessages.put(key, createMessages(sectionHolder.section(key)));
                continue;
            }

            addMessage(key, sectionHolder.getString(key));
        }
    }

    @Override
    public Messages<T> getSubMessages(String key) {
        return subMessages.getOrDefault(key, null);
    }

    @Override
    public String getStringMessage(String key) {
        if (nullMessage == null)
            return getStringMessage(key, (String) null);
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

package me.mastercapexd.auth.config.messages;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;

public abstract class AbstractMessages<T> implements Messages<T>, ConfigurationHolder {
	private static final String MESSAGE_NOT_FOUND_ERROR = "Message with key %s not found!";
	protected static final String DEFAULT_DELIMITER = "\n";

	protected Map<String, String> messages = Maps.newHashMap();
	protected HashMap<String, AbstractMessages<T>> subMessages = new HashMap<>();

	public AbstractMessages(ConfigurationSectionHolder configurationSection, CharSequence delimiter) {
		for (String key : configurationSection.getKeys()) {
			if (configurationSection.isCollection(key)) {
				addMessage(key, String.join(delimiter, configurationSection.getList(key).toArray(new String[0])));
				continue;
			}

			if (configurationSection.isConfigurationSection(key)) {
				subMessages.put(key, createMessages(configurationSection.getSection(key)));
				continue;
			}

			if (configurationSection.isString(key)) {
				addMessage(key, configurationSection.getString(key));
				continue;
			}

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
		return getStringMessage(key, String.format(MESSAGE_NOT_FOUND_ERROR, key));
	}

	@Override
	public String getStringMessage(String key,String defaultValue) {
		return messages.getOrDefault(key, defaultValue);
	}

	
	@Override
	public T getMessage(String key, MessageContext context) {
		return fromText(context.formatString(getStringMessage(key)));
	}

	public void addMessage(String path, String message) {
		String formattedMessage = formatString(message);
		messages.put(path, formattedMessage);
	}
	
	protected abstract AbstractMessages<T> createMessages(ConfigurationSectionHolder configurationSection);
}

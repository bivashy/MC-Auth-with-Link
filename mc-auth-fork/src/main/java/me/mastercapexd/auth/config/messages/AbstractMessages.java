package me.mastercapexd.auth.config.messages;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public abstract class AbstractMessages<T, C extends MessageContext> extends BungeeConfigurationHolder implements Messages<T, C> {
	private static final String MESSAGE_NOT_FOUND_ERROR = "Message with key %s not found!";
	protected static final String DEFAULT_DELIMITER = "\n";

	protected Map<String, String> messages = Maps.newHashMap();
	protected HashMap<String, AbstractMessages<T, C>> subMessages = new HashMap<>();

	public AbstractMessages(Configuration configurationSection, CharSequence delimiter) {
		for (String key : configurationSection.getKeys()) {
			if (configurationSection.getStringList(key) != null && !configurationSection.getStringList(key).isEmpty()) {
				addMessage(key, String.join(delimiter, configurationSection.getStringList(key).toArray(new String[0])));
				continue;
			}	
				
			if (configurationSection.get(key) instanceof Configuration) {
				subMessages.put(key, createMessages(configurationSection.getSection(key)));
				continue;
			}	
			
			if (configurationSection.getString(key) != null) {
				addMessage(key, configurationSection.getString(key));
				continue;
			}
			
		}
	}

	public AbstractMessages(Configuration configurationSection) {
		this(configurationSection, DEFAULT_DELIMITER);
	}

	@Override
	public Messages<T, C> getSubMessages(String key) {
		return subMessages.getOrDefault(key, null);
	}

	@Override
	public String getStringMessage(String key) {
		return messages.getOrDefault(key, String.format(MESSAGE_NOT_FOUND_ERROR, key));
	}

	public void addMessage(String path, String message) {
		String formattedMessage = formatString(message);
		messages.put(path, formattedMessage);
	}

	protected abstract AbstractMessages<T, C> createMessages(Configuration configurationSection);
}

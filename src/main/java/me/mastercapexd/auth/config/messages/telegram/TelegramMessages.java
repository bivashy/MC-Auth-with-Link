package me.mastercapexd.auth.config.messages.telegram;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.messages.messenger.MessengerMessages;

public class TelegramMessages extends MessengerMessages{
	public TelegramMessages(ConfigurationSectionHolder configurationSection) {
		super(configurationSection, "\n");
	}
}

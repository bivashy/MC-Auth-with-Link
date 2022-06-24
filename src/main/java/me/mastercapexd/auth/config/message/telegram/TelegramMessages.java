package me.mastercapexd.auth.config.message.telegram;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.messenger.MessengerMessages;

public class TelegramMessages extends MessengerMessages {
    public TelegramMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, "\n");
    }
}

package me.mastercapexd.auth.config.message.telegram;

import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.link.LinkMessages;

public class TelegramMessages extends LinkMessages {
    public TelegramMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, "\n");
    }
}

package me.mastercapexd.auth.config.message.discord;

import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.link.LinkMessages;

public class DiscordMessages extends LinkMessages {
    public DiscordMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, "\n");
    }
}

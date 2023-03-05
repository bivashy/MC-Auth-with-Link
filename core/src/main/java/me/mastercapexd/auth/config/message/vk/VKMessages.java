package me.mastercapexd.auth.config.message.vk;

import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.link.LinkMessages;

public class VKMessages extends LinkMessages {
    public VKMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, "<br>");
    }
}

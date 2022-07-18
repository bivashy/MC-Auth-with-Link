package me.mastercapexd.auth.config.message.vk;

import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.messenger.MessengerMessages;

public class VKMessages extends MessengerMessages {
    public VKMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, "<br>");
    }
}

package me.mastercapexd.auth.config.telegram;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;
import com.ubivashka.messenger.telegram.message.keyboard.TelegramKeyboard;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.config.messenger.MessengerKeyboards;

public class TelegramKeyboards implements ConfigurationHolder, MessengerKeyboards {
    private final Map<String, String> jsonKeyboards;

    public TelegramKeyboards(ConfigurationSectionHolder sectionHolder) {
        jsonKeyboards = sectionHolder.keys().stream().collect(Collectors.toMap(Function.identity(), sectionHolder::getString));
    }

    @Override
    public Map<String, String> getRawJsonKeyboards() {
        return Collections.unmodifiableMap(jsonKeyboards);
    }

    @Override
    public Keyboard createKeyboardModel(String rawJson) {
        return new TelegramKeyboard(GSON.fromJson(rawJson, InlineKeyboardMarkup.class));
    }
}

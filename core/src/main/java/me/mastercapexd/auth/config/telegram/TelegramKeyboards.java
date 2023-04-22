package me.mastercapexd.auth.config.telegram;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bivashy.auth.api.config.link.LinkKeyboards;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;
import com.bivashy.messenger.telegram.message.keyboard.TelegramKeyboard;
import com.bivashy.messenger.common.keyboard.Keyboard;

public class TelegramKeyboards implements ConfigurationHolder, LinkKeyboards {
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

package me.mastercapexd.auth.config.discord;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bivashy.auth.api.config.link.LinkKeyboards;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;
import com.bivashy.messenger.common.keyboard.Keyboard;
import com.bivashy.messenger.discord.message.keyboard.DiscordKeyboard;
import com.google.gson.reflect.TypeToken;

import net.dv8tion.jda.api.interactions.components.ActionRow;

public class DiscordKeyboards implements ConfigurationHolder, LinkKeyboards {
    private final Map<String, String> jsonKeyboards;

    public DiscordKeyboards(ConfigurationSectionHolder sectionHolder) {
        jsonKeyboards = sectionHolder.keys().stream().collect(Collectors.toMap(Function.identity(), sectionHolder::getString));
    }

    @Override
    public Map<String, String> getRawJsonKeyboards() {
        return Collections.unmodifiableMap(jsonKeyboards);
    }

    @Override
    public Keyboard createKeyboardModel(String rawJson) {
        return new DiscordKeyboard(GSON.fromJson(rawJson, new TypeToken<List<ActionRow>>() {
        }.getType()));
    }
}

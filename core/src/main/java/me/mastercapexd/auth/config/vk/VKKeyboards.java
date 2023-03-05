package me.mastercapexd.auth.config.vk;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bivashy.auth.api.config.link.LinkKeyboards;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;
import com.ubivashka.messenger.vk.message.keyboard.VkKeyboard;
import com.ubivaska.messenger.common.keyboard.Keyboard;

public class VKKeyboards implements ConfigurationHolder, LinkKeyboards {
    private final Map<String, String> jsonKeyboards;

    public VKKeyboards(ConfigurationSectionHolder sectionHolder) {
        jsonKeyboards = sectionHolder.keys().stream().collect(Collectors.toMap(Function.identity(), sectionHolder::getString));
    }

    @Override
    public Map<String, String> getRawJsonKeyboards() {
        return Collections.unmodifiableMap(jsonKeyboards);
    }

    @Override
    public Keyboard createKeyboardModel(String rawJson) {
        return new VkKeyboard(GSON.fromJson(rawJson, com.vk.api.sdk.objects.messages.Keyboard.class));
    }
}

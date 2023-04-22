package com.bivashy.auth.api.config.link;

import java.util.Map;
import java.util.Map.Entry;

import com.bivashy.auth.api.util.CollectionUtil.ArrayPairHashMapAdapter;
import com.bivashy.messenger.common.keyboard.Keyboard;
import com.google.gson.Gson;

public interface LinkKeyboards {
    Gson GSON = new Gson();

    default Keyboard createKeyboard(String key, String... placeholders) {
        String rawJson = getRawJsonKeyboards().get(key);
        for (Entry<String, String> entry : new ArrayPairHashMapAdapter<>(placeholders).entrySet())
            rawJson = rawJson.replaceAll(entry.getKey(), entry.getValue());
        return createKeyboardModel(rawJson);
    }

    Map<String, String> getRawJsonKeyboards();

    Keyboard createKeyboardModel(String rawJson);
}

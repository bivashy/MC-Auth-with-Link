package me.mastercapexd.auth.config.messenger;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.ubivashka.messenger.telegram.message.keyboard.TelegramKeyboard;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.adapter.ArrayPairHashMapAdapter;

public interface MessengerKeyboards {
    Gson GSON = new Gson();
    default Keyboard createKeyboard(String key, String... placeholders){
        String rawJson = getRawJsonKeyboards().get(key);
        for (Entry<String, String> entry : new ArrayPairHashMapAdapter<>(placeholders).entrySet())
            rawJson = rawJson.replaceAll(entry.getKey(), entry.getValue());
        return new TelegramKeyboard(GSON.fromJson(rawJson, InlineKeyboardMarkup.class));
    }

    Map<String,String> getRawJsonKeyboards();
}

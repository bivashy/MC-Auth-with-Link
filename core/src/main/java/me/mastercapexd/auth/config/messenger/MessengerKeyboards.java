package me.mastercapexd.auth.config.messenger;

import com.ubivaska.messenger.common.keyboard.Keyboard;

public interface MessengerKeyboards {
    Keyboard createKeyboard(String key, String... placeholders);
}

package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public interface MessengerKeyboards {
	IKeyboard createKeyboard(String key, String... placeholders);
}

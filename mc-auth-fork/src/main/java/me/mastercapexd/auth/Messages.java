package me.mastercapexd.auth;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public interface Messages {

	default BaseComponent[] getMessage(String key) {
		return TextComponent.fromLegacyText(getLegacyMessage(key));
	}
	
	String getLegacyMessage(String key);
}
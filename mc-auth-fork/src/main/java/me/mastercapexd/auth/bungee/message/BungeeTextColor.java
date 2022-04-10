package me.mastercapexd.auth.bungee.message;

import me.mastercapexd.auth.proxy.message.TextColor;
import net.md_5.bungee.api.ChatColor;

public class BungeeTextColor extends TextColor {

	@Override
	protected String colorText(char colorCharacter, String text) {
		return ChatColor.translateAlternateColorCodes(colorCharacter, text);
	}

}

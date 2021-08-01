package me.mastercapexd.auth.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TitleBar {

	public static void send(ProxiedPlayer proxiedPlayer, String titleMessage, String subtitleMessage, int fadeIn, int stay, int fadeOut) {
		Title title = ProxyServer.getInstance().createTitle();
		title.title(TextComponent.fromLegacyText(titleMessage));
		title.subTitle(TextComponent.fromLegacyText(subtitleMessage));
		title.fadeIn(fadeIn);
		title.stay(stay);
		title.fadeOut(fadeOut);
		title.send(proxiedPlayer);
	}
}
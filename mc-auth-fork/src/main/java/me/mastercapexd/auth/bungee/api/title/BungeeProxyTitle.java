package me.mastercapexd.auth.bungee.api.title;

import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeProxyTitle extends ProxyTitle {

	private final Title bungeeTitle = ProxyServer.getInstance().createTitle();

	@Override
	public ProxyTitle send(ProxyPlayer... players) {
		bungeeTitle.title(TextComponent.fromLegacyText(title));
		bungeeTitle.subTitle(TextComponent.fromLegacyText(subtitle));
		bungeeTitle.fadeIn(fadeIn);
		bungeeTitle.stay(stay);
		bungeeTitle.fadeOut(fadeOut);
		for (ProxyPlayer player : players)
			bungeeTitle.send(player.getRealPlayer());
		return this;
	}

}

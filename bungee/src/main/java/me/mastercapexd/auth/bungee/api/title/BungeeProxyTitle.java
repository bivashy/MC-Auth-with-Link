package me.mastercapexd.auth.bungee.api.title;

import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;

public class BungeeProxyTitle extends ProxyTitle {

    private final Title bungeeTitle = ProxyServer.getInstance().createTitle();

    public BungeeProxyTitle(ProxyComponent title) {
        title(title);
    }

    @Override
    public ProxyTitle send(ProxyPlayer... players) {
        bungeeTitle.title(title.as(BungeeComponent.class).components());
        bungeeTitle.subTitle(subtitle.as(BungeeComponent.class).components());
        bungeeTitle.fadeIn(fadeIn);
        bungeeTitle.stay(stay);
        bungeeTitle.fadeOut(fadeOut);
        for (ProxyPlayer player : players)
            bungeeTitle.send(player.getRealPlayer());
        return this;
    }

}

package me.mastercapexd.auth.bungee.api.title;

import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeProxyTitle extends ProxyTitle {
    private final Title bungeeTitle = ProxyServer.getInstance().createTitle();

    public BungeeProxyTitle(ProxyComponent title) {
        title(title);
    }

    @Override
    public ProxyTitle title(ProxyComponent title) {
        super.title(title);
        bungeeTitle.title(ComponentSerializer.parse(title.jsonText()));
        return this;
    }

    @Override
    public ProxyTitle subtitle(ProxyComponent subtitle) {
        super.subtitle(subtitle);
        bungeeTitle.subTitle(ComponentSerializer.parse(subtitle.jsonText()));
        return this;
    }

    @Override
    public ProxyTitle send(ProxyPlayer... players) {
        bungeeTitle.fadeIn(fadeIn);
        bungeeTitle.stay(stay);
        bungeeTitle.fadeOut(fadeOut);
        for (ProxyPlayer player : players)
            bungeeTitle.send(player.getRealPlayer());
        return this;
    }
}

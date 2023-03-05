package me.mastercapexd.auth.bungee.api.title;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.title.ServerTitle;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeServerTitle extends ServerTitle {
    private final Title bungeeTitle = ProxyServer.getInstance().createTitle();

    public BungeeServerTitle(ServerComponent title) {
        title(title);
    }

    @Override
    public ServerTitle title(ServerComponent title) {
        super.title(title);
        bungeeTitle.title(ComponentSerializer.parse(title.jsonText()));
        return this;
    }

    @Override
    public ServerTitle subtitle(ServerComponent subtitle) {
        super.subtitle(subtitle);
        bungeeTitle.subTitle(ComponentSerializer.parse(subtitle.jsonText()));
        return this;
    }

    @Override
    public ServerTitle send(ServerPlayer... players) {
        bungeeTitle.fadeIn(fadeIn);
        bungeeTitle.stay(stay);
        bungeeTitle.fadeOut(fadeOut);
        for (ServerPlayer player : players)
            bungeeTitle.send(player.getRealPlayer());
        return this;
    }
}

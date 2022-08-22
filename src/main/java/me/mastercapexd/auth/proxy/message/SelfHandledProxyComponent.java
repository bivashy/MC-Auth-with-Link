package me.mastercapexd.auth.proxy.message;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface SelfHandledProxyComponent extends ProxyComponent{
    void send(ProxyPlayer player);
}

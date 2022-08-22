package me.mastercapexd.auth.authentication.step;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface MessageableAuthenticationStep {
    void process(ProxyPlayer player);
}

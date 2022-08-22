package me.mastercapexd.auth.management;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;

/**
 * Class that manages player login. For example send player to the server if he has session. Or block if player trying to join multiple accounts with
 * same ip. Or remove
 */
public interface LoginManagement {
    /**
     * Handle player join. Start authentication/registration/session process.
     * On BungeeCord this will use PostLoginEvent.
     */
    void onLogin(ProxyPlayer player);

    /**
     * Handle player leave. Remove him from caching or modify player on database (Save player quit event for example).
     */
    void onDisconnect(ProxyPlayer player);
}

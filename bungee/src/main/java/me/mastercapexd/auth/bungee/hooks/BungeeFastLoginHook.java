package me.mastercapexd.auth.bungee.hooks;

import com.github.games647.fastlogin.bungee.FastLoginBungee;

import me.mastercapexd.auth.hooks.FastLoginHook;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeFastLoginHook {
    public BungeeFastLoginHook(ProxyPlugin plugin, FastLoginBungee fastLoginBungee) {
        fastLoginBungee.getCore().setAuthPluginHook(new FastLoginHook<ProxiedPlayer>(plugin) {});
    }
}

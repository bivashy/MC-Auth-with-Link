package me.mastercapexd.auth.bungee.hooks.nanolimbo;

import java.net.SocketAddress;
import java.util.stream.IntStream;

import com.bivashy.auth.api.hook.LimboPluginHook;

import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeNanoLimboPluginHook implements LimboPluginHook {

    private final IntStream limboPortRange;
    private NanoLimboProvider provider;

    public BungeeNanoLimboPluginHook(IntStream limboPortRange, Plugin plugin) {
        this.limboPortRange = limboPortRange;
        if (!canHook())
            return;
        this.provider = new BungeeNanoLimboProvider(plugin.getClass().getClassLoader());
    }

    @Override
    public com.bivashy.auth.api.server.proxy.ProxyServer createServer(String serverName) {
        SocketAddress address = provider.findAvailableAddress(limboPortRange).orElseThrow(
                () -> new IllegalStateException("Cannot find available port for limbo server!"));
        provider.createAndStartLimbo(address);
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, address, "", false);
        ProxyServer.getInstance().getConfig().getServers().put(serverInfo.getName(), serverInfo);
        return new BungeeServer(serverInfo);
    }

    @Override
    public boolean canHook() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("NanoLimboBungee") != null;
    }

}

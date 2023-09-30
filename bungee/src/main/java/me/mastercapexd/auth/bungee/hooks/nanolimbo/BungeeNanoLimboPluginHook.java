package me.mastercapexd.auth.bungee.hooks.nanolimbo;

import java.net.SocketAddress;
import java.util.stream.IntStream;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.hook.LimboPluginHook;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeNanoLimboPluginHook implements LimboPluginHook, Listener {

    private final int[] limboPorts;
    private NanoLimboProvider provider;

    public BungeeNanoLimboPluginHook(IntStream limboPortRange) {
        this.limboPorts = limboPortRange.toArray();
        if (!canHook())
            return;
        this.provider = new BungeeNanoLimboProvider(ProxyServer.getInstance().getPluginManager().getPlugin("NanoLimboBungee").getClass().getClassLoader());
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeAuthPluginBootstrap.getInstance(), this);
    }

    @Override
    public com.bivashy.auth.api.server.proxy.ProxyServer createServer(String serverName) {
        SocketAddress address = provider.findAvailableAddress(limboPorts).orElseThrow(
                () -> new IllegalStateException("Cannot find available port for limbo server!"));
        provider.createAndStartLimbo(address);
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, address, "", false);
        ProxyServer.getInstance().getConfig().getServers().put(serverInfo.getName(), serverInfo);
        return new BungeeServer(serverInfo);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerChoose(ServerConnectEvent event) {
        // TODO: Implement ServerConnectEvent in the InitialServerManagement
        if (event.getReason() != Reason.JOIN_PROXY)
            return;
        PluginConfig config = AuthPlugin.instance().getConfig();
        event.setTarget(config.findServerInfo(config.getAuthServers()).asProxyServer().as(BungeeServer.class).getServerInfo());
    }

    @Override
    public boolean canHook() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("NanoLimboBungee") != null;
    }

}

package me.mastercapexd.auth.velocity.hooks.nanolimbo;

import java.net.InetSocketAddress;
import java.util.stream.IntStream;

import com.bivashy.auth.api.hook.LimboPluginHook;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboProvider;
import me.mastercapexd.auth.velocity.server.VelocityProxyServer;

public class VelocityNanoLimboPluginHook implements LimboPluginHook {

    private final int[] limboPorts;
    private final ProxyServer proxyServer;
    private NanoLimboProvider provider;

    public VelocityNanoLimboPluginHook(IntStream limboPorts, ProxyServer proxyServer) {
        this.limboPorts = limboPorts.toArray();
        this.proxyServer = proxyServer;
        if (!canHook())
            return;
        provider = new VelocityNanoLimboProvider(getClass().getClassLoader(), proxyServer);
    }

    @Override
    public com.bivashy.auth.api.server.proxy.ProxyServer createServer(String serverName) {
        InetSocketAddress address = provider.findAvailableAddress(limboPorts).orElseThrow(
                () -> new IllegalStateException("Cannot find available port for limbo server!"));
        provider.createAndStartLimbo(address);
        return new VelocityProxyServer(proxyServer.registerServer(new ServerInfo(serverName, address)));
    }

    @Override
    public boolean canHook() {
        return proxyServer.getPluginManager().isLoaded("nanolimbovelocity");
    }

}

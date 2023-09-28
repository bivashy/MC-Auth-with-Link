package me.mastercapexd.auth.velocity.hooks.nanolimbo;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.IntStream;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.proxy.config.PlayerInfoForwarding;
import com.velocitypowered.proxy.config.VelocityConfiguration;

import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboPluginHook;
import me.mastercapexd.auth.velocity.server.VelocityProxyServer;
import ua.nanit.limbo.NanoLimbo;
import ua.nanit.limbo.server.LimboServer;
import ua.nanit.limbo.server.data.InfoForwarding;

public class VelocityNanoLimboPluginHook implements NanoLimboPluginHook {

    private final IntStream limboPortRange;
    private final ClassLoader classLoader;
    private final ProxyServer proxyServer;

    public VelocityNanoLimboPluginHook(IntStream limboPortRange, ProxyServer proxyServer, String portRange) {
        this.limboPortRange = limboPortRange;
        this.classLoader = NanoLimbo.class.getClassLoader();
        this.proxyServer = proxyServer;
    }

    @Override
    public com.bivashy.auth.api.server.proxy.ProxyServer createServer(String serverName) {
        InetSocketAddress address = findAvailableAddress(limboPortRange).orElseThrow(
                () -> new IllegalStateException("Cannot find available port for limbo server!"));
        LimboServer server = createLimboServer(address);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new VelocityProxyServer(proxyServer.registerServer(new ServerInfo(serverName, address)));
    }

    @Override
    public InfoForwarding createForwarding() {
        VelocityConfiguration velocityConfiguration = (VelocityConfiguration) proxyServer.getConfiguration();
        PlayerInfoForwarding forwardingMode = velocityConfiguration.getPlayerInfoForwardingMode();
        if (forwardingMode == PlayerInfoForwarding.NONE)
            return FORWARDING_FACTORY.none();
        if (forwardingMode == PlayerInfoForwarding.LEGACY)
            return FORWARDING_FACTORY.legacy();
        if (forwardingMode == PlayerInfoForwarding.MODERN)
            return FORWARDING_FACTORY.modern(velocityConfiguration.getForwardingSecret());
        if (forwardingMode == PlayerInfoForwarding.BUNGEEGUARD)
            return FORWARDING_FACTORY.bungeeGuard(Collections.singleton(new String(velocityConfiguration.getForwardingSecret(), StandardCharsets.UTF_8)));
        return FORWARDING_FACTORY.none();
    }

    @Override
    public ClassLoader classLoader() {
        return classLoader;
    }

}

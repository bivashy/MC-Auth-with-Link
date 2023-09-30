package me.mastercapexd.auth.velocity.hooks.nanolimbo;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.config.PlayerInfoForwarding;
import com.velocitypowered.proxy.config.VelocityConfiguration;

import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboProvider;
import ua.nanit.limbo.server.data.InfoForwarding;

public class VelocityNanoLimboProvider implements NanoLimboProvider {

    private final ClassLoader classLoader;
    private final ProxyServer proxyServer;

    public VelocityNanoLimboProvider(ClassLoader classLoader, ProxyServer proxyServer) {
        this.classLoader = classLoader;
        this.proxyServer = proxyServer;
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

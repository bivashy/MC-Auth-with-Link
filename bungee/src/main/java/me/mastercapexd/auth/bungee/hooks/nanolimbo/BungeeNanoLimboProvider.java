package me.mastercapexd.auth.bungee.hooks.nanolimbo;

import me.mastercapexd.auth.hooks.nanolimbo.NanoLimboProvider;
import ua.nanit.limbo.server.data.InfoForwarding;

public class BungeeNanoLimboProvider implements NanoLimboProvider {

    private final ClassLoader classLoader;

    public BungeeNanoLimboProvider(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public InfoForwarding createForwarding() {
        return FORWARDING_FACTORY.legacy();
    }

    @Override
    public ClassLoader classLoader() {
        return classLoader;
    }

}

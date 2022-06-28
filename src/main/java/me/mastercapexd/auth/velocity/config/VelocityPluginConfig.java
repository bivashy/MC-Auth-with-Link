package me.mastercapexd.auth.velocity.config;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.SpongeConfiguratePluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VelocityPluginConfig extends SpongeConfiguratePluginConfig {
    public VelocityPluginConfig(ProxyPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(ProxyPlugin plugin) {
        return null;
    }
}

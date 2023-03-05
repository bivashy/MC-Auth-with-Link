package me.mastercapexd.auth.velocity.config;

import com.bivashy.auth.api.AuthPlugin;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.SpongeConfiguratePluginConfig;

public class VelocityPluginConfig extends SpongeConfiguratePluginConfig {
    public VelocityPluginConfig(AuthPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(AuthPlugin plugin) {
        return null;
    }
}

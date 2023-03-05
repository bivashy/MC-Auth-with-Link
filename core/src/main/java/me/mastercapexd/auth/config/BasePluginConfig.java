package me.mastercapexd.auth.config;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.asset.resource.impl.FolderResourceReader;
import com.ubivashka.configuration.configurate.holder.ConfigurationNodeHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

public class BasePluginConfig extends SpongeConfiguratePluginConfig {
    public BasePluginConfig(AuthPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(AuthPlugin proxyPlugin) {
        return new ConfigurationNodeHolder(loadConfiguration(proxyPlugin.getFolder(),
                new FolderResourceReader(proxyPlugin.getClass().getClassLoader(), "configurations").read()));
    }
}

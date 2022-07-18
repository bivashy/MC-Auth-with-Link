package me.mastercapexd.auth.config;

import com.ubivashka.configuration.configurate.holder.ConfigurationNodeHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.asset.resource.folder.FolderResourceReader;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultPluginConfig extends SpongeConfiguratePluginConfig{
    public DefaultPluginConfig(ProxyPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(ProxyPlugin proxyPlugin) {
        return new ConfigurationNodeHolder(loadConfiguration(proxyPlugin.getFolder(),
                new FolderResourceReader(proxyPlugin.getClass().getClassLoader(), "configurations").read()));
    }
}

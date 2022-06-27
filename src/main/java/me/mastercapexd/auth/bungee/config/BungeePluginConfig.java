package me.mastercapexd.auth.bungee.config;

import com.ubivashka.configuration.configurate.holder.ConfigurationNodeHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.asset.resource.folder.FolderResourceReader;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.SpongeConfiguratePluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePluginConfig extends SpongeConfiguratePluginConfig {

    public BungeePluginConfig(ProxyPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(ProxyPlugin proxyPlugin) {
        Plugin plugin = proxyPlugin.as(AuthPlugin.class);
        return new ConfigurationNodeHolder(loadConfiguration(plugin.getDataFolder(),
                new FolderResourceReader(plugin.getClass().getClassLoader(), "configurations").read()));
    }
}
package me.mastercapexd.auth.bungee.config;

import com.ubivashka.configuration.configurate.holder.ConfigurationHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.asset.resource.folder.FolderResourceReader;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.SpongeConfiguratePluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePluginConfig extends SpongeConfiguratePluginConfig {

    public BungeePluginConfig(ProxyPlugin proxyPlugin) {
        super(proxyPlugin);
        proxyPlugin.getConfigurationProcessor().registerFieldResolver(ProxyComponent.class,
                context -> {
                    if (context.configuration().isConfigurationSection(context.path())) {
                        ConfigurationSectionHolder sectionHolder = context.configuration().getSection(context.path());
                        String componentType = sectionHolder.getString("type");
                        switch (componentType) {
                            case "json":
                                return proxyPlugin.getCore().componentJson(sectionHolder.getString("value"));
                            case "legacy":
                                return proxyPlugin.getCore().componentLegacy(sectionHolder.getString("value"));
                            case "plain":
                                return proxyPlugin.getCore().componentPlain(sectionHolder.getString("value"));
                            default:
                                throw new IllegalArgumentException(
                                        "Illegal component type in " + context.path() + ":" + componentType + ", available: json,legacy,plain");
                        }
                    }
                    return proxyPlugin.getCore()
                            .componentLegacy(context.configuration().getString(context.path()));
                });
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(ProxyPlugin proxyPlugin) {
        Plugin plugin = proxyPlugin.as(AuthPlugin.class);
        return new ConfigurationHolder(loadConfiguration(plugin.getDataFolder(), new FolderResourceReader(plugin.getClass().getClassLoader(), "configurations"
        ).read()));
    }
}
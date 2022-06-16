package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultCommandPaths implements ConfigurationHolder, MessengerCommandPaths {

    @ConfigField
    private ConfigurationHolderMap<DefaultCommandPath> defaultCommands = new ConfigurationHolderMap<>();

    public DefaultCommandPaths(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public DefaultCommandPath getPath(String commandKey) {
        return defaultCommands.getOrDefault(commandKey, null);
    }

    @Override
    public MessengerCommandPath getCommandPath(String commandPath) {
        return getPath(commandPath);
    }
}

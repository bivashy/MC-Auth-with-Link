package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultCommandPaths implements ConfigurationHolder, MessengerCommandPaths {

    @ConfigField("self")
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

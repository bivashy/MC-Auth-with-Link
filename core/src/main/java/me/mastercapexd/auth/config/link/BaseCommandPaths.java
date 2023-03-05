package me.mastercapexd.auth.config.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;
import com.bivashy.auth.api.config.link.command.LinkCommandPaths;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class BaseCommandPaths implements ConfigurationHolder, LinkCommandPaths {
    @ConfigField("self")
    private ConfigurationHolderMap<BaseCommandPath> defaultCommands = new ConfigurationHolderMap<>();

    public BaseCommandPaths(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BaseCommandPath getPath(String commandKey) {
        return defaultCommands.getOrDefault(commandKey, null);
    }

    @Override
    public LinkCommandPathSettings getCommandPath(String commandPath) {
        return getPath(commandPath);
    }
}

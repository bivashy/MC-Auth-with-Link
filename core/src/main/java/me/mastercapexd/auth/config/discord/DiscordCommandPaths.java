package me.mastercapexd.auth.config.discord;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCommandPaths;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class DiscordCommandPaths implements ConfigurationHolder, LinkCommandPaths {
    @ConfigField("self")
    private ConfigurationHolderMap<DiscordCommandSettings> defaultCommands = new ConfigurationHolderMap<>();

    public DiscordCommandPaths(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public DiscordCommandSettings getCommandPath(String commandPath) {
        return defaultCommands.getOrDefault(commandPath, null);
    }
}

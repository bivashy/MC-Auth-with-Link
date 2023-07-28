package me.mastercapexd.auth.config.discord;

import java.util.Map;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.link.BaseCommandPath;

public class DiscordCommandSettings extends BaseCommandPath {
    @ConfigField("arguments")
    private ConfigurationHolderMap<DiscordCommandArgumentSettings> arguments = new ConfigurationHolderMap<>();

    public DiscordCommandSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public Map<String, DiscordCommandArgumentSettings> getArguments() {
        return arguments;
    }
}

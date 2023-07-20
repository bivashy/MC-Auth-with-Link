package me.mastercapexd.auth.config.discord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.annotation.ImportantField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class DiscordCommandSettings implements ConfigurationHolder, LinkCommandPathSettings {
    @ImportantField
    @ConfigField("main-command")
    private String commandPath = null;
    @ConfigField("aliases")
    private List<String> aliases = new ArrayList<>();
    @ConfigField("arguments")
    private ConfigurationHolderMap<DiscordCommandArgumentSettings> arguments = new ConfigurationHolderMap<>();

    public DiscordCommandSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public String getCommandPath() {
        return commandPath;
    }

    @Override
    public String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

    public Map<String, DiscordCommandArgumentSettings> getArguments() {
        return arguments;
    }
}

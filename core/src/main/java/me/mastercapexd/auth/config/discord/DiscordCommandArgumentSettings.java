package me.mastercapexd.auth.config.discord;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class DiscordCommandArgumentSettings implements ConfigurationHolder {
    @ConfigField("name")
    private String name;
    @ConfigField("description")
    private String description;

    public DiscordCommandArgumentSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

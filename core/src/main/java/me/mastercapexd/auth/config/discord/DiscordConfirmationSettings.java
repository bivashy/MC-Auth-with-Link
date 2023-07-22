package me.mastercapexd.auth.config.discord;

import java.util.Collections;
import java.util.Map;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.link.BaseConfirmationSettings;

public class DiscordConfirmationSettings extends BaseConfirmationSettings {
    @ConfigField("guild-id")
    private long guildId;
    @ConfigField("role-modification")
    private ConfigurationHolderMap<RoleModificationSettings> roleModificationSettings = new ConfigurationHolderMap<>();

    public DiscordConfirmationSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public long getGuildId() {
        return guildId;
    }

    public Map<String, RoleModificationSettings> getRoleModificationSettings() {
        return Collections.unmodifiableMap(roleModificationSettings);
    }
}

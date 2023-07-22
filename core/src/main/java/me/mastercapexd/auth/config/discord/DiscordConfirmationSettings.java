package me.mastercapexd.auth.config.discord;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.duration.ConfigurationDuration;
import com.bivashy.auth.api.config.link.stage.LinkConfirmationSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.util.RandomCodeFactory;

public class DiscordConfirmationSettings implements ConfigurationHolder, LinkConfirmationSettings {
    @ConfigField("remove-delay")
    private ConfigurationDuration removeDelay = new ConfigurationDuration(120 * 1000);
    @ConfigField("code-length")
    private int codeLength = 6;
    @ConfigField("can-toggle")
    private boolean canToggleConfirmation = false;
    @ConfigField("code-characters")
    private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
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

    public ConfigurationHolderMap<RoleModificationSettings> getRoleModificationSettings() {
        return roleModificationSettings;
    }

    @Override
    public ConfigurationDuration getRemoveDelay() {
        return removeDelay;
    }

    @Override
    public int getCodeLength() {
        return codeLength;
    }

    @Override
    public boolean canToggleConfirmation() {
        return canToggleConfirmation;
    }

    @Override
    public String getCodeCharacters() {
        return codeCharacters;
    }

    @Override
    public String generateCode() {
        return RandomCodeFactory.generateCode(codeLength, codeCharacters);
    }
}

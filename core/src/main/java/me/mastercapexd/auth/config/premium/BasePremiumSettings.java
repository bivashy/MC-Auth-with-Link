package me.mastercapexd.auth.config.premium;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.premium.PremiumSettings;
import com.bivashy.auth.api.config.premium.ProfileConflictResolutionStrategy;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasePremiumSettings implements ConfigurationHolder, PremiumSettings {
    @ConfigField("enabled")
    private boolean enabled = true;
    @ConfigField("block-offline-players-with-premium-name")
    private boolean blockOfflinePlayersWithPremiumName = false;
    @ConfigField("profile-conflict-resolution-strategy")
    private ProfileConflictResolutionStrategy profileConflictResolutionStrategy = ProfileConflictResolutionStrategy.BLOCK;
    @ConfigField("authentication-steps")
    private List<String> authenticationSteps = Collections.singletonList("ENTER_SERVER");

    public BasePremiumSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BasePremiumSettings() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean getBlockOfflinePlayersWithPremiumName() {
        return blockOfflinePlayersWithPremiumName;
    }

    @Override
    public ProfileConflictResolutionStrategy getProfileConflictResolutionStrategy() {
        return profileConflictResolutionStrategy;
    }

    @Override
    public List<String> getAuthenticationSteps() {
        return Collections.unmodifiableList(authenticationSteps);
    }
}

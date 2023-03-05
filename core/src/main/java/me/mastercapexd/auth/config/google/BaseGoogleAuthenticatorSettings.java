package me.mastercapexd.auth.config.google;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.GoogleAuthenticatorSettings;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

public class BaseGoogleAuthenticatorSettings implements ConfigurationHolder, GoogleAuthenticatorSettings {
    @ConfigField("enabled")
    private boolean enabled = false;

    public BaseGoogleAuthenticatorSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BaseGoogleAuthenticatorSettings() {
    }

    public boolean isEnabled() {
        return enabled;
    }
}

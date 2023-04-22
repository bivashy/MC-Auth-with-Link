package me.mastercapexd.auth.config.google;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.GoogleAuthenticatorSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

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

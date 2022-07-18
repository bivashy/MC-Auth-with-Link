package me.mastercapexd.auth.config.google;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class GoogleAuthenticatorSettings implements ConfigurationHolder {
    @ConfigField("enabled")
    private boolean enabled = false;

    public GoogleAuthenticatorSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public boolean isEnabled() {
        return enabled;
    }
}

package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.duration.ConfigurationDuration;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultConfirmationSettings implements ConfigurationHolder, MessengerConfirmationSettings {
    @ConfigField("remove-delay")
    private ConfigurationDuration removeDelay = new ConfigurationDuration(120 * 1000);
    @ConfigField("code-length")
    private int codeLength = 6;
    @ConfigField("can-toggle")
    private boolean canToggleConfirmation = false;
    @ConfigField("code-characters")
    private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public DefaultConfirmationSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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
}

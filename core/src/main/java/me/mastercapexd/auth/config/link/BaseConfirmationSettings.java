package me.mastercapexd.auth.config.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.duration.ConfigurationDuration;
import com.bivashy.auth.api.config.link.stage.LinkConfirmationSettings;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.util.RandomCodeFactory;

public class BaseConfirmationSettings implements ConfigurationHolder, LinkConfirmationSettings {
    @ConfigField("remove-delay")
    private ConfigurationDuration removeDelay = new ConfigurationDuration(120 * 1000);
    @ConfigField("code-length")
    private int codeLength = 6;
    @ConfigField("can-toggle")
    private boolean canToggleConfirmation = false;
    @ConfigField("code-characters")
    private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public BaseConfirmationSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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

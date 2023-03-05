package me.mastercapexd.auth.config.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.stage.LinkRestoreSettings;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.util.RandomCodeFactory;

public class BaseRestoreSettings implements ConfigurationHolder, LinkRestoreSettings {
    @ConfigField("code-length")
    private int codeLength = 6;
    @ConfigField("code-characters")
    private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public BaseRestoreSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public int getCodeLength() {
        return codeLength;
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

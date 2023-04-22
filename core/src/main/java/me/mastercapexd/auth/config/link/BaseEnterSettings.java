package me.mastercapexd.auth.config.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.stage.LinkEnterSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseEnterSettings implements ConfigurationHolder, LinkEnterSettings {
    @ConfigField("enter-delay")
    private int enterDelay = 60;

    public BaseEnterSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public int getEnterDelay() {
        return enterDelay;
    }
}

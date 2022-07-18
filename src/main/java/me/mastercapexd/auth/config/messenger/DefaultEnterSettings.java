package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultEnterSettings implements ConfigurationHolder, MessengerEnterSettings {
    @ConfigField("enter-delay")
    private int enterDelay = 60;

    public DefaultEnterSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public int getEnterDelay() {
        return enterDelay;
    }
}

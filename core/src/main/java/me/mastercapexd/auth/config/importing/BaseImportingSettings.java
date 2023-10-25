package me.mastercapexd.auth.config.importing;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.importing.ImportingSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseImportingSettings implements ConfigurationHolder, ImportingSettings {
    @ConfigField("retrieval-size")
    private int retrievalSize;

    public BaseImportingSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public int getRetrievalSize() {
        return retrievalSize;
    }

}

package me.mastercapexd.auth.config.migration;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.migration.MigrationSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseMigrationSettings implements ConfigurationHolder, MigrationSettings {
    @ConfigField("retrieval-size")
    private int retrievalSize;

    public BaseMigrationSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public int getRetrievalSize() {
        return retrievalSize;
    }

}

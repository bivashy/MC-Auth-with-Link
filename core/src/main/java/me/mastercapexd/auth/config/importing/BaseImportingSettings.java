package me.mastercapexd.auth.config.importing;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.importing.ImportingSettings;
import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class BaseImportingSettings implements ConfigurationHolder, ImportingSettings {

    @ConfigField("sources")
    private ConfigurationHolderMap<BaseImportingSourceSettings> sourceSettings;

    public BaseImportingSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public Optional<ImportingSourceSettings> sourceSettings(String sourceType) {
        return Optional.ofNullable(sourceSettings.get(sourceType));
    }

}

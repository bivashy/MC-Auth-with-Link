package me.mastercapexd.auth.config.storage.schema;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.schema.SchemaSettings;
import com.bivashy.auth.api.config.database.schema.TableSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class BaseSchemaSettings implements ConfigurationHolder, SchemaSettings {
    @ConfigField("self")
    private ConfigurationHolderMap<BaseTableSettings> tableSettings = new ConfigurationHolderMap<>();

    public BaseSchemaSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BaseSchemaSettings() {
    }

    @Override
    public Optional<TableSettings> getTableSettings(String key) {
        return Optional.ofNullable(tableSettings.get(key));
    }
}

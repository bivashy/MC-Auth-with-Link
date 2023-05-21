package me.mastercapexd.auth.config.storage.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.schema.TableSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseTableSettings implements ConfigurationHolder, TableSettings {
    @ConfigField("table-name")
    private String tableName;
    private Map<String, String> columnNames = new HashMap<>();

    public BaseTableSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
        ConfigurationSectionHolder columnsSectionHolder = sectionHolder.section("columns");
        columnNames = columnsSectionHolder.keys().stream().collect(Collectors.toMap(Function.identity(), columnsSectionHolder::getString));
    }

    public BaseTableSettings(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public Optional<String> getColumnName(String key) {
        return Optional.ofNullable(columnNames.get(key));
    }
}

package me.mastercapexd.auth.config.importing;

import java.io.File;
import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.bivashy.auth.api.database.DatabaseConnectionProvider;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseImportingSourceSettings implements ConfigurationHolder, ImportingSourceSettings {

    private final ConfigurationSectionHolder sectionHolder;
    @ConfigField("type")
    private DatabaseConnectionProvider connectionProvider;
    @ConfigField("driver-url")
    private String driverUrl;
    @ConfigField("cache-driver-path")
    private File cacheDriverPath = new File(AuthPlugin.instance().getFolder(), "importing" + File.separator + "database-driver.jar");
    @ConfigField("jdbc-url")
    private String jdbcUrl;
    @ConfigField("username")
    private String username;
    @ConfigField("password")
    private String password;

    public BaseImportingSourceSettings(ConfigurationSectionHolder sectionHolder) {
        this.sectionHolder = sectionHolder;
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public File getDriverPath() {
        return cacheDriverPath;
    }

    @Override
    public String getDriverDownloadUrl() {
        if (connectionProvider != null)
            return connectionProvider.getDriverDownloadUrl();
        return driverUrl;
    }

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Optional<String> getProperty(String key) {
        if (!sectionHolder.contains(key))
            return Optional.empty();
        return Optional.of(sectionHolder.getString(key));
    }

}

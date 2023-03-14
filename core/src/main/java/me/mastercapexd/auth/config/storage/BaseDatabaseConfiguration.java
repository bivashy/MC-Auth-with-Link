package me.mastercapexd.auth.config.storage;

import java.io.File;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.DatabaseSettings;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.RawURLProvider;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.SqlConnectionUrl;

public class BaseDatabaseConfiguration implements ConfigurationHolder, DatabaseSettings {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    @SqlConnectionUrl
    @ImportantField
    @ConfigField("url")
    private RawURLProvider urlProvider;
    @ImportantField
    @ConfigField("download-url")
    private String driverDownloadUrl;
    @ConfigField("username")
    private String username;
    @ConfigField("password")
    private String password;
    @ConfigField("cache-driver-path")
    private File cacheDriverPath = new File(PLUGIN.getFolder(), "database-driver.jar");

    public BaseDatabaseConfiguration(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BaseDatabaseConfiguration(RawURLProvider urlProvider, String driverDownloadUrl, String username, String password) {
        this.urlProvider = urlProvider;
        this.driverDownloadUrl = driverDownloadUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getConnectionUrl() {
        return urlProvider.url();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDriverDownloadUrl() {
        return driverDownloadUrl;
    }

    @Override
    public File getCacheDriverPath() {
        return cacheDriverPath;
    }
}
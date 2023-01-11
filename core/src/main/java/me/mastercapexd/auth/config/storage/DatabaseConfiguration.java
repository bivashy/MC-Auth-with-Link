package me.mastercapexd.auth.config.storage;

import java.io.File;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.RawURLProvider;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.SqlConnectionUrl;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DatabaseConfiguration implements ConfigurationHolder {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
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

    public DatabaseConfiguration(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public DatabaseConfiguration(RawURLProvider urlProvider, String driverDownloadUrl, String username, String password) {
        this.urlProvider = urlProvider;
        this.driverDownloadUrl = driverDownloadUrl;
        this.username = username;
        this.password = password;
    }

    public String getConnectionUrl() {
        return urlProvider.url();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverDownloadUrl() {
        return driverDownloadUrl;
    }

    public File getCacheDriverPath() {
        return cacheDriverPath;
    }
}

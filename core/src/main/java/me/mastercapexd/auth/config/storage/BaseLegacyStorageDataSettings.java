package me.mastercapexd.auth.config.storage;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.LegacyStorageDataSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.annotation.ImportantField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseLegacyStorageDataSettings implements ConfigurationHolder, LegacyStorageDataSettings {
    @ImportantField
    @ConfigField("host")
    private String host = null;
    @ImportantField
    @ConfigField("database")
    private String database = null;
    @ImportantField
    @ConfigField("username")
    private String user = null;
    @ImportantField
    @ConfigField("password")
    private String password = null;
    @ImportantField
    @ConfigField("port")
    private int port = 0;

    public BaseLegacyStorageDataSettings(ConfigurationSectionHolder configurationSection) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(configurationSection, this);
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getPort() {
        return port;
    }
}
package me.mastercapexd.auth.config.storage;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class StorageDataSettings implements ConfigurationHolder {

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

    public StorageDataSettings(ConfigurationSectionHolder configurationSection) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(configurationSection, this);
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }
}
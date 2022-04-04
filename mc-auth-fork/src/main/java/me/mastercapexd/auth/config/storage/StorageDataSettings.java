package me.mastercapexd.auth.config.storage;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.annotations.ImportantField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class StorageDataSettings implements ConfigurationHolder {

	@ImportantField
	@ConfigField
	private String host = null;
	@ImportantField
	@ConfigField
	private String database = null;
	@ImportantField
	@ConfigField("username")
	private String user = null;
	@ImportantField
	@ConfigField
	private String password = null;
	@ImportantField
	@ConfigField
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
package me.mastercapexd.auth.objects;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.annotations.ImportantField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class StorageDataSettings extends BungeeConfigurationHolder {

	@ImportantField
	@ConfigField
	private String host = null;
	@ImportantField
	@ConfigField
	private String database = null;
	@ImportantField
	@ConfigField(path = "username")
	private String user = null;
	@ImportantField
	@ConfigField
	private String password = null;
	@ImportantField
	@ConfigField
	private int port = 0;

	public StorageDataSettings(Configuration configurationSection) {
		init(configurationSection);
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
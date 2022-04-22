package me.mastercapexd.auth.storage.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.storage.StorageDataSettings;
import me.mastercapexd.auth.storage.sql.SQLAccountStorage;

public class MySQLAccountStorage extends SQLAccountStorage {
	private String url;
	private String user;
	private String password;
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `auth` (`id` VARCHAR(64) PRIMARY KEY, `uuid` VARCHAR(64) NOT NULL, `name` VARCHAR(32) NOT NULL, `password` VARCHAR(255),`google_key` VARCHAR(64), `vkId` INT NOT NULL,`vk_confirm_enabled` VARCHAR(5), `last_quit` BIGINT, `last_ip` VARCHAR(64), `last_session_start` BIGINT, `id_type` VARCHAR(32) NOT NULL, `hash_type` VARCHAR(32) NOT NULL);";
	private static final String SELECT_BY_ID = "SELECT * FROM `auth` WHERE `id` = ? LIMIT 1;";
	private static final String SELECT_BY_NAME = "SELECT * FROM `auth` WHERE `name` = ? LIMIT 1;";
	private static final String SELECT_BY_VKID = "SELECT * FROM `auth` WHERE `vkId` = ?;";
	private static final String SELECT_BY_LINK_ID = "SELECT * FROM `auth` WHERE ? IN(vkId);";
	private static final String SELECT_BY_LAST_QUIT_ORDERED = "SELECT * FROM `auth` ORDER BY `last_quit` DESC LIMIT ?;";
	private static final String SELECT_ALL = "SELECT * FROM `auth`;";
	private static final String SELECT_ALL_LINKED = "SELECT * FROM `auth` WHERE `vkId` NOT IN(?);";
	private static final String SELECT_VKIDs = "SELECT `vkId` FROM `auth`;";
	private static final String UPDATE_ID = "INSERT INTO `auth` (`id`, `uuid`, `name`, `password`,`google_key`,`vkId`,`vk_confirm_enabled`, `last_quit`, `last_ip`, `last_session_start`, `id_type`, `hash_type`) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
			+ "`id` = VALUES(`id`), `uuid` = VALUES(`uuid`), `name` = VALUES(`name`), "
			+ "`password` = VALUES(`password`),`google_key` = VALUES(`google_key`),`vkId` = VALUES(`vkId`),`vk_confirm_enabled` = VALUES(`vk_confirm_enabled`), `last_quit` = VALUES(`last_quit`), `last_ip` = VALUES(`last_ip`), `last_session_start` = VALUES(`last_session_start`), `id_type` = VALUES(`id_type`), `hash_type` = VALUES(`hash_type`);";
	private static final String DELETE = "DELETE FROM `auth` WHERE `id`=?;";

	public MySQLAccountStorage(PluginConfig config, AccountFactory accountFactory) {
		super(config, accountFactory, CREATE_TABLE, SELECT_BY_ID, SELECT_BY_NAME, SELECT_BY_VKID, SELECT_BY_LINK_ID,
				SELECT_BY_LAST_QUIT_ORDERED, SELECT_VKIDs, SELECT_ALL, SELECT_ALL_LINKED, UPDATE_ID, DELETE);

		StorageDataSettings dataSettings = config.getStorageDataSettings();
		String host = dataSettings.getHost();
		int port = dataSettings.getPort();
		String databaseName = dataSettings.getDatabase();
		user = dataSettings.getUser();
		password = dataSettings.getPassword();
		url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.createTable();
		this.createColumns();
	}

	@Override
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
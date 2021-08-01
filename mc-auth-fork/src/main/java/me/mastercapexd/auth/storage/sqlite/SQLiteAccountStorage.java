package me.mastercapexd.auth.storage.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;	

import me.mastercapexd.auth.AccountFactory;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.storage.sql.SQLAccountStorage;

public class SQLiteAccountStorage extends SQLAccountStorage {
	private String url;
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `auth` (`id` VARCHAR(50) PRIMARY KEY, `uuid` VARCHAR(64) NOT NULL, `name` VARCHAR(32) NOT NULL, `password` VARCHAR(255), `vkId` INTEGER NOT NULL, `last_quit` INTEGER, `last_ip` VARCHAR(64), `last_session_start` INTEGER, `id_type` VARCHAR(32) NOT NULL, `hash_type` VARCHAR(32) NOT NULL);";
	private static final String SELECT_BY_ID = "SELECT * FROM `auth` WHERE `id` = ? LIMIT 1;";
	private static final String SELECT_BY_VKID = "SELECT * FROM `auth` WHERE `vkId` = ?;";
	private static final String SELECT_BY_LAST_QUIT_ORDERED = "SELECT * FROM `auth` ORDER BY `last_quit` DESC LIMIT ?;";
	private static final String SELECT_ALL = "SELECT * FROM `auth`;";
	private static final String SELECT_ALL_LINKED = "SELECT * FROM `auth` WHERE `vkId` NOT IN(-1);";
	private static final String SELECT_VKIDs = "SELECT `vkId` FROM `auth`;";
	private static final String UPDATE_ID = "REPLACE INTO `auth` (`id`, `uuid`, `name`, `password`, `last_quit`, `vkId`, `last_ip`, `last_session_start`, `id_type`, `hash_type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
	private static final String DELETE = "DELETE FROM `auth` WHERE `id`=?;";

	public SQLiteAccountStorage(PluginConfig config, AccountFactory accountFactory, File parent) {
		super(config, accountFactory, CREATE_TABLE, SELECT_BY_ID, SELECT_BY_VKID, SELECT_BY_LAST_QUIT_ORDERED,
				SELECT_VKIDs, SELECT_ALL,SELECT_ALL_LINKED, UPDATE_ID, DELETE);

		File file = new File(parent + File.separator + "auth.db");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		url = "jdbc:sqlite:" + file.getPath();
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		this.createTable();
	}

	@Override
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}
}
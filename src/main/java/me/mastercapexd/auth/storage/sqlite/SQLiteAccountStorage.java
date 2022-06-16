package me.mastercapexd.auth.storage.sqlite;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.storage.sql.SQLAccountStorage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteAccountStorage extends SQLAccountStorage {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `auth` (`id` VARCHAR(50) PRIMARY KEY, `uuid` VARCHAR(64) NOT NULL, `name` VARCHAR" +
            "(32) NOT NULL, `password` VARCHAR(255),`google_key` VARCHAR(64), `vkId` INTEGER NOT NULL,`vk_confirmation_enabled` BIT,`telegram_id` BIGINT NOT " +
            "NULL,`telegram_confirmation_enabled` BIT, `last_quit` BIGINT, `last_ip` VARCHAR(64), `last_session_start` INTEGER, `id_type` VARCHAR(32) NOT " +
            "NULL, `hash_type` VARCHAR(32) NOT NULL);";
    private static final String SELECT_BY_ID = "SELECT * FROM `auth` WHERE `id` = ? LIMIT 1;";
    private static final String SELECT_BY_NAME = "SELECT * FROM `auth` WHERE `name` = ? LIMIT 1;";
    private static final String SELECT_BY_VKID = "SELECT * FROM `auth` WHERE `vkId` = ?;";
    private static final String SELECT_BY_LINK_ID = "SELECT * FROM `auth` WHERE ? IN(vkId,telegram_id);";
    private static final String SELECT_BY_LAST_QUIT_ORDERED = "SELECT * FROM `auth` ORDER BY `last_quit` DESC LIMIT ?;";
    private static final String SELECT_ALL = "SELECT * FROM `auth`;";
    private static final String SELECT_ALL_LINKED = "SELECT * FROM `auth` WHERE NOT vkId = ? OR NOT telegram_id = ?;";
    private static final String SELECT_VKIDs = "SELECT `vkId` FROM `auth`;";
    private static final String UPDATE_ID = "REPLACE INTO `auth` (`id`, `uuid`, `name`, `password`,`google_key`, `vkId`,`vk_confirmation_enabled`," +
            "`telegram_id`,`telegram_confirmation_enabled`, `last_quit`,  `last_ip`, `last_session_start`, `id_type`, `hash_type`) VALUES (?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?,?,?);";
    private static final String DELETE = "DELETE FROM `auth` WHERE `id`=?;";
    private String url;

    public SQLiteAccountStorage(PluginConfig config, AccountFactory accountFactory, File parent) {
        super(config, accountFactory, CREATE_TABLE, SELECT_BY_ID, SELECT_BY_NAME, SELECT_BY_VKID, SELECT_BY_LINK_ID, SELECT_BY_LAST_QUIT_ORDERED,
                SELECT_VKIDs, SELECT_ALL, SELECT_ALL_LINKED, UPDATE_ID, DELETE);

        File file = new File(parent + File.separator + "auth.db");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        url = "jdbc:sqlite:" + file.getPath();
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        this.createTable();
        this.createColumns();
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public String getConfigurationName() {
        return "SQLITE";
    }
}
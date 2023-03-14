package me.mastercapexd.auth.database.migration;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.database.migration.ConditionalBatchTaskMigrator;
import com.bivashy.auth.api.database.migration.ConditionalMigrator;
import com.bivashy.auth.api.database.migration.Migrator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;

public class Migrations {
    public static final ConditionalMigrator<AuthAccount, Long> LEGACY_MC_AUTH_TO_NEW_MIGRATOR = new ConditionalMigrator<AuthAccount, Long>() {
        private final String migrationQuery = "INSERT INTO mc_auth_accounts SELECT null,id, id_type, hash_type, last_ip, uuid, name, password, last_quit, " +
                "last_session_start FROM auth;";

        @Override
        public boolean shouldMigrate(ConnectionSource connectionSource, Dao<? extends AuthAccount, Long> dao) throws SQLException {
            return connectionSource.getReadOnlyConnection(dao.getTableName()).isTableExists("auth") && dao.queryForFirst() == null;
        }

        @Override
        public void migrate(ConnectionSource connectionSource, Dao<? extends AuthAccount, Long> dao) throws SQLException {
            dao.executeRawNoArgs(migrationQuery);
        }
    };
    public static final ConditionalMigrator<AccountLink, Long> AUTH_1_5_0_LINKS_MIGRATOR = new ConditionalBatchTaskMigrator<AccountLink, Long>() {
        private final List<String> migrationQueries = Arrays.asList(
                "INSERT INTO auth_links (link_type, link_user_id, link_enabled, account_id) SELECT 'VK', vkId, 1, mc_auth_accounts.id FROM " +
                        "auth JOIN mc_auth_accounts ON auth.id = mc_auth_accounts.player_id WHERE vkId IS NOT NULL AND vkId != -1;",
                "INSERT INTO auth_links (link_type, link_user_id, link_enabled, account_id) SELECT 'GOOGLE', google_key, 1, mc_auth_accounts.id FROM " +
                        "auth JOIN mc_auth_accounts ON auth.id = mc_auth_accounts.player_id WHERE google_key IS NOT NULL;");

        @Override
        public boolean shouldMigrate(ConnectionSource connectionSource, Dao<? extends AccountLink, Long> dao) throws SQLException {
            DatabaseConnection readOnlyConnection = connectionSource.getReadOnlyConnection(dao.getTableName());
            return readOnlyConnection.isTableExists("auth") &&
                    readOnlyConnection.getUnderlyingConnection().getMetaData().getColumns(null, null, "auth", "vk_confirm_enabled").next() &&
                    dao.queryForFirst() == null; // I didn`t have database versioning in the old version of plugin, and there is only one dirty way
        }

        @Override
        public Collection<String> getMigrationQueries() {
            return Collections.unmodifiableCollection(migrationQueries);
        }
    };
    public static final Migrator<AccountLink, Long> AUTH_1_6_0_LINKS_MIGRATOR = new ConditionalBatchTaskMigrator<AccountLink, Long>() {
        private final List<String> migrationQueries = Arrays.asList(
                "INSERT INTO auth_links (link_type, link_user_id, link_enabled, account_id) SELECT 'VK', vkId, vk_confirmation_enabled, mc_auth_accounts.id " +
                        "FROM auth JOIN mc_auth_accounts ON auth.id = mc_auth_accounts.player_id WHERE vkId IS NOT NULL AND vkId != -1;",
                "INSERT INTO auth_links (link_type, link_user_id, link_enabled, account_id) SELECT 'GOOGLE', google_key, 1, mc_auth_accounts.id FROM auth " +
                        "JOIN mc_auth_accounts ON auth.id = mc_auth_accounts.player_id WHERE google_key IS NOT NULL;",
                "INSERT INTO auth_links (link_type, link_user_id, link_enabled, account_id) SELECT 'TELEGRAM', telegram_id, telegram_confirmation_enabled, " +
                        "mc_auth_accounts.id FROM auth JOIN mc_auth_accounts ON auth.id = mc_auth_accounts.player_id WHERE telegram_id IS NOT NULL AND " +
                        "telegram_id != -1;");

        @Override
        public boolean shouldMigrate(ConnectionSource connectionSource, Dao<? extends AccountLink, Long> dao) throws SQLException {
            DatabaseConnection readOnlyConnection = connectionSource.getReadOnlyConnection(dao.getTableName());
            return readOnlyConnection.isTableExists("auth") &&
                    readOnlyConnection.getUnderlyingConnection().getMetaData().getColumns(null, null, "auth", "vk_confirmation_enabled").next() &&
                    dao.queryForFirst() == null; // I didn`t have database versioning in the old version of plugin, and there is only one dirty way
        }

        @Override
        public Collection<String> getMigrationQueries() {
            return Collections.unmodifiableCollection(migrationQueries);
        }
    };

    private Migrations() {
    }
}

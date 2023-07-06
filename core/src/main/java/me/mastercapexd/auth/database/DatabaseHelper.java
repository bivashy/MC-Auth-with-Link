package me.mastercapexd.auth.database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.DatabaseSettings;
import com.bivashy.auth.api.config.database.schema.SchemaSettings;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;

import me.mastercapexd.auth.config.storage.schema.BaseTableSettings;
import me.mastercapexd.auth.database.dao.AccountLinkDao;
import me.mastercapexd.auth.database.dao.AuthAccountDao;
import me.mastercapexd.auth.database.migration.MigrationCoordinator;
import me.mastercapexd.auth.database.migration.Migrations;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;
import me.mastercapexd.auth.database.persister.CryptoProviderPersister;
import me.mastercapexd.auth.util.DownloadUtil;
import me.mastercapexd.auth.util.DriverUtil;
import me.mastercapexd.auth.util.HashUtils;

public class DatabaseHelper {
    public static final String ID_FIELD_KEY = "id";
    private final MigrationCoordinator<AuthAccount, Long> authAccountMigrationCoordinator = new MigrationCoordinator<>();
    private final MigrationCoordinator<AccountLink, Long> accountLinkMigrationCoordinator = new MigrationCoordinator<>();
    private ConnectionSource connectionSource;
    private AuthAccountDao authAccountDao;
    private AccountLinkDao accountLinkDao;

    public DatabaseHelper(AuthPlugin plugin) {
        DatabaseSettings databaseConfiguration = plugin.getConfig().getDatabaseConfiguration();
        SchemaSettings schemaSettings = databaseConfiguration.getSchemaSettings();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Logger.setGlobalLogLevel(Level.WARNING);

                File cacheDriverFile = databaseConfiguration.getCacheDriverPath();
                URL downloadUrl = new URL(databaseConfiguration.getDriverDownloadUrl());
                String cacheDriverCheckSum = HashUtils.getFileCheckSum(cacheDriverFile, HashUtils.getMD5());
                if (!cacheDriverFile.exists() || cacheDriverCheckSum != null && !DownloadUtil.checkSum(HashUtils.mapToMd5URL(downloadUrl), cacheDriverCheckSum))
                    DownloadUtil.downloadFile(downloadUrl, cacheDriverFile);
                DriverUtil.loadDriver(cacheDriverFile, plugin.getClass().getClassLoader());

                DataPersisterManager.registerDataPersisters(new CryptoProviderPersister());

                this.connectionSource = new JdbcPooledConnectionSource(databaseConfiguration.getConnectionUrl(), databaseConfiguration.getUsername(),
                        databaseConfiguration.getPassword());

                this.accountLinkDao = new AccountLinkDao(connectionSource,
                        schemaSettings.getTableSettings("link").orElse(new BaseTableSettings("auth_links")), this);
                this.authAccountDao = new AuthAccountDao(connectionSource,
                        schemaSettings.getTableSettings("auth").orElse(new BaseTableSettings("mc_auth_accounts")), this);

                authAccountMigrationCoordinator.add(Migrations.HASH_ITERATION_COLUMN_MIGRATOR);
                authAccountMigrationCoordinator.add(Migrations.LEGACY_MC_AUTH_TO_NEW_MIGRATOR);
                accountLinkMigrationCoordinator.add(Migrations.AUTH_1_5_0_LINKS_MIGRATOR);
                accountLinkMigrationCoordinator.add(Migrations.AUTH_1_6_0_LINKS_MIGRATOR);

                if (databaseConfiguration.isMigrationEnabled()) {
                    authAccountMigrationCoordinator.migrate(connectionSource, authAccountDao);
                    accountLinkMigrationCoordinator.migrate(connectionSource, accountLinkDao);
                }
            } catch(SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public MigrationCoordinator<AuthAccount, Long> getAuthAccountMigrationCoordinator() {
        return authAccountMigrationCoordinator;
    }

    public MigrationCoordinator<AccountLink, Long> getAccountLinkMigrationCoordinator() {
        return accountLinkMigrationCoordinator;
    }

    public AuthAccountDao getAuthAccountDao() {
        return authAccountDao;
    }

    public AccountLinkDao getAccountLinkDao() {
        return accountLinkDao;
    }
}

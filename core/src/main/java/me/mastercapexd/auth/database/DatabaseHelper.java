package me.mastercapexd.auth.database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.DatabaseSettings;
import me.mastercapexd.auth.util.DownloadUtil;
import me.mastercapexd.auth.util.DriverUtil;
import com.bivashy.auth.api.util.HashUtils;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;

import me.mastercapexd.auth.database.dao.AccountLinkDao;
import me.mastercapexd.auth.database.dao.AuthAccountDao;
import me.mastercapexd.auth.database.migration.MigrationCoordinator;
import me.mastercapexd.auth.database.migration.Migrations;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;

public class DatabaseHelper {
    public static final String ID_FIELD_KEY = "id";
    private final MigrationCoordinator<AuthAccount, Long> authAccountMigrationCoordinator = new MigrationCoordinator<>();
    private final MigrationCoordinator<AccountLink, Long> accountLinkMigrationCoordinator = new MigrationCoordinator<>();
    private ConnectionSource connectionSource;
    private AuthAccountDao authAccountDao;
    private AccountLinkDao accountLinkDao;

    public DatabaseHelper(AuthPlugin plugin) {
        DatabaseSettings databaseConfiguration = plugin.getConfig().getDatabaseConfiguration();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Logger.setGlobalLogLevel(Level.WARNING);

                File cacheDriverFile = databaseConfiguration.getCacheDriverPath();
                URL downloadUrl = new URL(databaseConfiguration.getDriverDownloadUrl());
                String cacheDriverCheckSum = HashUtils.getFileCheckSum(cacheDriverFile, HashUtils.getMD5());
                if (!cacheDriverFile.exists() || cacheDriverCheckSum != null && !DownloadUtil.checkSum(HashUtils.mapToMd5URL(downloadUrl), cacheDriverCheckSum))
                    DownloadUtil.downloadFile(downloadUrl, cacheDriverFile);
                DriverUtil.loadDriver(cacheDriverFile, plugin.getClass().getClassLoader());

                this.connectionSource = new JdbcPooledConnectionSource(databaseConfiguration.getConnectionUrl(), databaseConfiguration.getUsername(),
                        databaseConfiguration.getPassword());

                this.accountLinkDao = new AccountLinkDao(connectionSource, this);
                this.authAccountDao = new AuthAccountDao(connectionSource, this);

                authAccountMigrationCoordinator.add(Migrations.LEGACY_MC_AUTH_TO_NEW_MIGRATOR);
                accountLinkMigrationCoordinator.add(Migrations.AUTH_1_5_0_LINKS_MIGRATOR);
                accountLinkMigrationCoordinator.add(Migrations.AUTH_1_6_0_LINKS_MIGRATOR);

                authAccountMigrationCoordinator.migrate(connectionSource, authAccountDao);
                accountLinkMigrationCoordinator.migrate(connectionSource, accountLinkDao);
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

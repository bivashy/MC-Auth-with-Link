package com.bivashy.auth.api.database;

import java.io.File;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.database.LegacyStorageDataSettings;

public enum DatabaseConnectionProvider {
    MYSQL("https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.31/mysql-connector-j-8.0.31.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:mysql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase();
        }
    }, SQLITE("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:sqlite:" + AuthPlugin.instance().getFolder().getAbsolutePath() + File.separator + "auth.db";
        }
    }, POSTGRESQL("https://repo1.maven.org/maven2/org/postgresql/postgresql/42.5.1/postgresql-42.5.1.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:postgresql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase();
        }
    }, MARIADB("https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.0.8/mariadb-java-client-3.0.8.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:mariadb://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase();
        }
    };
    private final String driverDownloadUrl;

    DatabaseConnectionProvider(String driverDownloadUrl) {
        this.driverDownloadUrl = driverDownloadUrl;
    }

    public String getDriverDownloadUrl() {
        return driverDownloadUrl;
    }

    public abstract String getConnectionUrl(LegacyStorageDataSettings settings);
}
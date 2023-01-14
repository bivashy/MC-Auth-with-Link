package me.mastercapexd.auth.storage;

import java.io.File;

import me.mastercapexd.auth.config.storage.LegacyStorageDataSettings;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public enum StorageType {
    MYSQL("https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.31/mysql-connector-j-8.0.31.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:mysql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase();
        }
    }, SQLITE("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar") {
        @Override
        public String getConnectionUrl(LegacyStorageDataSettings settings) {
            return "jdbc:sqlite:" + ProxyPlugin.instance().getFolder().getAbsolutePath() + File.separator + "auth.db";
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

    StorageType(String driverDownloadUrl) {
        this.driverDownloadUrl = driverDownloadUrl;
    }

    public String getDriverDownloadUrl() {
        return driverDownloadUrl;
    }

    public abstract String getConnectionUrl(LegacyStorageDataSettings settings);
}
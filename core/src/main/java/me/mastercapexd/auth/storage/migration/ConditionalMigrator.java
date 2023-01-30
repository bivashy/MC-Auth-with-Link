package me.mastercapexd.auth.storage.migration;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface ConditionalMigrator<T, ID> extends Migrator<T, ID> {
    boolean shouldMigrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException;

    void migrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException;

    @Override
    default void tryToMigrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException {
        if (shouldMigrate(connectionSource, dao))
            migrate(connectionSource, dao);
    }
}

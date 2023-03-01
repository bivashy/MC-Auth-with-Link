package com.bivashy.auth.api.database.migration;

import java.sql.SQLException;
import java.util.Collection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface ConditionalBatchTaskMigrator<T, ID> extends ConditionalMigrator<T, ID> {
    Collection<String> getMigrationQueries();

    @Override
    default void migrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException {
        try {
            dao.callBatchTasks(() -> {
                for (String query : getMigrationQueries())
                    dao.executeRawNoArgs(query);
                return null;
            });
        } catch(SQLException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

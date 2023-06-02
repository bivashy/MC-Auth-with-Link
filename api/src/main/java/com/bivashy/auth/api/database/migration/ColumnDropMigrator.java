package com.bivashy.auth.api.database.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface ColumnDropMigrator<T, ID> extends ConditionalMigrator<T, ID> {
    static <T, ID> ColumnDropMigrator<T, ID> of(String columnName) {
        return () -> columnName;
    }

    @Override
    default boolean shouldMigrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException {
        ResultSet balanceColumnResultSet = connectionSource.getReadOnlyConnection(dao.getTableName())
                .getUnderlyingConnection()
                .getMetaData()
                .getColumns(null, null, dao.getTableName(), getColumnName());
        return !balanceColumnResultSet.next();
    }

    @Override
    default void migrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException {
        dao.executeRawNoArgs("ALTER TABLE " + dao.getTableName() + " DROP COLUMN " + getColumnName());
    }

    String getColumnName();
}


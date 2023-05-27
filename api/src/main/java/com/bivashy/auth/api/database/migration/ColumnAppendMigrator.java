package com.bivashy.auth.api.database.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface ColumnAppendMigrator<T, ID> extends ConditionalMigrator<T, ID> {
    static <T, ID> ColumnAppendMigrator<T, ID> of(String columnName, Function<ConnectionSource, String> columnTypeFunction) {
        return new ColumnAppendMigrator<T, ID>() {
            @Override
            public String getColumnName() {
                return columnName;
            }

            @Override
            public String getColumnType(ConnectionSource connectionSource) {
                return columnTypeFunction.apply(connectionSource);
            }
        };
    }

    static <T, ID> ColumnAppendMigrator<T, ID> of(String columnName, String columnType) {
        return of(columnName, ignored -> columnType);
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
        dao.executeRawNoArgs("ALTER TABLE " + dao.getTableName() + " ADD COLUMN " + getColumnName() + " " + getColumnType(connectionSource));
    }

    String getColumnName();

    String getColumnType(ConnectionSource connectionSource);
}

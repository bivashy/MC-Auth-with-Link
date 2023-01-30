package me.mastercapexd.auth.storage.migration;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface ColumnAppendMigrator<T, ID> extends ConditionalMigrator<T, ID> {
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
        dao.executeRawNoArgs("ALTER TABLE users ADD COLUMN " + getColumnName() + " " + getColumnType(connectionSource));
    }

    String getColumnName();

    String getColumnType(ConnectionSource connectionSource);
}

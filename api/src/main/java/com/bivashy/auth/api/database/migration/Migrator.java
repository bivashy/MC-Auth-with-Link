package com.bivashy.auth.api.database.migration;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface Migrator<T, ID> {
    void tryToMigrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException;
}

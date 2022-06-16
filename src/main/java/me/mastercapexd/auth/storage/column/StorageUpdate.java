package me.mastercapexd.auth.storage.column;

import java.sql.Connection;
import java.sql.SQLException;

public interface StorageUpdate {
    void apply(Connection connection) throws SQLException;
}

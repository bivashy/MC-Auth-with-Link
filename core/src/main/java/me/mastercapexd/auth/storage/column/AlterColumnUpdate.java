package me.mastercapexd.auth.storage.column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlterColumnUpdate implements StorageUpdate {
    private final String columnName, columnType;
    private String defaultValue;

    public AlterColumnUpdate(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public AlterColumnUpdate(String columnName, String columnType, String defaultValue) {
        this(columnName, columnType);
        this.defaultValue = defaultValue;
    }

    @Override
    public void apply(Connection connection) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getColumns(null, null, "auth", columnName);
        if (resultSet.next())
            return;
        if (defaultValue == null) {
            connection.createStatement().execute("ALTER TABLE `auth` ADD `" + columnName + "` " + columnType + ";");
        } else {
            connection.createStatement().execute("ALTER TABLE `auth` ADD `" + columnName + "` " + columnType + " DEFAULT " + defaultValue + ";");
        }
    }
}

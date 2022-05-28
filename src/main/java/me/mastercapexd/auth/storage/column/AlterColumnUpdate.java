package me.mastercapexd.auth.storage.column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlterColumnUpdate implements StorageUpdate {
	private final String columnName, columnType;

	public AlterColumnUpdate(String columnName, String columnType) {
		this.columnName = columnName;
		this.columnType = columnType;
	}

	@Override
	public void apply(Connection connection) throws SQLException {
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, "auth", columnName);
		if (resultSet.next())
			return;
		connection.createStatement().execute("ALTER TABLE `auth` ADD `" + columnName + "` " + columnType + ";");
	}
}

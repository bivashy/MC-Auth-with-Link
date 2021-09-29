package me.mastercapexd.auth.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageColumn {
	private final String columnName, columnType;

	public StorageColumn(String columnName, String columnType) {
		this.columnName = columnName;
		this.columnType = columnType;
	}

	public boolean tryToCreateColumn(Connection connection) throws SQLException {
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, "auth", columnName);
		if (resultSet.next())
			return false;
		return connection.createStatement()
				.execute("ALTER TABLE `auth` ADD `" + columnName + "` " + columnType + ";");
	}

}

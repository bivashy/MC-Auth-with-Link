package me.mastercapexd.auth.storage.column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DropColumnUpdate implements StorageUpdate{
	private final String columnName;

	public DropColumnUpdate(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public void apply(Connection connection) throws SQLException {
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, "auth", columnName);
		if (!resultSet.next())
			return;
		connection.createStatement().execute("ALTER TABLE `auth` DROP `" + columnName + "`;");
	}
}

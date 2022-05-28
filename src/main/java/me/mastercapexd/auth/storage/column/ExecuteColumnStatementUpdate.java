package me.mastercapexd.auth.storage.column;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteColumnStatementUpdate implements StorageUpdate{
	private final String columnName, statement;

	public ExecuteColumnStatementUpdate(String columnName, String statement) {
		this.columnName = columnName;
		this.statement = statement;
	}

	@Override
	public void apply(Connection connection) throws SQLException {
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, "auth", columnName);
		if (!resultSet.next())
			return;
		connection.createStatement().execute(statement);
	}
}

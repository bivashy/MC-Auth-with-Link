package me.mastercapexd.auth.database.importing;

import java.sql.SQLException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import me.mastercapexd.auth.database.importing.exception.ImportingException;

public class DatabaseStream implements AutoCloseable {

    private final ConnectionSource connectionSource;
    private final Dao<?, ?> dao;

    public DatabaseStream(ImportingSourceSettings settings) throws SQLException {
        connectionSource = new JdbcPooledConnectionSource(settings.getJdbcUrl(), settings.getUsername(), settings.getPassword());
        DatabaseTableConfig<DummyModel> tableConfig = new DatabaseTableConfig<>();
        tableConfig.setDataClass(DummyModel.class);
        dao = DaoManager.createDao(connectionSource, tableConfig);
    }

    public <T> Stream<T> execute(String query, RawRowMapper<T> rowMapper, String... args) throws SQLException {
        GenericRawResults<T> results = dao.queryRaw(query, rowMapper, args);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(results.iterator(), Spliterator.IMMUTABLE), false)
                .onClose(() -> {
                    try {
                        results.close();
                    } catch (Exception e) {
                        throw new ImportingException("Cannot close GenericRawResults of query '" + query + "'", e);
                    }
                }).onClose(() -> {
                    try {
                        connectionSource.close();
                    } catch (Exception e) {
                        throw new ImportingException("Cannot close JdbcPooledConnectionSource", e);
                    }
                });
    }

    public boolean tableExists(String tableName) throws SQLException {
        return connectionSource.getReadOnlyConnection(tableName).isTableExists(tableName);
    }

    @Override
    public void close() throws Exception {
        connectionSource.close();
    }

    public static class DummyModel {

        @DatabaseField(generatedId = true)
        public long id;

    }

}

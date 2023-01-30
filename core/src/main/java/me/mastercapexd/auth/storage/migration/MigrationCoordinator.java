package me.mastercapexd.auth.storage.migration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public class MigrationCoordinator<T, ID> {
    private final List<Migrator<T, ID>> migrators = new ArrayList<>();

    public void migrate(ConnectionSource connectionSource, Dao<? extends T, ID> dao) throws SQLException {
        try {
            for (Migrator<T, ID> migrator : migrators) {
                migrator.tryToMigrate(connectionSource, dao);
            }
        } catch(SQLException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void add(Migrator<T, ID> migrator) {
        migrators.add(migrator);
    }

    public void remove(Migrator<T, ID> migrator) {
        migrators.remove(migrator);
    }

    public List<Migrator<T, ID>> getMigrators() {
        return Collections.unmodifiableList(migrators);
    }
}

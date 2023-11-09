package me.mastercapexd.auth.database.importing;

import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;

import me.mastercapexd.auth.database.dao.SupplierExceptionCatcher;

public class BatchOperationExecutor {

    private final SupplierExceptionCatcher exceptionCatcher = new SupplierExceptionCatcher();
    private final Dao<?, ?> dao;

    public BatchOperationExecutor(Dao<?, ?> dao) {
        this.dao = dao;
    }

    public <T> T execute(Callable<T> callable) {
        return exceptionCatcher.execute(() -> dao.callBatchTasks(callable));
    }

}

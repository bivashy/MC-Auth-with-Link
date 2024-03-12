package me.mastercapexd.auth.database.importing;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import me.mastercapexd.auth.database.dao.AuthAccountDao;
import me.mastercapexd.auth.database.dao.SupplierExceptionCatcher;
import me.mastercapexd.auth.database.importing.exception.ImportingException;

public class BatchOperationExecutor {

    private final SupplierExceptionCatcher exceptionCatcher = new SupplierExceptionCatcher() {
        @Override
        public void processException(Throwable throwable) {
            throw new ImportingException("Cannot execute batch operation", throwable);
        }
    };
    private final Supplier<AuthAccountDao> dao;

    public BatchOperationExecutor(Supplier<AuthAccountDao> dao) {
        this.dao = dao;
    }

    public <T> T execute(Callable<T> callable) {
        return exceptionCatcher.execute(() -> dao.get().callBatchTasks(callable));
    }

}

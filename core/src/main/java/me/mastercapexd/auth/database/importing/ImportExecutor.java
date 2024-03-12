package me.mastercapexd.auth.database.importing;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import me.mastercapexd.auth.database.dao.AuthAccountDao;
import me.mastercapexd.auth.database.importing.model.ImportStatistics;
import me.mastercapexd.auth.database.importing.model.PortableAccount;

public class ImportExecutor {

    private final BatchOperationExecutor batchOperationExecutor;
    private final Supplier<AuthAccountDao> daoSupplier;

    public ImportExecutor(Supplier<AuthAccountDao> daoSupplier) {
        batchOperationExecutor = new BatchOperationExecutor(daoSupplier);
        this.daoSupplier = daoSupplier;
    }

    public CompletableFuture<ImportStatistics> performImport(ImportSource source) {
        CompletableFuture<ImportStatistics> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                future.complete(runImport(source));
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
        }, "mcAuth-Import").start();
        return future;
    }

    ImportStatistics runImport(ImportSource source) {
        ImportStatistics statistics = new ImportStatistics();
        ImportSink sink = new ImportSink(batchOperationExecutor, statistics, daoSupplier.get());
        transferAccounts(source, sink);
        return statistics;
    }

    private void transferAccounts(ImportSource source, ImportSink sink) {
        try (Stream<PortableAccount> accountStream = source.sourceAccounts()) {
            accountStream.forEach(sink::addAccountAndLinks);
        }
    }

}

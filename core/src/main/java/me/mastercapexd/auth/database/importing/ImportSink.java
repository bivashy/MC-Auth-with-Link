package me.mastercapexd.auth.database.importing;

import me.mastercapexd.auth.database.dao.AuthAccountDao;
import me.mastercapexd.auth.database.importing.adapter.PortableAccountAdapter;
import me.mastercapexd.auth.database.importing.adapter.PortableAccountLinkAdapter;
import me.mastercapexd.auth.database.importing.model.ImportStatistics;
import me.mastercapexd.auth.database.importing.model.PortableAccount;
import me.mastercapexd.auth.database.importing.model.PortableAccountLink;

public class ImportSink {

    private final BatchOperationExecutor operationExecutor;
    private final ImportStatistics statistics;
    private final AuthAccountDao accountDao;

    public ImportSink(BatchOperationExecutor operationExecutor, ImportStatistics statistics, AuthAccountDao accountDao) {
        this.operationExecutor = operationExecutor;
        this.statistics = statistics;
        this.accountDao = accountDao;
    }

    void addAccountAndLinks(PortableAccount account) {
        operationExecutor.execute(() -> {
            // TODO: Handle statistics
            PortableAccountAdapter accountAdapter = new PortableAccountAdapter(account);
            accountDao.create(accountAdapter);
            accountDao.assignEmptyForeignCollection(accountAdapter, "links"); // TODO: Replace "links" with value from dao
            for (PortableAccountLink linkAccount : account.getLinkAccounts())
                accountAdapter.addAccountLink(new PortableAccountLinkAdapter(linkAccount, accountAdapter));
            return null;
        });
    }

}

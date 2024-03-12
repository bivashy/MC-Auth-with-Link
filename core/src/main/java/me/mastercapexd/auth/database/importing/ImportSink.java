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
        if (account == null) {
            statistics.invalidEntrySkipped();
            return;
        }
        operationExecutor.execute(() -> {
            PortableAccountAdapter accountAdapter = new PortableAccountAdapter(account);
            accountDao.create(accountAdapter);
            accountDao.assignEmptyForeignCollection(accountAdapter, "links");
            statistics.accountAdded();
            for (PortableAccountLink linkAccount : account.getLinkAccounts()) {
                accountAdapter.addAccountLink(new PortableAccountLinkAdapter(linkAccount, accountAdapter));
                statistics.linkAccountAdded();
            }
            return null;
        });
    }

}

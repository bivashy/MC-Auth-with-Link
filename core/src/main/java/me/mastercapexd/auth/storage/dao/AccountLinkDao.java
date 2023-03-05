package me.mastercapexd.auth.storage.dao;

import java.sql.SQLException;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import me.mastercapexd.auth.storage.DatabaseHelper;
import me.mastercapexd.auth.storage.model.AccountLink;

public class AccountLinkDao extends BaseDaoImpl<AccountLink, Long> {
    private static final SupplierExceptionCatcher DEFAULT_EXCEPTION_CATCHER = new SupplierExceptionCatcher();

    public AccountLinkDao(ConnectionSource connectionSource, DatabaseHelper databaseHelper) throws SQLException {
        super(connectionSource, AccountLink.class);
        TableUtils.createTableIfNotExists(connectionSource, AccountLink.class);
    }

    public QueryBuilder<AccountLink, Long> queryBuilder(LinkUserIdentificator linkUserIdentificator, String linkType) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().selectColumns(AccountLink.ACCOUNT_ID_FIELD_KEY)
                .where()
                .eq(AccountLink.LINK_USER_ID_FIELD_KEY, linkUserIdentificator.asString())
                .and()
                .eq(AccountLink.LINK_TYPE_FIELD_KEY, linkType)
                .queryBuilder());
    }

    public QueryBuilder<AccountLink, Long> queryBuilder(LinkUserIdentificator linkUserIdentificator) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().selectColumns(AccountLink.ACCOUNT_ID_FIELD_KEY)
                .where()
                .eq(AccountLink.LINK_USER_ID_FIELD_KEY, linkUserIdentificator.asString())
                .queryBuilder());
    }
}

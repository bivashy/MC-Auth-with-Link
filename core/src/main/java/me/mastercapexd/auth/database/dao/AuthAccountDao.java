package me.mastercapexd.auth.database.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import me.mastercapexd.auth.database.DatabaseHelper;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;
import me.mastercapexd.auth.database.model.AuthAccountProvider;

public class AuthAccountDao extends BaseDaoImpl<AuthAccount, Long> {
    private static final SupplierExceptionCatcher DEFAULT_EXCEPTION_CATCHER = new SupplierExceptionCatcher();
    private final DatabaseHelper databaseHelper;

    public AuthAccountDao(ConnectionSource connectionSource, DatabaseHelper databaseHelper) throws SQLException {
        super(connectionSource, AuthAccount.class);
        TableUtils.createTableIfNotExists(connectionSource, AuthAccount.class);
        this.databaseHelper = databaseHelper;
    }

    public Optional<AuthAccount> queryFirstAccountPlayerId(String playerId) {
        return Optional.ofNullable(
                DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().where().eq(AuthAccount.PLAYER_ID_FIELD_KEY, playerId).queryForFirst()));
    }

    public Optional<AuthAccount> queryFirstAccountPlayerName(String playerName) {
        return Optional.ofNullable(
                DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().where().eq(AuthAccount.PLAYER_NAME_FIELD_KEY, playerName).queryForFirst()));
    }

    public Collection<AuthAccount> queryAccounts(LinkUserIdentificator linkUserIdentificator, String linkType) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().where()
                .in(DatabaseHelper.ID_FIELD_KEY, databaseHelper.getAccountLinkDao().queryBuilder(linkUserIdentificator, linkType))
                .query(), Collections.emptyList());
    }

    public Collection<AuthAccount> queryAccounts(LinkUserIdentificator linkUserIdentificator) {
        return DEFAULT_EXCEPTION_CATCHER.execute(
                () -> queryBuilder().where().in(DatabaseHelper.ID_FIELD_KEY, databaseHelper.getAccountLinkDao().queryBuilder(linkUserIdentificator)).query(),
                Collections.emptyList());
    }

    public Collection<AuthAccount> queryAllAccounts() {
        return DEFAULT_EXCEPTION_CATCHER.execute(this::queryForAll, Collections.emptyList());
    }

    public Collection<AuthAccount> queryAllLinkedAccounts() {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().where()
                .in(AuthAccount.PLAYER_ID_FIELD_KEY, databaseHelper.getAccountLinkDao()
                        .queryBuilder()
                        .groupBy(AccountLink.ACCOUNT_ID_FIELD_KEY)
                        .having("COUNT(DISTINCT " + AccountLink.LINK_USER_ID_FIELD_KEY + ") = 0"))
                .query(), Collections.emptyList());
    }

    public Optional<AuthAccount> createOrUpdateAccount(Account account) {
        if (!(account instanceof AuthAccountProvider))
            throw new IllegalArgumentException("Cannot create or update not AuthAccountProvider: " + account.getClass().getName());
        AuthAccountProvider authAccountProvider = (AuthAccountProvider) account;
        AuthAccount authAccount = authAccountProvider.getAuthAccount();

        DEFAULT_EXCEPTION_CATCHER.execute(() -> createIfNotExists(authAccount));
        authAccountProvider.syncLinkAdaptersWithLinks().join();
        return createOrUpdateAuthAccount(authAccount);
    }

    public Optional<AuthAccount> createOrUpdateAuthAccount(AuthAccount authAccount) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> {
            update(authAccount);
            return Optional.of(authAccount);
        }, Optional.empty());
    }

    public Collection<AuthAccount> deleteAccountById(String id) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> deleteBuilder().where().eq(AuthAccount.PLAYER_ID_FIELD_KEY, id).query(), Collections.emptyList());
    }
}

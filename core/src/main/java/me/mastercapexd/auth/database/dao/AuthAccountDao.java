package me.mastercapexd.auth.database.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.database.schema.TableSettings;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import me.mastercapexd.auth.database.DatabaseHelper;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;
import me.mastercapexd.auth.database.model.AuthAccountProvider;
import me.mastercapexd.auth.database.persister.CryptoProviderPersister;

public class AuthAccountDao extends BaseDaoImpl<AuthAccount, Long> {
    private static final String PLAYER_ID_CONFIGURATION_KEY = "playerId";
    private static final String PLAYER_ID_TYPE_CONFIGURATION_KEY = "playerIdType";
    private static final String CRYPTO_PROVIDER_CONFIGURATION_KEY = "cryptoProvider";
    private static final String LAST_IP_CONFIGURATION_KEY = "lastIp";
    private static final String UNIQUE_ID_CONFIGURATION_KEY = "uniqueId";
    private static final String PLAYER_NAME_CONFIGURATION_KEY = "playerName";
    private static final String PASSWORD_HASH_CONFIGURATION_KEY = "passwordHash";
    private static final String LAST_QUIT_TIMESTAMP_CONFIGURATION_KEY = "lastQuitTimestamp";
    private static final String LAST_SESSION_TIMESTAMP_START_CONFIGURATION_KEY = "lastSessionStartTimestamp";
    private static final String LINKS_CONFIGURATION_KEY = "links";
    private static final SupplierExceptionCatcher DEFAULT_EXCEPTION_CATCHER = new SupplierExceptionCatcher();
    private final DatabaseHelper databaseHelper;

    public AuthAccountDao(ConnectionSource connectionSource, TableSettings settings, DatabaseHelper databaseHelper) throws SQLException {
        super(connectionSource, createTableConfig(settings));
        TableUtils.createTableIfNotExists(connectionSource, getTableConfig());
        this.databaseHelper = databaseHelper;
    }

    private static DatabaseTableConfig<AuthAccount> createTableConfig(TableSettings settings) {
        List<DatabaseFieldConfig> fields = new ArrayList<>();

        DatabaseFieldConfig idFieldConfig = new DatabaseFieldConfig("id");
        idFieldConfig.setGeneratedId(true);
        fields.add(idFieldConfig);

        DatabaseFieldConfig playerIdFieldConfig = createFieldConfig(settings, PLAYER_ID_CONFIGURATION_KEY, AuthAccount.PLAYER_ID_FIELD_KEY);
        playerIdFieldConfig.setUnique(true);
        playerIdFieldConfig.setCanBeNull(false);
        fields.add(playerIdFieldConfig);

        DatabaseFieldConfig playerIdTypeFieldConfig = createFieldConfig(settings, PLAYER_ID_TYPE_CONFIGURATION_KEY, AuthAccount.PLAYER_ID_TYPE_FIELD_KEY);
        playerIdTypeFieldConfig.setCanBeNull(false);
        playerIdTypeFieldConfig.setDataType(DataType.ENUM_NAME);
        fields.add(playerIdTypeFieldConfig);

        DatabaseFieldConfig hashTypeFieldConfig = createFieldConfig(settings, CRYPTO_PROVIDER_CONFIGURATION_KEY, AuthAccount.HASH_TYPE_FIELD_KEY);
        hashTypeFieldConfig.setCanBeNull(false);
        hashTypeFieldConfig.setDataPersister(CryptoProviderPersister.getSingleton());
        fields.add(hashTypeFieldConfig);

        fields.add(createFieldConfig(settings, LAST_IP_CONFIGURATION_KEY, AuthAccount.LAST_IP_FIELD_KEY));

        DatabaseFieldConfig uniqueIdFieldConfig = createFieldConfig(settings, UNIQUE_ID_CONFIGURATION_KEY, AuthAccount.UNIQUE_ID_FIELD_KEY);
        uniqueIdFieldConfig.setCanBeNull(false);
        uniqueIdFieldConfig.setDataType(DataType.UUID);
        fields.add(uniqueIdFieldConfig);

        DatabaseFieldConfig playerNameFieldConfig = createFieldConfig(settings, PLAYER_NAME_CONFIGURATION_KEY, AuthAccount.PLAYER_NAME_FIELD_KEY);
        playerNameFieldConfig.setCanBeNull(false);
        fields.add(playerNameFieldConfig);

        fields.add(createFieldConfig(settings, PASSWORD_HASH_CONFIGURATION_KEY, AuthAccount.PASSWORD_HASH_FIELD_KEY));

        DatabaseFieldConfig lastQuitTimestampFieldConfig = createFieldConfig(settings, LAST_QUIT_TIMESTAMP_CONFIGURATION_KEY,
                AuthAccount.LAST_QUIT_TIMESTAMP_FIELD_KEY);
        lastQuitTimestampFieldConfig.setDataType(DataType.LONG);
        fields.add(lastQuitTimestampFieldConfig);

        DatabaseFieldConfig lastSessionStartTimestampFieldConfig = createFieldConfig(settings, LAST_SESSION_TIMESTAMP_START_CONFIGURATION_KEY,
                AuthAccount.LAST_SESSION_TIMESTAMP_START_FIELD_KEY);
        lastSessionStartTimestampFieldConfig.setDataType(DataType.LONG);
        fields.add(lastSessionStartTimestampFieldConfig);

        DatabaseFieldConfig linksFieldConfig = new DatabaseFieldConfig(LINKS_CONFIGURATION_KEY);
        linksFieldConfig.setForeignCollection(true);
        fields.add(linksFieldConfig);
        return new DatabaseTableConfig<>(AuthAccount.class, "mc_auth_accounts", fields);
    }

    private static DatabaseFieldConfig createFieldConfig(TableSettings settings, String configurationKey, String defaultValue) {
        DatabaseFieldConfig config = new DatabaseFieldConfig(configurationKey);
        config.setColumnName(settings.getColumnName(configurationKey).orElse(defaultValue));
        return config;
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
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().join(databaseHelper.getAccountLinkDao()
                .queryBuilder()
                .selectColumns(AccountLink.ACCOUNT_ID_FIELD_KEY)
                .where()
                .isNotNull(AccountLink.LINK_USER_ID_FIELD_KEY)
                .and()
                .notIn(AccountLink.LINK_USER_ID_FIELD_KEY, AccountFactory.DEFAULT_TELEGRAM_ID, AccountFactory.DEFAULT_VK_ID)
                .queryBuilder()).distinct().query(), Collections.emptyList());
    }

    public Collection<AuthAccount> queryAllLinkedAccounts(LinkType linkType) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> queryBuilder().join(databaseHelper.getAccountLinkDao()
                .queryBuilder()
                .selectColumns(AccountLink.ACCOUNT_ID_FIELD_KEY)
                .where()
                .isNotNull(AccountLink.LINK_USER_ID_FIELD_KEY)
                .and()
                .notIn(AccountLink.LINK_USER_ID_FIELD_KEY, AccountFactory.DEFAULT_TELEGRAM_ID, AccountFactory.DEFAULT_VK_ID)
                .and().eq(AccountLink.LINK_TYPE_FIELD_KEY, linkType.getName())
                .queryBuilder()).distinct().query(), Collections.emptyList());
    }

    public Optional<AuthAccount> createOrUpdateAccount(Account account) {
        if (!(account instanceof AuthAccountProvider))
            throw new IllegalArgumentException("Cannot create or update not AuthAccountProvider: " + account.getClass().getName());
        AuthAccountProvider authAccountProvider = (AuthAccountProvider) account;
        AuthAccount authAccount = authAccountProvider.getAuthAccount();

        DEFAULT_EXCEPTION_CATCHER.execute(() -> createIfNotExists(authAccount));
        return updateAccount(authAccount);
    }

    public Optional<AuthAccount> updateAccount(AuthAccount authAccount) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> {
            update(authAccount);
            return Optional.of(authAccount);
        }, Optional.empty());
    }

    public Collection<Void> deleteAccountById(String id) {
        return DEFAULT_EXCEPTION_CATCHER.execute(() -> {
            DeleteBuilder<AuthAccount, Long> deleteBuilder = deleteBuilder();
            deleteBuilder.where().eq(AuthAccount.PLAYER_ID_FIELD_KEY, id);
            deleteBuilder.delete();
            return null;
        }, Collections.emptyList());
    }
}

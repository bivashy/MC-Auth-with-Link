package me.mastercapexd.auth.database.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bivashy.auth.api.config.database.schema.TableSettings;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import me.mastercapexd.auth.database.model.AccountLink;

public class AccountLinkDao extends BaseDaoImpl<AccountLink, Long> {
    private static final String LINK_TYPE_CONFIGURATION_KEY = "linkType";
    private static final String LINK_USER_ID_CONFIGURATION_KEY = "linkUserId";
    private static final String LINK_ENABLED_CONFIGURATION_KEY = "linkEnabled";
    private static final String ACCOUNT_ID_CONFIGURATION_KEY = "account";
    private static final SupplierExceptionCatcher DEFAULT_EXCEPTION_CATCHER = new SupplierExceptionCatcher();

    public AccountLinkDao(ConnectionSource connectionSource, TableSettings settings) throws SQLException {
        super(connectionSource, createTableConfig(settings));
        TableUtils.createTableIfNotExists(connectionSource, getTableConfig());
    }

    private static DatabaseTableConfig<AccountLink> createTableConfig(TableSettings settings) {
        List<DatabaseFieldConfig> fields = new ArrayList<>();

        DatabaseFieldConfig idFieldConfig = new DatabaseFieldConfig("id");
        idFieldConfig.setGeneratedId(true);
        fields.add(idFieldConfig);

        DatabaseFieldConfig linkTypeFieldConfig = createFieldConfig(settings, LINK_TYPE_CONFIGURATION_KEY, AccountLink.LINK_TYPE_FIELD_KEY);
        linkTypeFieldConfig.setCanBeNull(false);
        linkTypeFieldConfig.setUniqueCombo(true);
        fields.add(linkTypeFieldConfig);

        fields.add(createFieldConfig(settings, LINK_USER_ID_CONFIGURATION_KEY, AccountLink.LINK_USER_ID_FIELD_KEY));

        DatabaseFieldConfig linkEnabledFieldConfig = createFieldConfig(settings, LINK_ENABLED_CONFIGURATION_KEY, AccountLink.LINK_ENABLED_FIELD_KEY);
        linkEnabledFieldConfig.setDataType(DataType.BOOLEAN_INTEGER);
        linkEnabledFieldConfig.setCanBeNull(false);
        linkEnabledFieldConfig.setDefaultValue("true");
        fields.add(linkEnabledFieldConfig);

        DatabaseFieldConfig accountIdFieldConfig = createFieldConfig(settings, ACCOUNT_ID_CONFIGURATION_KEY, AccountLink.ACCOUNT_ID_FIELD_KEY);
        accountIdFieldConfig.setForeign(true);
        accountIdFieldConfig.setUniqueCombo(true);
        fields.add(accountIdFieldConfig);
        return new DatabaseTableConfig<>(AccountLink.class, settings.getTableName(), fields);
    }

    private static DatabaseFieldConfig createFieldConfig(TableSettings settings, String configurationKey, String defaultValue) {
        DatabaseFieldConfig config = new DatabaseFieldConfig(configurationKey);
        config.setColumnName(settings.getColumnName(configurationKey).orElse(defaultValue));
        return config;
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

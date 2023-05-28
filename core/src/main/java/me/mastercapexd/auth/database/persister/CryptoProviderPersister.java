package me.mastercapexd.auth.database.persister;

import java.sql.SQLException;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

public class CryptoProviderPersister extends BaseDataType {
    private static final CryptoProviderPersister SINGLETON = new CryptoProviderPersister();
    public static CryptoProviderPersister getSingleton() {
        return SINGLETON;
    }
    public CryptoProviderPersister() {
        super(SqlType.STRING, new Class<?>[]{CryptoProvider.class});
    }

    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (fieldType == null) {
            return sqlArg;
        } else {
            String value = (String) sqlArg;
            return AuthPlugin.instance()
                    .getCryptoProviderBucket()
                    .findCryptoProvider(value)
                    .orElseThrow(() -> new SQLException("Cannot get crypto provider value of '" + value + "' for field " + fieldType));
        }
    }

    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }
}

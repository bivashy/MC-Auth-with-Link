package me.mastercapexd.auth.database.importing;

import com.j256.ormlite.stmt.QueryBuilder;

public interface ImportSource {

    QueryBuilder<PortableAccount, ?> sourceAccounts();

}

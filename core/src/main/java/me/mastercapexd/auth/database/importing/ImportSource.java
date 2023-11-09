package me.mastercapexd.auth.database.importing;

import java.util.stream.Stream;

import me.mastercapexd.auth.database.importing.model.PortableAccount;

public interface ImportSource {

    Stream<PortableAccount> sourceAccounts();

}

package me.mastercapexd.auth.database.importing.source;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.j256.ormlite.dao.RawRowMapper;

import me.mastercapexd.auth.crypto.BcryptCryptoProvider;
import me.mastercapexd.auth.crypto.authme.AuthMeSha256CryptoProvider;
import me.mastercapexd.auth.database.importing.DatabaseStream;
import me.mastercapexd.auth.database.importing.ImportSource;
import me.mastercapexd.auth.database.importing.exception.ImportingException;
import me.mastercapexd.auth.database.importing.model.PortableAccount;
import me.mastercapexd.auth.database.importing.model.PortableAccount.AccountDetails;

public class AuthMeImportSource implements ImportSource {

    private static final String QUERY_ALL_ACCOUNTS = "SELECT * FROM %s";
    private final ImportingSourceSettings settings;

    public AuthMeImportSource(ImportingSourceSettings settings) {
        this.settings = settings;
    }

    @Override
    public Stream<PortableAccount> sourceAccounts() {
        try (DatabaseStream databaseStream = new DatabaseStream(settings)) {
            String tableName = settings.getProperty("table-name").orElse("authme");
            return databaseStream.execute(String.format(QUERY_ALL_ACCOUNTS, tableName), new AccountRowMapper());
        } catch (Exception e) {
            throw new ImportingException("Exception occurred during AuthMe accounts resolution", e);
        }
    }

    private static class AccountRowMapper implements RawRowMapper<PortableAccount> {

        private static UUID getCrackedUUIDFromName(String name) {
            if (name == null)
                return null;
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public PortableAccount mapRow(String[] columnNames, String[] resultColumns) {
            return singleAccount(Arrays.asList(columnNames), resultColumns);
        }

        private PortableAccount singleAccount(List<String> columns, String[] resultColumns) {
            String name = requireColumn(columns, resultColumns, "realname");
            UUID uniqueId = getCrackedUUIDFromName(name);
            String passwordHash = requireColumn(columns, resultColumns, "password");

            CryptoProvider cryptoProvider = null;
            if (passwordHash.startsWith("$SHA$"))
                cryptoProvider = new AuthMeSha256CryptoProvider();
            else if (passwordHash.startsWith("$2a$")) {
                cryptoProvider = new BcryptCryptoProvider();
            } else {
                throw new ImportingException("Cannot migrate AuthMe '" + name + "' account, due to unknown hashing algorithm");
            }

            return new PortableAccount(name, uniqueId, cryptoProvider, passwordHash, new ArrayList<>(),
                    new AccountDetails(0, null, 0));
        }

        private String requireColumn(List<String> columnNameList, String[] resultColumns, String columnName) {
            return column(columnNameList, resultColumns, columnName).orElseThrow(
                    () -> new ImportingException("LoginSecurity '" + columnName + "' column was not found!"));
        }

        private Optional<String> column(List<String> columnNameList, String[] resultColumns, String columnName) {
            int index = columnNameList.indexOf(columnName);
            if (index == -1)
                return Optional.empty();
            return Optional.ofNullable(resultColumns[index]);
        }

    }

}

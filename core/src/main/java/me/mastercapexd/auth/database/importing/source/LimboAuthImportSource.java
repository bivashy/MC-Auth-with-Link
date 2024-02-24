package me.mastercapexd.auth.database.importing.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.j256.ormlite.dao.RawRowMapper;

import me.mastercapexd.auth.crypto.BcryptCryptoProvider;
import me.mastercapexd.auth.database.importing.DatabaseStream;
import me.mastercapexd.auth.database.importing.ImportSource;
import me.mastercapexd.auth.database.importing.exception.ImportingException;
import me.mastercapexd.auth.database.importing.model.PortableAccount;
import me.mastercapexd.auth.database.importing.model.PortableAccount.AccountDetails;
import me.mastercapexd.auth.database.importing.model.PortableAccountLink;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class LimboAuthImportSource implements ImportSource {

    private static final String QUERY_ALL_ACCOUNTS = "SELECT * FROM AUTH";
    private static final String QUERY_ALL_ACCOUNTS_WITH_LINKS = "SELECT * FROM AUTH LEFT JOIN SOCIAL ON AUTH.LOWERCASENICKNAME=SOCIAL.LOWERCASENICKNAME";
    private final ImportingSourceSettings settings;

    public LimboAuthImportSource(ImportingSourceSettings settings) {
        this.settings = settings;
    }

    @Override
    public Stream<PortableAccount> sourceAccounts() {
        try (DatabaseStream databaseStream = new DatabaseStream(settings)) {
            if (databaseStream.tableExists("SOCIAL"))
                return databaseStream.execute(QUERY_ALL_ACCOUNTS_WITH_LINKS, new AccountRowMapper());
            return databaseStream.execute(QUERY_ALL_ACCOUNTS, new AccountRowMapper());
        } catch (Exception e) {
            throw new ImportingException("Exception occurred during LimboAuth accounts resolution", e);
        }
    }

    private static class AccountRowMapper implements RawRowMapper<PortableAccount> {

        @Override
        public PortableAccount mapRow(String[] columnNames, String[] resultColumns) {
            List<String> columnNameList = Arrays.asList(columnNames);
            if (columnNameList.contains("SOCIAL.LOWERCASENICKNAME"))
                return singleAccountWithLink(columnNameList, resultColumns);
            return singleAccount(columnNameList, resultColumns);
        }

        private PortableAccount singleAccount(List<String> columns, String[] resultColumns) {
            Optional<String> uniqueIdOptional = column(columns, resultColumns, "UUID");
            if (!uniqueIdOptional.isPresent())
                return null; // TODO: Log about invalid entry
            String rawUniqueId = uniqueIdOptional.get();
            if (rawUniqueId.isEmpty())
                return null; // TODO: Log about invalid entry
            String name = requireColumn(columns, resultColumns, "NICKNAME");
            UUID uniqueId = UUID.fromString(rawUniqueId);
            String passwordHash = requireColumn(columns, resultColumns, "HASH");
            String lastIp = column(columns, resultColumns, "LOGINIP").orElse(null);

            PortableAccount account = new PortableAccount(name, uniqueId, new BcryptCryptoProvider(), passwordHash, new ArrayList<>(),
                    new AccountDetails(0, lastIp, 0));

            String totpToken = column(columns, resultColumns, "TOTPTOKEN").orElse(null);
            if (totpToken != null) {
                PortableAccountLink portableAccountLink = new PortableAccountLink(GoogleLinkType.getInstance(), totpToken, account);
                account.addLinkAccount(portableAccountLink);
            }
            return account;
        }

        private PortableAccount singleAccountWithLink(List<String> columns, String[] resultColumns) {
            PortableAccount account = singleAccount(columns, resultColumns);
            if (account == null)
                return null; // TODO: Log about invalid entry
            column(columns, resultColumns, "VK_ID").ifPresent(
                    vkId -> account.addLinkAccount(new PortableAccountLink(VKLinkType.getInstance(), vkId, account)));
            column(columns, resultColumns, "TELEGRAM_ID").ifPresent(
                    telegramId -> account.addLinkAccount(new PortableAccountLink(TelegramLinkType.getInstance(), telegramId, account)));
            column(columns, resultColumns, "DISCORD_ID").ifPresent(
                    discordId -> account.addLinkAccount(new PortableAccountLink(DiscordLinkType.getInstance(), discordId, account)));
            return account;
        }

        private String requireColumn(List<String> columnNameList, String[] resultColumns, String columnName) {
            return column(columnNameList, resultColumns, columnName).orElseThrow(
                    () -> new ImportingException("LimboAuth '" + columnName + "' column was not found!"));
        }

        private Optional<String> column(List<String> columnNameList, String[] resultColumns, String columnName) {
            int index = columnNameList.indexOf(columnName);
            if (index == -1)
                return Optional.empty();
            return Optional.ofNullable(resultColumns[index]);
        }

    }

}

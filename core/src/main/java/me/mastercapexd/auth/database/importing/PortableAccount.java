package me.mastercapexd.auth.database.importing;

import java.util.Collection;
import java.util.UUID;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.database.importing.adapter.PortableAccountAdapter;

public class PortableAccount {

    private final String name;
    private final UUID uniqueId;
    private final CryptoProvider cryptoProvider;
    private final String hashedPassword;
    private final Collection<PortableLinkAccount> linkAccounts;
    private final AccountDetails details;

    public PortableAccount(String name, UUID uniqueId, CryptoProvider cryptoProvider, String hashedPassword, Collection<PortableLinkAccount> linkAccounts,
                           AccountDetails details) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.cryptoProvider = cryptoProvider;
        this.hashedPassword = hashedPassword;
        this.linkAccounts = linkAccounts;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public CryptoProvider getCryptoProvider() {
        return cryptoProvider;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Collection<PortableLinkAccount> getLinkAccounts() {
        return linkAccounts;
    }

    public AccountDetails getDetails() {
        return details;
    }

    public static final class AccountDetails {

        private final long lastQuitTimestamp;
        private final String lastIpAddress;
        private final long lastSessionStartTimestamp;

        public AccountDetails(long lastQuitTimestamp, String lastIpAddress, long lastSessionStartTimestamp) {
            this.lastQuitTimestamp = lastQuitTimestamp;
            this.lastIpAddress = lastIpAddress;
            this.lastSessionStartTimestamp = lastSessionStartTimestamp;
        }

        public long getLastQuitTimestamp() {
            return lastQuitTimestamp;
        }

        public String getLastIpAddress() {
            return lastIpAddress;
        }

        public long getLastSessionStartTimestamp() {
            return lastSessionStartTimestamp;
        }

    }
    public static final class PortableLinkAccount implements LinkUser {

        private final LinkType linkType;
        private final LinkUserInfo linkUserInfo;
        private final Account account;

        public PortableLinkAccount(LinkType linkType, String identificator, PortableAccount account) {
            this.linkType = linkType;
            this.linkUserInfo = LinkUserInfo.of(LinkUserIdentificator.ofParsed(identificator));
            this.account = new PortableAccountAdapter(account);
        }

        @Override
        public LinkType getLinkType() {
            return linkType;
        }

        @Override
        public Account getAccount() {
            return account;
        }

        @Override
        public LinkUserInfo getLinkUserInfo() {
            return linkUserInfo;
        }

    }

}

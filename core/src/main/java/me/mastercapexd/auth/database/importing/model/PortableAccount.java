package me.mastercapexd.auth.database.importing.model;

import java.util.Collection;
import java.util.UUID;

import com.bivashy.auth.api.crypto.CryptoProvider;

public class PortableAccount {

    private final String name;
    private final UUID uniqueId;
    private final CryptoProvider cryptoProvider;
    private final String hashedPassword;
    private final Collection<PortableAccountLink> linkAccounts;
    private final AccountDetails details;

    public PortableAccount(String name, UUID uniqueId, CryptoProvider cryptoProvider, String hashedPassword, Collection<PortableAccountLink> linkAccounts,
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

    public Collection<PortableAccountLink> getLinkAccounts() {
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

}

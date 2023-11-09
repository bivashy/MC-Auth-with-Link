package me.mastercapexd.auth.database.importing.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.account.AccountTemplate;
import me.mastercapexd.auth.database.importing.PortableAccount;

public class PortableAccountAdapter extends AccountTemplate {

    private final List<LinkUser> linkUsers;
    private final PortableAccount portableAccount;
    private IdentifierType identifierType;
    private CryptoProvider cryptoProvider;
    private HashedPassword hashedPassword;
    private long lastQuitTimestamp, lastSessionStartTimestamp;
    private String lastIpAddress;

    public PortableAccountAdapter(PortableAccount portableAccount) {
        this.portableAccount = portableAccount;
        this.linkUsers = new ArrayList<>(portableAccount.getLinkAccounts());
        this.identifierType = AuthPlugin.instance().getConfig().getActiveIdentifierType();
        this.hashedPassword = HashedPassword.of(portableAccount.getHashedPassword(), cryptoProvider);
        this.cryptoProvider = portableAccount.getCryptoProvider();
        this.lastQuitTimestamp = portableAccount.getDetails().getLastQuitTimestamp();
        this.lastSessionStartTimestamp = portableAccount.getDetails().getLastSessionStartTimestamp();
        this.lastIpAddress = portableAccount.getDetails().getLastIpAddress();
    }

    @Override
    public long getDatabaseId() {
        return 0;
    }

    @Override
    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    @Override
    public CryptoProvider getCryptoProvider() {
        return cryptoProvider;
    }

    @Override
    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public UUID getUniqueId() {
        return portableAccount.getUniqueId();
    }

    @Override
    public String getName() {
        return portableAccount.getName();
    }

    @Override
    public HashedPassword getPasswordHash() {
        return hashedPassword;
    }

    @Override
    public void setPasswordHash(HashedPassword passwordHash) {
        this.hashedPassword = passwordHash;
    }

    @Override
    public List<LinkUser> getLinkUsers() {
        return Collections.unmodifiableList(linkUsers);
    }

    @Override
    public void addLinkUser(LinkUser linkUser) {
        if (linkUser == null)
            return;
        if (linkUsers.stream().anyMatch(listLinkUser -> listLinkUser.getLinkType().equals(linkUser.getLinkType())))
            return;
        linkUsers.add(linkUser);
    }

    @Override
    public Optional<LinkUser> findFirstLinkUser(Predicate<LinkUser> filter) {
        return linkUsers.stream().filter(filter).findFirst();
    }

    @Override
    public long getLastQuitTimestamp() {
        return lastQuitTimestamp;
    }

    @Override
    public void setLastQuitTimestamp(long timestamp) {
        this.lastQuitTimestamp = timestamp;
    }

    @Override
    public String getLastIpAddress() {
        return lastIpAddress;
    }

    @Override
    public void setLastIpAddress(String hostString) {
        this.lastIpAddress = hostString;
    }

    @Override
    public long getLastSessionStartTimestamp() {
        return lastSessionStartTimestamp;
    }

    @Override
    public void setLastSessionStartTimestamp(long timestamp) {
        this.lastSessionStartTimestamp = timestamp;
    }

    @Override
    public String getPlayerId() {
        switch (identifierType) {
            case NAME:
                return getName().toLowerCase();
            case UUID:
                return getUniqueId().toString();
        }
        throw new IllegalStateException("Unsupported identifier type " + identifierType);
    }

    @Override
    public int compareTo(@NotNull AccountTemplate accountTemplate) {
        return 0;
    }

}

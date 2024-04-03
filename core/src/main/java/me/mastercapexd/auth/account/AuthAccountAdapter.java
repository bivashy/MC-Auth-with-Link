package me.mastercapexd.auth.account;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;
import me.mastercapexd.auth.link.user.AccountLinkAdapter;

public class AuthAccountAdapter extends AccountTemplate {

    private final List<LinkUser> linkUsers;
    private final AuthAccount authAccount;

    /**
     * Create {@link com.bivashy.auth.api.account.Account} from {@link AuthAccount}
     *
     * @param authAccount  - account that need to adapt
     * @param accountLinks - linked social {@link AccountLink} collection
     */
    public AuthAccountAdapter(AuthAccount authAccount, Collection<AccountLink> accountLinks) {
        this.authAccount = authAccount;
        this.linkUsers = accountLinks.stream().map(accountLink -> new AccountLinkAdapter(accountLink, this)).collect(Collectors.toList());
    }

    /**
     * Create {@link com.bivashy.auth.api.account.Account} from {@link AuthAccount}
     * <p>
     * This constructor will query {@link AccountLink} from database, because of
     * {@link com.j256.ormlite.dao.ForeignCollection}
     *
     * @param authAccount - account that need to adapt
     * @see com.j256.ormlite.dao.ForeignCollection
     */
    public AuthAccountAdapter(AuthAccount authAccount) {
        this(authAccount, authAccount.getLinks());
    }

    @Override
    public long getDatabaseId() {
        return authAccount.getId();
    }

    @Override
    public String getPlayerId() {
        return authAccount.getPlayerId();
    }

    @Override
    public IdentifierType getIdentifierType() {
        return authAccount.getPlayerIdType();
    }

    @Override
    public CryptoProvider getCryptoProvider() {
        return authAccount.getHashType();
    }

    @Override
    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        authAccount.setHashType(cryptoProvider);
    }

    @Override
    public UUID getUniqueId() {
        return authAccount.getUniqueId();
    }

    @Override
    public String getName() {
        return authAccount.getPlayerName();
    }

    @Override
    public HashedPassword getPasswordHash() {
        return HashedPassword.of(authAccount.getPasswordHash(), null, authAccount.getHashType());
    }

    @Override
    public void setPasswordHash(HashedPassword hashedPassword) {
        authAccount.setPasswordHash(hashedPassword.getHash());
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
        return authAccount.getLastQuitTimestamp();
    }

    @Override
    public void setLastQuitTimestamp(long timestamp) {
        authAccount.setLastQuitTimestamp(timestamp);
    }

    @Override
    public String getLastIpAddress() {
        return authAccount.getLastIp();
    }

    @Override
    public void setLastIpAddress(String hostString) {
        authAccount.setLastIp(hostString);
    }

    @Override
    public void setIsPremium(boolean newIsPremium) {
        authAccount.setIsPremium(newIsPremium);
    }

    @Override
    public long getLastSessionStartTimestamp() {
        return authAccount.getLastSessionStartTimestamp();
    }

    @Override
    public void setLastSessionStartTimestamp(long currentTimeMillis) {
        authAccount.setLastSessionStartTimestamp(currentTimeMillis);
    }

    @Override
    public boolean isPremium() {
        return authAccount.isPremium();
    }

    @Override
    public int compareTo(AccountTemplate accountTemplate) {
        return accountTemplate.getName().compareTo(getName());
    }

}

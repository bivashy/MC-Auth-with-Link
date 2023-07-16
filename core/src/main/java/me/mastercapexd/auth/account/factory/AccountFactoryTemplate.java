package me.mastercapexd.auth.account.factory;

import java.util.UUID;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

public abstract class AccountFactoryTemplate implements AccountFactory {
    @Override
    public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, CryptoProvider cryptoProvider, String passwordHash,
                                 String lastIp) {
        Account account = newAccount(identifierType.fromRawString(id), identifierType, uuid, name);

        account.setCryptoProvider(cryptoProvider);
        account.setPasswordHash(HashedPassword.of(passwordHash, cryptoProvider));
        account.setLastIpAddress(lastIp);

        for (LinkType linkType : AuthPlugin.instance().getLinkTypeProvider().getLinkTypes())
            account.addLinkUser(createUser(linkType, account));
        return account;
    }

    private LinkUser createUser(LinkType linkType, Account account) {
        return LinkUserTemplate.of(linkType, account, LinkUserInfo.of(linkType.getDefaultIdentificator()));
    }

    protected abstract Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name);
}
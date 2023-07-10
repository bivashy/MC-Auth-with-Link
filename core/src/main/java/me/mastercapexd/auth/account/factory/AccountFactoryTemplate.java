package me.mastercapexd.auth.account.factory;

import java.util.UUID;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.vk.VKLinkType;

public abstract class AccountFactoryTemplate implements AccountFactory {
    @Override
    public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, CryptoProvider cryptoProvider, String passwordHash,
                                 String lastIp) {
        Account account = newAccount(identifierType.fromRawString(id), identifierType, uuid, name);

        account.setCryptoProvider(cryptoProvider);
        account.setPasswordHash(HashedPassword.of(passwordHash, cryptoProvider));
        account.setLastIpAddress(lastIp);

        account.addLinkUser(createUser(VKLinkType.getInstance(), account));
        account.addLinkUser(createUser(TelegramLinkType.getInstance(), account));
        account.addLinkUser(createUser(GoogleLinkType.getInstance(), account));
        return account;
    }

    private LinkUser createUser(LinkType linkType, Account account) {
        return LinkUserTemplate.of(linkType, account, LinkUserInfo.of(linkType.getDefaultIdentificator()));
    }

    protected abstract Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name);
}
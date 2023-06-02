package me.mastercapexd.auth.account.factory;

import java.util.UUID;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkUser;
import me.mastercapexd.auth.link.vk.VKLinkUser;

public abstract class AccountFactoryTemplate implements AccountFactory {
    @Override
    public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, CryptoProvider cryptoProvider, String passwordHash, String googleKey,
                                 int vkId, boolean vkConfirmationEnabled, long telegramId, boolean telegramConfirmationEnabled, long lastQuit, String lastIp, long lastSessionStart, long sessionTime) {

        Account account = newAccount(identifierType.fromRawString(id), identifierType, uuid, name);

        account.setCryptoProvider(cryptoProvider);
        account.setPasswordHash(HashedPassword.of(passwordHash, cryptoProvider));
        account.setLastQuitTimestamp(lastQuit);
        account.setLastIpAddress(lastIp);
        account.setLastSessionStartTimestamp(lastSessionStart);

        account.addLinkUser(new VKLinkUser(account, vkId, vkConfirmationEnabled));
        account.addLinkUser(new TelegramLinkUser(account, telegramId, telegramConfirmationEnabled));
        account.addLinkUser(new GoogleLinkUser(account, googleKey));
        return account;
    }

    protected abstract Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name);
}
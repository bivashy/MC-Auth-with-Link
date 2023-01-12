package me.mastercapexd.auth.account.factories;

import java.util.UUID;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkUser;
import me.mastercapexd.auth.link.vk.VKLinkUser;

public abstract class AccountFactoryTemplate implements AccountFactory {

    @Override
    public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType, String password, String googleKey,
                                 Integer vkId, boolean vkConfirmationEnabled, Long telegramId, boolean telegramConfirmationEnabled, long lastQuit,
                                 String lastIp, long lastSessionStart, long sessionTime) {

        Account account = newAccount(identifierType.fromRawString(id), identifierType, uuid, name);

        account.setHashType(hashType);
        account.setPasswordHash(password);
        account.setLastQuitTime(lastQuit);
        account.setLastIpAddress(lastIp);
        account.setLastSessionStart(lastSessionStart);

        account.addLinkUser(new VKLinkUser(account, vkId, vkConfirmationEnabled));
        account.addLinkUser(new TelegramLinkUser(account, telegramId, telegramConfirmationEnabled));
        account.addLinkUser(new GoogleLinkUser(account, googleKey));
        return account;
    }

    protected abstract Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name);
}
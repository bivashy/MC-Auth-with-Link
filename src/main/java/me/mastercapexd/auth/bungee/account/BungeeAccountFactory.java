package me.mastercapexd.auth.bungee.account;

import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AbstractAccountFactory;

import java.util.UUID;

public class BungeeAccountFactory extends AbstractAccountFactory {

    @Override
    protected Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        return new BungeeAccount(id, identifierType, uniqueId, name);
    }

}

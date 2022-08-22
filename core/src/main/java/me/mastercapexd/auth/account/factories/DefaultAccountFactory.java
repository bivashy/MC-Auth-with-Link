package me.mastercapexd.auth.account.factories;

import java.util.UUID;

import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.DefaultAccount;

public class DefaultAccountFactory extends AbstractAccountFactory {
    @Override
    protected Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        return new DefaultAccount(id, identifierType, uniqueId, name);
    }
}

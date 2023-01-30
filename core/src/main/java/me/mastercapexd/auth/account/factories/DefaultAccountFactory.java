package me.mastercapexd.auth.account.factories;

import java.util.UUID;

import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.DefaultAccount;

@Deprecated
public class DefaultAccountFactory extends AccountFactoryTemplate {
    @Override
    protected Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        return new DefaultAccount(id, identifierType, uniqueId, name);
    }
}

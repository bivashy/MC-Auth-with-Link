package me.mastercapexd.auth.account;

import java.util.UUID;

import me.mastercapexd.auth.IdentifierType;

public class DefaultAccount extends AbstractAccount {
    public DefaultAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        super(id, identifierType, uniqueId, name);
    }
}

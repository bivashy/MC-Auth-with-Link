package me.mastercapexd.auth.bungee.account;

import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.AbstractAccount;

import java.util.UUID;

public class BungeeAccount extends AbstractAccount {
    public BungeeAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        super(id, identifierType, uniqueId, name);
    }

}
package me.mastercapexd.auth.account.factories;

import java.util.ArrayList;
import java.util.UUID;

import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.AuthAccountAdapter;
import me.mastercapexd.auth.storage.model.AuthAccount;

public class AuthAccountFactory extends AccountFactoryTemplate {
    @Override
    protected Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        return new AuthAccountAdapter(new AuthAccount(id, identifierType, name, uniqueId), new ArrayList<>());
    }
}

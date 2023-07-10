package me.mastercapexd.auth.account.factory;

import java.util.ArrayList;
import java.util.UUID;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.account.AuthAccountAdapter;
import me.mastercapexd.auth.database.model.AuthAccount;

public class AuthAccountFactory extends AccountFactoryTemplate {
    @Override
    protected Account newAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        AuthAccount authAccount = new AuthAccount(id, identifierType, name, uniqueId);
        return new AuthAccountAdapter(authAccount, new ArrayList<>());
    }
}

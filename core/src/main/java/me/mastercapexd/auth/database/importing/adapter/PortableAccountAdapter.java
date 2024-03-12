package me.mastercapexd.auth.database.importing.adapter;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.database.importing.model.PortableAccount;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;

public class PortableAccountAdapter extends AuthAccount {

    public PortableAccountAdapter(PortableAccount portableAccount) {
        super(playerId(portableAccount), identifierType(), portableAccount.getCryptoProvider(), portableAccount.getDetails().getLastIpAddress(),
                portableAccount.getUniqueId(), portableAccount.getName(), portableAccount.getHashedPassword(),
                portableAccount.getDetails().getLastQuitTimestamp(), portableAccount.getDetails().getLastSessionStartTimestamp());
    }

    public void addAccountLink(AccountLink accountLink) {
        getLinks().add(accountLink);
    }

    private static IdentifierType identifierType() {
        return AuthPlugin.instance().getConfig().getActiveIdentifierType();
    }

    private static String playerId(PortableAccount account) {
        IdentifierType identifierType = identifierType();
        switch (identifierType) {
            case NAME:
                return account.getName().toLowerCase();
            case UUID:
                return account.getUniqueId().toString();
        }
        throw new IllegalStateException("Unsupported identifier type " + identifierType);
    }

}

package me.mastercapexd.auth.database.importing.adapter;

import me.mastercapexd.auth.database.importing.model.PortableAccountLink;
import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;

public class PortableAccountLinkAdapter extends AccountLink {

    public PortableAccountLinkAdapter(PortableAccountLink accountLink, AuthAccount account) {
        super(accountLink.getLinkType().getName(), accountLink.getIdentificator(), true, account);
    }

}

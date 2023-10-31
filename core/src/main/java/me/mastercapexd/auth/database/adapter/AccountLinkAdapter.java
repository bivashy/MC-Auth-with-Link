package me.mastercapexd.auth.database.adapter;

import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.database.model.AccountLink;

public class AccountLinkAdapter extends AccountLink {

    public AccountLinkAdapter(LinkUser linkUser) {
        super(linkUser.getLinkType().getName(), linkUser.getLinkUserInfo().getIdentificator().asString(), linkUser.getLinkUserInfo().isConfirmationEnabled(),
                null);
    }

}

package me.mastercapexd.auth.database.adapter;

import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.database.model.AccountLink;
import me.mastercapexd.auth.database.model.AuthAccount;

public class AccountLinkAdapter extends AccountLink {

    public AccountLinkAdapter(LinkUser linkUser, AuthAccount account) {
        super(linkUser.getLinkType().getName(), linkUser.getLinkUserInfo().getIdentificator().asString(), linkUser.getLinkUserInfo().isConfirmationEnabled(),
                account);
    }

}

package me.mastercapexd.auth.link.user;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.database.model.AccountLink;

public class AccountLinkAdapter extends LinkUserTemplate {

    public AccountLinkAdapter(AccountLink accountLink, Account account) {
        super(findLinkType(accountLink), account, LinkUserInfo.of(LinkUserIdentificator.ofParsed(accountLink.getLinkUserId()), accountLink.isLinkEnabled()));
    }

    private static LinkType findLinkType(AccountLink accountLink) {
        return AuthPlugin.instance()
                .getLinkTypeProvider()
                .getLinkType(accountLink.getLinkType())
                .orElseThrow(() -> new IllegalArgumentException("Link type " + accountLink.getLinkType() + " not exists!"));
    }

}

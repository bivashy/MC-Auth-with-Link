package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.entry.LinkEntryUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class VKLinkEntryUser extends LinkEntryUserTemplate {

    public VKLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(VKLinkType.getInstance(), account, linkUserInfo);
    }

    public VKLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
    }

}

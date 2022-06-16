package me.mastercapexd.auth.link.entryuser.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.AbstractLinkEntryUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class VKLinkEntryUser extends AbstractLinkEntryUser {

    public VKLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(VKLinkType.getInstance(), account, linkUserInfo);
    }

    public VKLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
    }

}

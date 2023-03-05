package me.mastercapexd.auth.link.vk;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.entry.BaseLinkEntryUser;

public class VKLinkEntryUser extends BaseLinkEntryUser {
    public VKLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(VKLinkType.getInstance(), account, linkUserInfo);
    }

    public VKLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUserOrNew(VKLinkType.LINK_USER_FILTER, VKLinkType.getInstance()).getLinkUserInfo());
    }
}

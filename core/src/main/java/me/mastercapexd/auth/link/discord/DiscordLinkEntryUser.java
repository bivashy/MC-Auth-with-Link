package me.mastercapexd.auth.link.discord;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.entry.BaseLinkEntryUser;

public class DiscordLinkEntryUser extends BaseLinkEntryUser {
    public DiscordLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(DiscordLinkType.getInstance(), account, linkUserInfo);
    }

    public DiscordLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUserOrNew(DiscordLinkType.LINK_USER_FILTER, DiscordLinkType.getInstance()).getLinkUserInfo());
    }
}

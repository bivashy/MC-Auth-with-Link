package me.mastercapexd.auth.link.discord;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

public class DiscordLinkUser extends LinkUserTemplate {
    private DiscordLinkUserInfo linkInfoAccount;

    public DiscordLinkUser(Account account, Long discordId, boolean confirmationEnabled) {
        super(DiscordLinkType.getInstance(), account);
        this.linkInfoAccount = new DiscordLinkUserInfo(discordId, confirmationEnabled);
    }

    public DiscordLinkUser(Account account, Long discordId) {
        super(DiscordLinkType.getInstance(), account);
        this.linkInfoAccount = new DiscordLinkUserInfo(discordId);
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkInfoAccount;
    }
}


package me.mastercapexd.auth.link.discord;

import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;

import me.mastercapexd.auth.link.user.BaseLinkUserInfo;

public class DiscordLinkUserInfo extends BaseLinkUserInfo {
    public DiscordLinkUserInfo(Long linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), confirmationEnabled);
    }

    public DiscordLinkUserInfo(Long linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }
}

package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class VKLinkUser extends LinkUserTemplate {
    private VKLinkUserInfo linkInfoAccount;

    public VKLinkUser(Account account, Integer vkId, boolean confirmationEnabled) {
        super(VKLinkType.getInstance(), account);
        this.linkInfoAccount = new VKLinkUserInfo(vkId, confirmationEnabled);
    }

    public VKLinkUser(Account account, Integer vkId) {
        super(VKLinkType.getInstance(), account);
        this.linkInfoAccount = new VKLinkUserInfo(vkId);
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkInfoAccount;
    }

}

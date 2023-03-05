package me.mastercapexd.auth.link.vk;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

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

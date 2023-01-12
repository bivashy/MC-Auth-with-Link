package me.mastercapexd.auth.link.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class TelegramLinkUser extends LinkUserTemplate {
    private TelegramLinkUserInfo linkInfoAccount;

    public TelegramLinkUser(Account account, Long telegramId, boolean confirmationEnabled) {
        super(TelegramLinkType.getInstance(), account);
        this.linkInfoAccount = new TelegramLinkUserInfo(telegramId, confirmationEnabled);
    }

    public TelegramLinkUser(Account account, Long telegramId) {
        super(TelegramLinkType.getInstance(), account);
        this.linkInfoAccount = new TelegramLinkUserInfo(telegramId);
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkInfoAccount;
    }
}

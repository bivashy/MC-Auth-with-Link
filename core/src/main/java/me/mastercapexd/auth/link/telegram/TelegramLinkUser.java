package me.mastercapexd.auth.link.telegram;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

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

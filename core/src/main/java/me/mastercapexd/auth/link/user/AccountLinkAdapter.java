package me.mastercapexd.auth.link.user;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;

import me.mastercapexd.auth.database.model.AccountLink;

public class AccountLinkAdapter extends LinkUserTemplate {
    private final LinkUserInfo linkUserInfo;
    private AccountLink accountLink;

    public AccountLinkAdapter(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        super(linkType, account);
        this.linkUserInfo = linkUserInfo;
    }

    public AccountLinkAdapter(LinkUser linkUser) {
        this(linkUser.getLinkType(), linkUser.getAccount(), linkUser.getLinkUserInfo());
    }

    public AccountLinkAdapter(AccountLink accountLink, Account account) {
        this(AuthPlugin.instance()
                        .getLinkTypeProvider()
                        .getLinkType(accountLink.getLinkType())
                        .orElseThrow(() -> new IllegalArgumentException("Link type " + accountLink.getLinkType() + " not exists!")), account,
                new AccountLinkUserInfo(accountLink.getLinkUserId(), accountLink.isLinkEnabled()));
        this.accountLink = accountLink;
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkUserInfo;
    }

    public Optional<AccountLink> getAccountLink() {
        return Optional.ofNullable(accountLink);
    }

    public static class AccountLinkUserInfo extends BaseLinkUserInfo {
        public AccountLinkUserInfo(LinkUserIdentificator userIdentificator, boolean confirmationEnabled) {
            super(userIdentificator, confirmationEnabled);
        }

        public AccountLinkUserInfo(String userId, boolean confirmationEnabled) {
            this(isLong(userId) ? new UserNumberIdentificator(Long.parseLong(userId)) : new UserStringIdentificator(userId), confirmationEnabled);
        }

        private static boolean isLong(String input) {
            try {
                Long.parseLong(input);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    }
}

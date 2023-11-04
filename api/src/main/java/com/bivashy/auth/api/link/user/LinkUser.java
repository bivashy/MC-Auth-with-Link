package com.bivashy.auth.api.link.user;

import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public interface LinkUser {

    static LinkUser of(LinkType linkType, Account account, LinkUserInfo userInfo) {
        return new LinkUser() {
            @Override
            public LinkType getLinkType() {
                return linkType;
            }

            @Override
            public Account getAccount() {
                return account;
            }

            @Override
            public LinkUserInfo getLinkUserInfo() {
                return userInfo;
            }
        };
    }

    /**
     * @return link type, for example link type of VK, link type of DISCORD,link
     * type of TELEGRAM.
     */
    LinkType getLinkType();

    /**
     * @return account that linked to VK or similar.
     */
    Account getAccount();

    /**
     * @return Instance of {@link LinkUserInfo}
     */
    LinkUserInfo getLinkUserInfo();

    default boolean isIdentifierDefaultOrNull() {
        return Optional.ofNullable(getLinkUserInfo())
                .map(LinkUserInfo::getIdentificator)
                .map(identificator -> identificator.equals(getLinkType().getDefaultIdentificator()) || identificator.asString() == null)
                .orElse(true);
    }

}

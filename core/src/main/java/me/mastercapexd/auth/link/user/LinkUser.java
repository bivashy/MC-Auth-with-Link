package me.mastercapexd.auth.link.user;

import java.util.Optional;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public interface LinkUser {

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
                .map(identificator -> !(identificator.equals(getLinkType().getDefaultIdentificator()) || identificator.asString() == null))
                .orElse(false);
    }
}

package me.mastercapexd.auth.config.message.link.context;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.config.message.context.account.BaseAccountPlaceholderContext;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;

public class LinkPlaceholderContext extends BaseAccountPlaceholderContext {
    protected LinkUser linkUser;

    public LinkPlaceholderContext(Account account, LinkType linkType, String linkName) {
        super(account);
        linkUser = account.findFirstLinkUser((user) -> user.getLinkType().equals(linkType)).orElseThrow(NullPointerException::new);
        registerPlaceholderProvider(PlaceholderProvider.of(linkUser.getLinkUserInfo().getIdentificator().asString(), "%" + linkName + "_id%"));
    }
}

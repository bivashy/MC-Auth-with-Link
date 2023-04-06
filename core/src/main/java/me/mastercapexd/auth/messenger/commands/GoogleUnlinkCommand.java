package me.mastercapexd.auth.messenger.commands;

import java.util.Objects;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleUnlinkCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "google-remove";
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    @GoogleUse
    @Default
    @ConfigurationArgumentError("google-unlink-not-enough-arguments")
    public void unlink(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        LinkUser linkUser = account.findFirstLinkUserOrNew(GoogleLinkType.LINK_USER_FILTER, GoogleLinkType.getInstance());

        String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();
        if (Objects.equals(linkUserKey, AccountFactory.DEFAULT_GOOGLE_KEY) || linkUserKey.isEmpty()) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlink-not-have-google", linkType.newMessageContext(account)));
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlinked", linkType.newMessageContext(account)));
        linkUser.getLinkUserInfo().setIdentificator(GoogleLinkType.getInstance().getDefaultIdentificator());
        accountDatabase.saveOrUpdateAccount(account);
    }
}

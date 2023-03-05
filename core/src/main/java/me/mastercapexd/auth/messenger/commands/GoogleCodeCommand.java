package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleCodeCommand implements OrphanCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @GoogleUse
    @Default
    @ConfigurationArgumentError("google-code-not-enough-arguments")
    public void googleCode(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account, Integer code) {
        LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
            GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, AccountFactory.DEFAULT_GOOGLE_KEY);
            account.addLinkUser(googleLinkUser);
            return googleLinkUser;
        });

        String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();
        if (linkUserKey == null || linkUser.isIdentifierDefaultOrNull()) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-account-not-have-google", linkType.newMessageContext(account)));
            return;
        }

        if (!plugin.getLinkEntryBucket().hasLinkUser(account.getPlayerId(), GoogleLinkType.getInstance())) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-not-need-enter", linkType.newMessageContext(account)));
            return;
        }

        if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(), code)) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-valid", linkType.newMessageContext(account)));
            account.getCurrentAuthenticationStep().getAuthenticationStepContext().setCanPassToNextStep(true);
            account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-not-valid", linkType.newMessageContext(account)));
    }
}

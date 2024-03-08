package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(GoogleCodeCommand.CONFIGURATION_KEY)
public class GoogleCodeCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "google-code";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @GoogleUse
    @ConfigurationArgumentError("google-code-not-enough-arguments")
    @DefaultFor("~")
    public void googleCode(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account, Integer code) {
        LinkUser linkUser = account.findFirstLinkUserOrNew(GoogleLinkType.LINK_USER_FILTER, GoogleLinkType.getInstance());

        String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();
        if (linkUserKey == null || linkUser.isIdentifierDefaultOrNull()) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-account-not-have-google", linkType.newMessageContext(account)));
            return;
        }

        if (!plugin.getLinkEntryBucket().find(account.getPlayerId(), GoogleLinkType.getInstance()).isPresent()) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-not-need-enter", linkType.newMessageContext(account)));
            return;
        }

        if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(), code)) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-valid", linkType.newMessageContext(account)));
            account.getCurrentAuthenticationStep().getAuthenticationStepContext().setCanPassToNextStep(true);
            if (account.isPremium()) {
                account.nextAuthenticationStep(plugin.getPremiumAuthenticationContextFactoryBucket().createContext(account));
            } else {
                account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
            }
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-code-not-valid", linkType.newMessageContext(account)));
    }
}

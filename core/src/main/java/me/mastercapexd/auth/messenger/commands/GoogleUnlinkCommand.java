package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountUnlinkEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(GoogleUnlinkCommand.CONFIGURATION_KEY)
public class GoogleUnlinkCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "google-remove";
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private EventBus eventBus;

    @GoogleUse
    @ConfigurationArgumentError("google-unlink-not-enough-arguments")
    @DefaultFor("~")
    public void unlink(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        LinkUser linkUser = account.findFirstLinkUserOrNew(GoogleLinkType.LINK_USER_FILTER, GoogleLinkType.getInstance());

        if (linkUser.isIdentifierDefaultOrNull()) {
            actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlink-not-have-google", linkType.newMessageContext(account)));
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getStringMessage("google-unlinked", linkType.newMessageContext(account)));
        eventBus.publish(AccountUnlinkEvent.class, account, false, GoogleLinkType.getInstance(), linkUser, linkUser.getLinkUserInfo().getIdentificator(),
                actorWrapper).thenAccept(result -> {
            if (result.getEvent().isCancelled())
                return;
            linkUser.getLinkUserInfo().setIdentificator(GoogleLinkType.getInstance().getDefaultIdentificator());
            accountDatabase.updateAccountLinks(account);
        });
    }
}

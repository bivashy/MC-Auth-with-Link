package me.mastercapexd.auth.messenger.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.event.AccountLinkEnterDeclineEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(AccountEnterDeclineCommand.CONFIGURATION_KEY)
public class AccountEnterDeclineCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "enter-decline";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private EventBus eventBus;

    @DefaultFor("~")
    public void onDecline(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Default("all") String declinePlayerName) {
        List<LinkEntryUser> accounts = plugin.getLinkEntryBucket().find(entryUser -> {
            if (!entryUser.getLinkType().equals(linkType))
                return false;

            if (!entryUser.getLinkUserInfo().getIdentificator().equals(actorWrapper.userId()))
                return false;

            Duration confirmationSecondsPassed = Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS);

            if (confirmationSecondsPassed.getSeconds() > linkType.getSettings().getEnterSettings().getEnterDelay())
                return false;

            if (!declinePlayerName.equals("all"))
                return entryUser.getAccount().getName().equalsIgnoreCase(declinePlayerName);
            return true;
        });
        if (accounts.isEmpty()) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-no-accounts"));
            return;
        }
        accounts.forEach((entryUser) -> eventBus.publish(AccountLinkEnterDeclineEvent.class, entryUser.getAccount(), false, linkType, entryUser, entryUser,
                actorWrapper).thenAccept(result -> {
            if (result.getEvent().isCancelled())
                return;
            plugin.getLinkEntryBucket().modifiable().remove(entryUser);
            entryUser.getAccount()
                    .kick(linkType.getServerMessages().getStringMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
        }));
    }
}

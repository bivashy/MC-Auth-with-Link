package me.mastercapexd.auth.messenger.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.AccountLinkEnterAcceptEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(AccountEnterAcceptCommand.CONFIGURATION_KEY)
public class AccountEnterAcceptCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "enter-accept";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private EventBus eventBus;

    @DefaultFor("~")
    public void onAccept(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Default("all") String acceptPlayerName) {
        List<LinkEntryUser> accounts = plugin.getLinkEntryBucket().find(entryUser -> {
            if (!entryUser.getLinkType().equals(linkType))
                return false;

            if (!entryUser.getLinkUserInfo().getIdentificator().equals(actorWrapper.userId()))
                return false;

            Duration confirmationSecondsPassed = Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS);

            if (confirmationSecondsPassed.getSeconds() > linkType.getSettings().getEnterSettings().getEnterDelay())
                return false;

            if (!acceptPlayerName.equals("all"))
                return entryUser.getAccount().getName().equalsIgnoreCase(acceptPlayerName);
            return true;
        });
        if (accounts.isEmpty()) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-no-accounts"));
            return;
        }
        accounts.forEach((entryUser) -> eventBus.publish(AccountLinkEnterAcceptEvent.class, entryUser.getAccount(), false, linkType, entryUser, entryUser,
                actorWrapper).thenAccept(result -> {
            if (result.getEvent().isCancelled())
                return;
            entryUser.setConfirmed(true);
            Account account = entryUser.getAccount();
            account.getPlayer().ifPresent(player -> player.sendMessage(linkType.getServerMessages().getStringMessage("enter-confirmed",
                    linkType.newMessageContext(account))));
            account.nextAuthenticationStep(account.isPremium() ?
                    plugin.getPremiumAuthenticationContextFactoryBucket().createContext(account) :
                    plugin.getAuthenticationContextFactoryBucket().createContext(account)
            );
            plugin.getLinkEntryBucket().modifiable().remove(entryUser);

            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-accepted", linkType.newMessageContext(account)));
        }));
    }
}

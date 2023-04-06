package me.mastercapexd.auth.messenger.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountEnterDeclineCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "enter-decline";
    @Dependency
    private AuthPlugin plugin;

    @Default
    public void onDecline(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Default("all") String acceptPlayerName) {
        List<LinkEntryUser> accounts = plugin.getLinkEntryBucket().getLinkUsers(entryUser -> {
            if (!entryUser.getLinkType().equals(linkType))
                return false;

            if (!entryUser.getLinkUserInfo().getIdentificator().equals(actorWrapper.userId()))
                return false;

            Duration confirmationSecondsPassed = Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS);

            if (confirmationSecondsPassed.getSeconds() > linkType.getSettings().getEnterSettings().getEnterDelay())
                return false;

            if (!acceptPlayerName.equals("all")) // If player not default value
                return entryUser.getAccount().getName().equalsIgnoreCase(acceptPlayerName); // Check if entryUser name
            // equals to accept player
            return true;
        });
        if (accounts.isEmpty()) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-no-accounts"));
            return;
        }
        accounts.forEach((entryUser) -> {
            plugin.getLinkEntryBucket().removeLinkUser(entryUser);
            entryUser.getAccount().kick(linkType.getServerMessages().getStringMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
        });
    }
}

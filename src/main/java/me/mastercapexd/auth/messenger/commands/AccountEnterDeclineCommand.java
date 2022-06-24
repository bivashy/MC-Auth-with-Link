package me.mastercapexd.auth.messenger.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountEnterDeclineCommand implements OrphanCommand {
    @Default
    public void onDecline(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Default("all") String acceptPlayerName) {
        List<LinkEntryUser> accounts = Auth.getLinkEntryAuth().getLinkUsers(entryUser -> {
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
            actorWrapper.reply(linkType.getLinkMessages().getMessageNullable("enter-no-accounts"));
            return;
        }
        accounts.forEach((entryUser) -> {
            entryUser.getAccount().kick(linkType.getProxyMessages().getStringMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
            actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-declined", linkType.newMessageContext(entryUser.getAccount())));
        });
    }
}

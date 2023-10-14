package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountLinkConfirmationToggleEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(ConfirmationToggleCommand.CONFIGURATION_KEY)
public class ConfirmationToggleCommand implements OrphanCommand {

    public static final String CONFIGURATION_KEY = "confirmation-toggle";
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private EventBus eventBus;

    @ConfigurationArgumentError("confirmation-no-player")
    @DefaultFor("~")
    @CommandCooldown(CommandCooldown.DEFAULT_VALUE)
    public void onConfirmationToggle(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        if (!linkType.getSettings().getConfirmationSettings().canToggleConfirmation()) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-toggle-disabled", linkType.newMessageContext(account)));
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-toggled", linkType.newMessageContext(account)));
        LinkUser linkUser = account.findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);
        eventBus.publish(AccountLinkConfirmationToggleEvent.class, account, false, linkType, linkUser, actorWrapper).thenAccept(result -> {
            if (result.getEvent().isCancelled())
                return;
            LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();
            linkUserInfo.setConfirmationEnabled(!linkUserInfo.isConfirmationEnabled());
            accountDatabase.saveOrUpdateAccount(account);
        });
    }

}

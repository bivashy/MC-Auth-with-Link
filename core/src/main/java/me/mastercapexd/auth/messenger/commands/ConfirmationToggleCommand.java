package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(ConfirmationToggleCommand.CONFIGURATION_KEY)
public class ConfirmationToggleCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "confirmation-toggle";
    @Dependency
    private AccountDatabase accountDatabase;

    @ConfigurationArgumentError("confirmation-no-player")
    @DefaultFor("~")
    public void onKick(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        if (!linkType.getSettings().getConfirmationSettings().canToggleConfirmation()) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-toggle-disabled", linkType.newMessageContext(account)));
            return;
        }
        actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-toggled", linkType.newMessageContext(account)));
        account.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).ifPresent(linkUser -> {
            LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();
            linkUserInfo.setConfirmationEnabled(!linkUserInfo.isConfirmationEnabled());
            accountDatabase.saveOrUpdateAccount(account);
        });
    }
}

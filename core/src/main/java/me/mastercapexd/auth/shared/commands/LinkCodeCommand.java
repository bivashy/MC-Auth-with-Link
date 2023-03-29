package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class LinkCodeCommand implements OrphanCommand {
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    @Default
    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    public void onLink(LinkCommandActorWrapper actorWrapper, LinkType linkType, MessengerLinkContext linkContext) {
        accountDatabase.getAccount(linkContext.getConfirmationUser().getAccount().getPlayerId()).thenAccept(account -> {

            account.findFirstLinkUserOrNew(linkUser -> linkUser.getLinkType().equals(linkType), linkType)
                    .getLinkUserInfo()
                    .setIdentificator(actorWrapper.userId());

            accountDatabase.saveOrUpdateAccount(account);

            linkContext.getConfirmationUser()
                    .getAccount()
                    .getPlayer()
                    .ifPresent(player -> player.sendMessage(linkType.getServerMessages().getMessage("linked")));

            actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-success", linkType.newMessageContext(account)));
        });
    }
}

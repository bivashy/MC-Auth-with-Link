package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class LinkCodeCommand implements OrphanCommand {
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    public void onLink(LinkCommandActorWrapper actorWrapper, LinkType linkType, MessengerLinkContext linkContext) {
        accountStorage.getAccount(linkContext.getConfirmationUser().getAccount().getId()).thenAccept(account -> {

            account.findFirstLinkUser(linkUser -> linkUser.getLinkType().equals(linkType)).get().getLinkUserInfo().setIdentificator(actorWrapper.userId());

            accountStorage.saveOrUpdateAccount(account);

            linkContext.getConfirmationUser().getAccount().getPlayer().ifPresent(player -> player.sendMessage(linkType.getProxyMessages().getStringMessage(
                    "linked")));

            actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-success", linkType.newMessageContext(account)));

            Auth.getLinkConfirmationAuth().removeLinkUser(linkContext.getConfirmationUser());
        });
    }
}

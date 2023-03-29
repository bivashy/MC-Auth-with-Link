package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

public class LinkCodeCommand implements OrphanCommand {
    private final LinkConfirmationType linkConfirmationType;
    private final Messages<?> messages;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private LinkConfirmationBucket linkConfirmationBucket;

    public LinkCodeCommand(LinkConfirmationType linkConfirmationType, Messages<?> messages) {
        this.linkConfirmationType = linkConfirmationType;
        this.messages = messages;
    }

    @Default
    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    public void onLink(MessageableCommandActor actor, MessengerLinkContext linkContext, @Optional LinkUserIdentificator possibleIdentificator) {
        LinkType linkType = linkContext.getConfirmationUser().getLinkType();
        LinkUserIdentificator identificator = linkConfirmationType.selectLinkUserIdentificator(linkContext.getConfirmationUser(), possibleIdentificator);
        accountDatabase.getAccount(linkContext.getConfirmationUser().getLinkTarget().getPlayerId())
                .thenAccept(account -> accountDatabase.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                    if (linkType.getSettings().getMaxLinkCount() > 0 && accounts.size() >= linkType.getSettings().getMaxLinkCount()) {
                        actor.replyWithMessage(messages.getMessage("link-limit-reached"));
                        return;
                    }
                    account.findFirstLinkUserOrNew(linkUser -> linkUser.getLinkType().equals(linkType), linkType)
                            .getLinkUserInfo()
                            .setIdentificator(identificator);

                    accountDatabase.saveOrUpdateAccount(account);

                    linkContext.getConfirmationUser()
                            .getLinkTarget()
                            .getPlayer()
                            .ifPresent(player -> player.sendMessage(linkType.getServerMessages().getMessage("linked")));

                    actor.replyWithMessage(messages.getMessage("confirmation-success"));
                    linkConfirmationBucket.removeLinkConfirmation(linkContext.getConfirmationUser());
                }));
    }
}

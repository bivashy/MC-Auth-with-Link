package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.annotation.DefaultForOrphan;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(LinkCodeCommand.CONFIGURATION_KEY)
public class LinkCodeCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "code";
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

    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    @DefaultForOrphan
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

                    actor.replyWithMessage(messages.getMessage("confirmation-success"));
                    linkConfirmationBucket.removeLinkConfirmation(linkContext.getConfirmationUser());
                }));
    }
}

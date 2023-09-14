package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountLinkEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.config.message.context.account.BaseAccountPlaceholderContext;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(LinkCodeCommand.CONFIGURATION_KEY)
public class LinkCodeCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "code";
    @Dependency
    private PluginConfig config;
    @Dependency
    private EventBus eventBus;
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private LinkConfirmationBucket linkConfirmationBucket;

    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    @DefaultFor("~")
    public void onLink(MessageableCommandActor actor, MessengerLinkContext linkContext, @Optional LinkUserIdentificator possibleIdentificator) {
        LinkConfirmationType linkConfirmationType = getLinkConfirmationType(actor);
        Messages<?> messages = linkConfirmationType.getConfirmationMessages(linkContext.getConfirmationUser());
        LinkType linkType = linkContext.getConfirmationUser().getLinkType();
        LinkUserIdentificator identificator = linkConfirmationType.selectLinkUserIdentificator(linkContext.getConfirmationUser(), possibleIdentificator);

        if (!linkType.getSettings().getLinkConfirmationTypes().contains(linkConfirmationType))
            return;

        accountDatabase.getAccount(linkContext.getConfirmationUser().getLinkTarget().getPlayerId())
                .thenAccept(account -> accountDatabase.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                    if (linkType.getSettings().getMaxLinkCount() > 0 && accounts.size() >= linkType.getSettings().getMaxLinkCount()) {
                        actor.replyWithMessage(messages.getMessage("link-limit-reached"));
                        return;
                    }
                    LinkUser foundLinkUser = account.findFirstLinkUserOrNew(linkUser -> linkUser.getLinkType().equals(linkType), linkType);
                    eventBus.publish(AccountLinkEvent.class, account, false, linkType, foundLinkUser, identificator, actor).thenAccept(result -> {
                        if (result.getEvent().isCancelled())
                            return;
                        foundLinkUser.getLinkUserInfo().setIdentificator(identificator);

                        accountDatabase.updateAccountLinks(account);

                        actor.replyWithMessage(messages.getMessage("confirmation-success", new BaseAccountPlaceholderContext(account)));
                        linkConfirmationBucket.modifiable().remove(linkContext.getConfirmationUser());
                    });
                }));
    }

    private LinkConfirmationType getLinkConfirmationType(MessageableCommandActor actor) {
        if (actor instanceof ServerCommandActor)
            return LinkConfirmationType.FROM_GAME;
        if (actor instanceof LinkCommandActorWrapper)
            return LinkConfirmationType.FROM_LINK;
        throw new IllegalArgumentException("Cannot resolve confirmation type for actor: " + actor);
    }
}

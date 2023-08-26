package me.mastercapexd.auth.shared.commands;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import revxrsal.commands.orphan.OrphanCommand;

public class MessengerLinkCommandTemplate implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "link-game";
    private final AuthPlugin plugin = AuthPlugin.instance();
    private final PluginConfig config = plugin.getConfig();
    private final Messages<?> messages;
    private final LinkType linkType;

    public MessengerLinkCommandTemplate(Messages<?> messages, LinkType linkType) {
        this.messages = messages;
        this.linkType = linkType;
    }

    public boolean isInvalidAccount(Account account, MessageableCommandActor commandActor, Predicate<LinkUser> linkFilter) {
        if (account == null || !account.isRegistered()) {
            commandActor.replyWithMessage(messages.getMessage("account-not-found"));
            return true;
        }
        LinkUser linkUser = account.findFirstLinkUserOrNew(linkFilter, linkType);
        if (!linkUser.isIdentifierDefaultOrNull()) {
            commandActor.replyWithMessage(messages.getMessage("already-linked"));
            return true;
        }
        return false;
    }

    public void sendLinkConfirmation(MessageableCommandActor commandActor, LinkConfirmationUser confirmationUser) {
        plugin.getLinkConfirmationBucket().modifiable().add(confirmationUser);
        commandActor.replyWithMessage(messages.getMessage("confirmation-sent", MessageContext.of("%code%", confirmationUser.getConfirmationCode())));
    }

    public String generateCode(Supplier<String> codeGenerator) {
        String code;
        do {
            code = codeGenerator.get();
        } while (codeExists(code));
        return code;
    }

    public LinkConfirmationType getLinkConfirmationType(MessageableCommandActor actor) {
        if (actor instanceof ServerCommandActor)
            return LinkConfirmationType.FROM_LINK;
        if (actor instanceof LinkCommandActorWrapper)
            return LinkConfirmationType.FROM_GAME;
        throw new IllegalArgumentException("Cannot resolve confirmation type for actor: " + actor);
    }

    private boolean codeExists(String code) {
        return plugin.getLinkConfirmationBucket().findFirst(user -> user.getConfirmationCode().equals(code)).isPresent();
    }
}
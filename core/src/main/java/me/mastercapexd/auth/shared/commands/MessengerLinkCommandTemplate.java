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
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

public class MessengerLinkCommandTemplate {
    private final AuthPlugin plugin = AuthPlugin.instance();
    private final PluginConfig config = plugin.getConfig();
    private final LinkConfirmationType linkConfirmationType;
    private final Messages<?> messages;
    private final LinkType linkType;

    public MessengerLinkCommandTemplate(LinkConfirmationType linkConfirmationType, Messages<?> messages, LinkType linkType) {
        this.linkConfirmationType = linkConfirmationType;
        this.messages = messages;
        this.linkType = linkType;
    }

    public boolean isInvalidAccount(Account account, MessageableCommandActor commandActor, Predicate<LinkUser> linkFilter) {
        if (account == null || !account.isRegistered()) {
            commandActor.replyWithMessage(config.getServerMessages().getMessage("account-not-found"));
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
        plugin.getLinkConfirmationBucket().addLinkConfirmation(confirmationUser);
        commandActor.replyWithMessage(messages.getMessage("confirmation-sent", MessageContext.of("%code%", confirmationUser.getConfirmationCode())));
    }

    public LinkConfirmationType getLinkConfirmationType() {
        return linkConfirmationType;
    }

    public String generateCode(Supplier<String> codeGenerator) {
        String code;
        do {
            code = codeGenerator.get();
        } while(codeExists(code));
        return code;
    }

    private boolean codeExists(String code) {
        return plugin.getLinkConfirmationBucket().findFirst(user -> user.getConfirmationCode().equals(code)).isPresent();
    }
}
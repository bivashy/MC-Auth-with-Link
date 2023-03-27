package me.mastercapexd.auth.server.commands;

import java.util.Collection;
import java.util.function.Predicate;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.server.ServerMessages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.server.player.ServerPlayer;

public class MessengerLinkCommandTemplate {
    private final AuthPlugin plugin = AuthPlugin.instance();
    private final PluginConfig config = plugin.getConfig();
    private final ServerMessages messages = config.getServerMessages();
    private final String subMessagesKey;
    private final LinkType linkType;

    public MessengerLinkCommandTemplate(String subMessagesKey, LinkType linkType) {
        this.subMessagesKey = subMessagesKey;
        this.linkType = linkType;
    }

    public boolean isValidAccount(Account account, ServerPlayer player, Predicate<LinkUser> linkFilter) {
        if (account == null || !account.isRegistered()) {
            player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
            return false;
        }
        LinkUser linkUser = account.findFirstLinkUserOrNew(linkFilter, linkType);
        if (!linkUser.isIdentifierDefaultOrNull()) {
            player.sendMessage(messages.getSubMessages(subMessagesKey).getMessage("already-linked"));
            return false;
        }
        return true;
    }

    public boolean isValidLinkAccounts(Collection<Account> accounts, ServerPlayer player) {
        if (linkType.getSettings().getMaxLinkCount() != 0 && accounts.size() >= linkType.getSettings().getMaxLinkCount()) {
            player.sendMessage(messages.getSubMessages(subMessagesKey).getMessage("link-limit-reached"));
            return false;
        }
        return true;
    }

    public void sendLinkConfirmation(LinkUserIdentificator identificator, ServerPlayer player, LinkConfirmationUser confirmationUser, String accountId) {
        plugin.getLinkConfirmationBucket()
                .removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                        linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(identificator));
        plugin.getLinkConfirmationBucket().addLinkUser(confirmationUser);
        player.sendMessage(messages.getSubMessages(subMessagesKey)
                .getMessage("confirmation-sent", MessageContext.of("%code%", confirmationUser.getConfirmationInfo().getConfirmationCode())));
    }
}

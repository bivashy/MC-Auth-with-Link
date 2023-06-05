package me.mastercapexd.auth.step.impl.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStepContext;
import com.bivashy.auth.api.step.MessageableAuthenticationStep;
import com.bivashy.messenger.common.identificator.Identificator;
import com.bivashy.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.step.AuthenticationStepTemplate;

public class MessengerAuthenticationStep extends AuthenticationStepTemplate implements MessageableAuthenticationStep {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private final LinkEntryUser linkEntryUser;

    public MessengerAuthenticationStep(String stepName, AuthenticationStepContext authenticationStepContext, LinkEntryUser linkEntryUser) {
        super(stepName, authenticationStepContext);
        this.linkEntryUser = linkEntryUser;
    }

    @Override
    public boolean shouldPassToNextStep() {
        return linkEntryUser.isConfirmed();
    }

    @Override
    public boolean shouldSkip() {
        Account account = authenticationStepContext.getAccount();
        LinkType linkType = linkEntryUser.getLinkType();

        if (!linkType.getSettings().isEnabled())
            return true;

        if (PLUGIN.getLinkEntryBucket().hasLinkUser(account.getPlayerId(), linkType))
            return true;

        if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability()))
            return true;

        LinkUser linkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).orElse(null);

        if (linkUser == null) {
            linkEntryUser.getAccount().getPlayer().ifPresent(player -> player.sendMessage(linkType.getServerMessages().getMessage("not-linked")));
            return true;
        }

        LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

        if (linkUser.isIdentifierDefaultOrNull()) {
            linkEntryUser.getAccount().getPlayer().ifPresent(player -> player.sendMessage(linkType.getServerMessages().getMessage("not-linked")));
            return true;
        }

        if (linkType.getSettings().getConfirmationSettings().canToggleConfirmation() && !linkUserInfo.isConfirmationEnabled())
            return true;

        PLUGIN.getLinkEntryBucket().addLinkUser(linkEntryUser);
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("confirmation", "%name%", account.getName());

        Identificator userIdentificator = linkUserInfo.getIdentificator().isNumber() ? Identificator.of(linkUserInfo.getIdentificator().asNumber()) :
                Identificator.of(linkUserInfo.getIdentificator().asString());
        linkType.newMessageBuilder(linkType.getSettings().getMessages().getMessage("enter-message", linkType.newMessageContext(account)))
                .keyboard(keyboard)
                .build()
                .send(userIdentificator);
        return false;
    }

    @Override
    public void process(ServerPlayer player) {
        Messages<ServerComponent> messages = linkEntryUser.getLinkType().getServerMessages();
        player.sendMessage(messages.getMessage("enter-confirm-need-chat", new ServerMessageContext(linkEntryUser.getAccount())));
        PLUGIN.getCore()
                .createTitle(messages.getMessage("enter-confirm-need-title"))
                .subtitle(messages.getMessage("enter-confirm-need-subtitle"))
                .stay(120)
                .send(player);
    }
}

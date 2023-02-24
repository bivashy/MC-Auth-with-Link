package me.mastercapexd.auth.authentication.step.steps.link;

import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.MessageableAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.entry.LinkEntryUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class MessengerAuthenticationStep extends AbstractAuthenticationStep implements MessageableAuthenticationStep {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
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
            linkEntryUser.getAccount().getPlayer().ifPresent(player -> player.sendMessage(linkType.getProxyMessages().getMessage("not-linked")));
            return true;
        }

        LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

        if (linkUserInfo == null || linkUser.isIdentifierDefaultOrNull()) {
            linkEntryUser.getAccount().getPlayer().ifPresent(player -> player.sendMessage(linkType.getProxyMessages().getMessage("not-linked")));
            return true;
        }

        if (!linkUserInfo.isConfirmationEnabled())
            return true;

        PLUGIN.getLinkEntryBucket().addLinkUser(linkEntryUser);
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("confirmation", "%name%", account.getName());

        Identificator userIdentificator = linkUserInfo.getIdentificator().isNumber() ? Identificator.of(linkUserInfo.getIdentificator().asNumber()) :
                Identificator.of(linkUserInfo.getIdentificator().asString());
        linkType.newMessageBuilder(linkType.getSettings().getMessages().getMessage("enter-message", linkType.newMessageContext(account))).keyboard(keyboard)
                .build().send(userIdentificator);
        return false;
    }

    @Override
    public void process(ProxyPlayer player) {
        Messages<ProxyComponent> messages = linkEntryUser.getLinkType().getProxyMessages();
        player.sendMessage(messages.getMessage("enter-confirm-need-chat", new ProxyMessageContext(linkEntryUser.getAccount())));
        PLUGIN.getCore().createTitle(messages.getMessage("enter-confirm-need-title")).subtitle(messages.getMessage("enter-confirm-need-subtitle"))
                .stay(120).send(player);
    }
}

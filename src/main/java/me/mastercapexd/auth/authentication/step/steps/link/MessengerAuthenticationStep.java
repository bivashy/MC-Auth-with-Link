package me.mastercapexd.auth.authentication.step.steps.link;

import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.keyboard.Keyboard;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class MessengerAuthenticationStep extends AbstractAuthenticationStep {

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

        if (!linkType.getSettings().isEnabled()) // Ignore if messenger was disabled in configuration
            return true;

        if (Auth.getLinkEntryAuth().hasLinkUser(account.getId(), linkType)) // Ignore if user already confirming
            return true;

        if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability())) // Ignore if player has active session
            return true;

        LinkUser linkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).orElse(null);

        if (linkUser == null) {
            linkType.getProxyMessages().getMessage("not-linked").ifPresent(component -> linkEntryUser.getAccount().getPlayer().get().sendMessage(linkType.newMessageContext(account).apply(component.legacyText())));
            return true;
        }

        LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

        if (linkUserInfo == null || linkUserInfo.getIdentificator().equals(linkType.getDefaultIdentificator()) || !linkUserInfo.getConfirmationState().shouldSendConfirmation()) {
            linkType.getProxyMessages().getMessage("not-linked").ifPresent(component -> linkEntryUser.getAccount().getPlayer().get().sendMessage(linkType.newMessageContext(account).apply(component.legacyText())));
            return true;
        }

        Auth.getLinkEntryAuth().addLinkUser(linkEntryUser);
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("confirmation", "%name%", account.getName());

        Identificator userIdentificator = linkUserInfo.getIdentificator().isNumber() ? Identificator.of(linkUserInfo.getIdentificator().asNumber()) :
                Identificator.of(linkUserInfo.getIdentificator().asString());
        linkType.newMessageBuilder(linkType.getSettings().getMessages().getMessage("enter-message", linkType.newMessageContext(account))).keyboard(keyboard).build().send(userIdentificator);
        return false;
    }

}

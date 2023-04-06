package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "account-control";

    @Default
    public void accountMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        Keyboard accountKeyboard = linkType.getSettings().getKeyboards().createKeyboard("account", "%account_name%", account.getName());
        actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("account-control", linkType.newMessageContext(account)))
                .keyboard(accountKeyboard)
                .build());
    }
}

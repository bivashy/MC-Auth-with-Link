package me.mastercapexd.auth.messenger.commands;

import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AdminPanelCommand implements OrphanCommand {
    @Default
    public void adminPanelMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType) {
        if (!linkType.getSettings().isAdministrator(actorWrapper.userId())) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("not-enough-permission"));
            return;
        }
        Keyboard adminPanelKeyboard = linkType.getSettings().getKeyboards().createKeyboard("admin-panel");
        actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("admin-panel")).keyboard(adminPanelKeyboard).build());
    }
}

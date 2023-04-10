package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.link.LinkType;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(AdminPanelCommand.CONFIGURATION_KEY)
public class AdminPanelCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "admin-panel";

    @DefaultFor("~")
    public void adminPanelMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType) {
        if (!linkType.getSettings().isAdministrator(actorWrapper.userId())) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("not-enough-permission"));
            return;
        }
        Keyboard adminPanelKeyboard = linkType.getSettings().getKeyboards().createKeyboard("admin-panel");
        actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("admin-panel")).keyboard(adminPanelKeyboard).build());
    }
}

package me.mastercapexd.auth.link.listener;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkDispatchCommandsSettings;
import com.bivashy.auth.api.event.AccountLinkEvent;
import com.bivashy.auth.api.event.AccountUnlinkEvent;
import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.link.google.GoogleLinkType;

public class DispatchCommandsListener {
    private final AuthPlugin plugin;

    public DispatchCommandsListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onLink(AccountLinkEvent event) {
        LinkDispatchCommandsSettings settings = event.getLinkType().getSettings().getDispatchCommandsSettings();
        if (!settings.isEnabled())
            return;
        for (String command : settings.getCommandsOnLink()) {
            plugin.getCore().dispatchConsoleCommand(command);
        }
    }

    @SubscribeEvent
    public void onUnlink(AccountUnlinkEvent event) {
        if (event.getLinkType() == GoogleLinkType.getInstance())
            return;
        LinkDispatchCommandsSettings settings = event.getLinkType().getSettings().getDispatchCommandsSettings();
        if (!settings.isEnabled())
            return;
        for (String command : settings.getCommandsOnUnlink()) {
            plugin.getCore().dispatchConsoleCommand(command);
        }
    }
}

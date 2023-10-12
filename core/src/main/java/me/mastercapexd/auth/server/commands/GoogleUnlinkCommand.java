package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountUnlinkEvent;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;

import io.github.revxrsal.eventbus.EventBus;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command({"googleunlink", "google unlink", "gunlink"})
public class GoogleUnlinkCommand {
    private static final Messages<ServerComponent> GOOGLE_MESSAGES = AuthPlugin.instance().getConfig().getServerMessages().getSubMessages("google");
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;
    @Dependency
    private EventBus eventBus;

    @GoogleUse
    @DefaultFor({"googleunlink", "google unlink", "gunlink"})
    @CommandCooldown(CommandCooldown.DEFAULT_VALUE)
    public void unlink(MessageableCommandActor actor, ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        accountStorage.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                return;
            }

            LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
            if (linkUser == null || linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("unlink-not-exists"));
                return;
            }
            eventBus.publish(AccountUnlinkEvent.class, account, false, GoogleLinkType.getInstance(), linkUser, linkUser.getLinkUserInfo().getIdentificator(),
                    actor).thenAccept(result -> {
                if (result.getEvent().isCancelled())
                    return;
                player.sendMessage(GOOGLE_MESSAGES.getMessage("unlinked"));
                linkUser.getLinkUserInfo().setIdentificator(GoogleLinkType.getInstance().getDefaultIdentificator());
                accountStorage.updateAccountLinks(account);
            });
        });
    }
}

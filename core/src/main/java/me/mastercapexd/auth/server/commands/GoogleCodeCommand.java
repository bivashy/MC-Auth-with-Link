package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command({"googlecode", "gcode"})
public class GoogleCodeCommand {
    private static final Messages<ServerComponent> GOOGLE_MESSAGES = AuthPlugin.instance().getConfig().getServerMessages().getSubMessages("google");
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @DefaultFor({"googlecode", "gcode"})
    public void defaultCommand(ServerPlayer player) {
        player.sendMessage(GOOGLE_MESSAGES.getMessage("code-not-enough-arguments"));
    }

    @GoogleUse
    @Command({"googlecode", "gcode", "google code"})
    public void googleCode(ServerPlayer player, Integer code) {
        String playerId = config.getActiveIdentifierType().getId(player);
        accountStorage.getAccount(playerId).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                return;
            }
            LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
            if (linkUser == null || linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("code-not-exists"));
                return;
            }
            if (!plugin.getLinkEntryBucket().hasLinkUser(playerId, GoogleLinkType.getInstance())) {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("code-not-need-enter"));
                return;
            }
            if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(), code)) {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("code-entered"));
                account.getCurrentAuthenticationStep().getAuthenticationStepContext().setCanPassToNextStep(true);
                account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
                return;
            }
            player.sendMessage(GOOGLE_MESSAGES.getMessage("code-wrong-code"));
        });
    }
}

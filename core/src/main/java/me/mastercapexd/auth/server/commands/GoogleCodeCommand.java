package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.step.impl.link.GoogleCodeAuthenticationStep;
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
    @AuthenticationStepCommand(stepName = GoogleCodeAuthenticationStep.STEP_NAME)
    @Command({"googlecode", "gcode", "google code"})
    public void googleCode(ServerPlayer player, Account account, Integer code) {
        String playerId = config.getActiveIdentifierType().getId(player);
        LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
        if (linkUser == null || linkUser.isIdentifierDefaultOrNull()) {
            player.sendMessage(GOOGLE_MESSAGES.getMessage("code-not-exists"));
            return;
        }
        if (!plugin.getLinkEntryBucket().find(playerId, GoogleLinkType.getInstance()).isPresent()) {
            player.sendMessage(GOOGLE_MESSAGES.getMessage("code-not-need-enter"));
            return;
        }
        if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(), code)) {
            player.sendMessage(GOOGLE_MESSAGES.getMessage("code-entered"));
            account.getCurrentAuthenticationStep().getAuthenticationStepContext().setCanPassToNextStep(true);
            if (account.isPremium()) {
                account.nextAuthenticationStep(plugin.getPremiumAuthenticationContextFactoryBucket().createContext(account));
            } else {
                account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
            }
            return;
        }
        player.sendMessage(GOOGLE_MESSAGES.getMessage("code-wrong-code"));
    }
}

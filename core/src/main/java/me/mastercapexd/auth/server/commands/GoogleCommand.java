package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command("google")
public class GoogleCommand {
    private static final Messages<ServerComponent> GOOGLE_MESSAGES = AuthPlugin.instance().getConfig().getServerMessages().getSubMessages("google");
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @GoogleUse
    @DefaultFor("google")
    public void linkGoogle(ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        accountStorage.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                return;
            }
            String key = plugin.getGoogleAuthenticator().createCredentials().getKey();
            LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
                GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, key);
                account.addLinkUser(googleLinkUser);
                return googleLinkUser;
            });
            if (linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("generated", MessageContext.of("%google_key%", key)));
            } else {
                player.sendMessage(GOOGLE_MESSAGES.getMessage("regenerated", MessageContext.of("%google_key%", key)));
            }
            linkUser.getLinkUserInfo().getIdentificator().setString(key);
            accountStorage.saveOrUpdateAccount(account);
        });
    }
}

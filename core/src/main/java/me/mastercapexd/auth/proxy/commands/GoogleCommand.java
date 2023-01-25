package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command("google")
public class GoogleCommand {
    private static final Messages<ProxyComponent> GOOGLE_MESSAGES = ProxyPlugin.instance().getConfig().getProxyMessages().getSubMessages("google");
    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @GoogleUse
    public void linkGoogle(ProxyPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        accountStorage.getAccount(id).thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
                return;
            }
            String key = plugin.getGoogleAuthenticator().createCredentials().getKey();
            LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
                GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, key);
                account.addLinkUser(googleLinkUser);
                return googleLinkUser;
            });
            if (linkUser.isIdentifierDefaultOrNull()) {
                player.sendMessage(GOOGLE_MESSAGES.getStringMessage("generated").replaceAll("(?i)%google_key%", key));
            } else {
                player.sendMessage(GOOGLE_MESSAGES.getStringMessage("regenerated").replaceAll("(?i)%google_key%", key));
            }
            linkUser.getLinkUserInfo().getIdentificator().setString(key);
            accountStorage.saveOrUpdateAccount(account);
        });
    }
}

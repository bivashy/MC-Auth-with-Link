package me.mastercapexd.auth.listener;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.PlayerChatPasswordEvent;
import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.server.commands.impl.LoginCommandImplementation;
import me.mastercapexd.auth.server.commands.impl.RegisterCommandImplementation;
import me.mastercapexd.auth.server.commands.parameters.RegisterPassword;

public class AuthenticationChatPasswordListener {
    private final AuthPlugin plugin;

    public AuthenticationChatPasswordListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onPlayerChatPassword(PlayerChatPasswordEvent e) {
        Account account = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccountNullable(e.getPlayer());

        if (!account.isRegistered()) {
            RegisterCommandImplementation impl = new RegisterCommandImplementation(plugin, plugin.getConfig(), plugin.getAccountDatabase());
            impl.performRegister(e.getPlayer(), account, new RegisterPassword(e.getPassword()));
        } else {
            LoginCommandImplementation impl = new LoginCommandImplementation(plugin, plugin.getConfig());
            impl.performLogin(e.getPlayer(), account, e.getPassword());
        }
    }
}
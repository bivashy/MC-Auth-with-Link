package me.mastercapexd.auth.listener;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.event.PlayerChatPasswordEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.server.commands.impl.LoginCommandImplementation;
import me.mastercapexd.auth.server.commands.impl.RegisterCommandImplementation;
import me.mastercapexd.auth.server.commands.parameters.RegisterPassword;

public class AuthenticationChatPasswordListener {

    private final AuthPlugin plugin;
    private final PluginConfig config;

    public AuthenticationChatPasswordListener(AuthPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @SubscribeEvent
    public void onPlayerChatPassword(PlayerChatPasswordEvent e) {
        String[] messageParts = e.getPassword().split("\\s+");
        String password = messageParts[0];
        Account account = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccountNullable(e.getPlayer());

        if (!account.isRegistered()) {
            if (passwordConfirmationFailed(messageParts, password, e.getPlayer()))
                return;
            RegisterCommandImplementation impl = new RegisterCommandImplementation(plugin);
            impl.performRegister(e.getPlayer(), account, new RegisterPassword(password));
        } else {
            LoginCommandImplementation impl = new LoginCommandImplementation(plugin);
            impl.performLogin(e.getPlayer(), account, password);
        }
    }

    private boolean passwordConfirmationFailed(String[] messageParts, String password, ServerPlayer player) {
        if (config.isPasswordConfirmationEnabled()) {
            if (messageParts.length < 2) {
                player.sendMessage(config.getServerMessages().getMessage("confirm-password"));
                return true;
            }
            String confirmationPassword = messageParts[1];
            if (!confirmationPassword.equals(password)) {
                player.sendMessage(config.getServerMessages().getMessage("confirm-failed"));
                return true;
            }
        }
        return false;
    }

}
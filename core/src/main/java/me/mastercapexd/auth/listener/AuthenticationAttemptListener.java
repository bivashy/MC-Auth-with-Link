package me.mastercapexd.auth.listener;

import java.util.HashMap;
import java.util.Map;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.event.AccountStateClearEvent;
import com.bivashy.auth.api.event.AccountTryLoginEvent;

import io.github.revxrsal.eventbus.SubscribeEvent;

public class AuthenticationAttemptListener {
    private final Map<String, Integer> loginAttemptCounts = new HashMap<>();
    private final AuthPlugin plugin;

    public AuthenticationAttemptListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onLogin(AccountTryLoginEvent e) {
        Account account = e.getAccount();
        if (e.isRightPassword())
            return;
        e.setCancelled(true);
        if (plugin.getConfig().getPasswordAttempts() < 1) {
            account.getPlayer().ifPresent(player -> player.sendMessage(plugin.getConfig().getServerMessages().getMessage("wrong-password")));
            return;
        }
        int loginAttempts = incrementLoginAttemptsAndGet(e.getAccount());
        account.getPlayer()
                .ifPresent(player -> player.sendMessage(plugin.getConfig()
                        .getServerMessages()
                        .getMessage("wrong-password",
                                MessageContext.of("%attempts%", Integer.toString(plugin.getConfig().getPasswordAttempts() - loginAttempts)))));
        if (loginAttempts < AuthPlugin.instance().getConfig().getPasswordAttempts())
            return;
        account.getPlayer().ifPresent(player -> player.disconnect(plugin.getConfig().getServerMessages().getMessage("attempts-limit")));
    }

    @SubscribeEvent
    public void onClear(AccountStateClearEvent e) {
        e.getAccount().map(Account::getPlayerId).ifPresent(loginAttemptCounts::remove);
    }

    private int incrementLoginAttemptsAndGet(Account account) {
        int accountLoginAttempts = loginAttemptCounts.getOrDefault(account.getPlayerId(), 0);
        loginAttemptCounts.put(account.getPlayerId(), accountLoginAttempts + 1);
        return loginAttemptCounts.get(account.getPlayerId());
    }
}

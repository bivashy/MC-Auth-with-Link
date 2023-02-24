package me.mastercapexd.auth.listener;

import java.util.HashMap;
import java.util.Map;

import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.event.AccountStateClearEvent;
import me.mastercapexd.auth.event.AccountTryLoginEvent;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class AuthenticationAttemptListener {
    private final Map<String, Integer> loginAttemptCounts = new HashMap<>();
    private final ProxyPlugin plugin;

    public AuthenticationAttemptListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onLogin(AccountTryLoginEvent e) {
        Account account = e.getAccount();
        if (e.isRightPassword())
            return;
        e.setCancelled(true);
        if (plugin.getConfig().getPasswordAttempts() < 1) {
            account.getPlayer().ifPresent(player -> player.sendMessage(plugin.getConfig().getProxyMessages().getMessage("wrong-password")));
            return;
        }
        int loginAttempts = incrementLoginAttemptsAndGet(e.getAccount());
        account.getPlayer()
                .ifPresent(player -> player.sendMessage(plugin.getConfig()
                        .getProxyMessages()
                        .getMessage("wrong-password",
                                MessageContext.of("%attempts%", Integer.toString(plugin.getConfig().getPasswordAttempts() - loginAttempts)))));
        if (loginAttempts < ProxyPlugin.instance().getConfig().getPasswordAttempts())
            return;
        account.getPlayer().ifPresent(player -> player.disconnect(plugin.getConfig().getProxyMessages().getMessage("attempts-limit")));
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

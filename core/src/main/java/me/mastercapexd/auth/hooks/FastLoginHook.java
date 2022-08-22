package me.mastercapexd.auth.hooks;

import java.util.Optional;

import com.github.games647.fastlogin.core.hooks.AuthPlugin;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public abstract class FastLoginHook<T> implements AuthPlugin<T> {
    private final ProxyPlugin plugin;

    public FastLoginHook(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean forceLogin(T player) {
        Optional<ProxyPlayer> proxyPlayerOptional = plugin.getCore().wrapPlayer(player);
        if (!proxyPlayerOptional.isPresent())
            return false;
        String accountId = plugin.getConfig().getActiveIdentifierType().getId(proxyPlayerOptional.get());
        if (!Auth.hasAccount(accountId))
            return false;
        Account account = Auth.getAccount(accountId);
        while(!account.getCurrentAuthenticationStep().getStepName().equals(LoginAuthenticationStep.STEP_NAME) &&
                !account.getCurrentAuthenticationStep().getStepName().equals(RegisterAuthenticationStep.STEP_NAME))
            account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryDealership().createContext(account));
        return true;
    }

    @Override
    public boolean forceRegister(T player, String password) {
        Optional<ProxyPlayer> proxyPlayerOptional = plugin.getCore().wrapPlayer(player);
        if (!proxyPlayerOptional.isPresent())
            return false;
        ProxyPlayer proxyPlayer = proxyPlayerOptional.get();
        Account account = plugin.getAccountFactory()
                .createAccount(plugin.getConfig().getActiveIdentifierType().getId(proxyPlayer),
                        plugin.getConfig().getActiveIdentifierType(), proxyPlayer.getUniqueId(), proxyPlayer.getNickname(),
                        plugin.getConfig().getActiveHashType(), plugin.getConfig().getActiveHashType().hash(password),
                        plugin.getConfig().getSessionDurability());
        plugin.getAccountStorage().saveOrUpdateAccount(account);
        return true;
    }

    @Override
    public boolean isRegistered(String nickname) throws Exception {
        Account account = plugin.getAccountStorage().getAccountFromName(nickname).get();
        return account == null || account.isRegistered();
    }
}

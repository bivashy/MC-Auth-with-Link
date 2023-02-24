package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.event.AccountTryChangePasswordEvent;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class ChangePasswordCommand implements OrphanCommand {
    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @ConfigurationArgumentError("changepass-not-enough-arguments")
    public void onPasswordChange(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account, NewPassword newPassword) {
        plugin.getEventBus().publish(AccountTryChangePasswordEvent.class, account, false, true).thenAccept(tryChangePasswordEventPostResult -> {
            if (tryChangePasswordEventPostResult.getEvent().isCancelled())
                return;
            account.setPasswordHash(account.getHashType().hash(newPassword.getNewPassword()));
            accountStorage.saveOrUpdateAccount(account);
            actorWrapper.reply(linkType.getLinkMessages()
                    .getStringMessage("changepass-success", linkType.newMessageContext(account))
                    .replaceAll("(?i)%password%", newPassword.getNewPassword()));
        });
    }
}

package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountTryChangePasswordEvent;
import com.bivashy.auth.api.link.LinkType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class ChangePasswordCommand implements OrphanCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private AccountDatabase accountStorage;

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

package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class RestoreCommand implements OrphanCommand {
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @ConfigurationArgumentError("restore-not-enough-arguments")
    public void onRestore(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        String generatedPassword = linkType.getSettings().getRestoreSettings().generateCode();
        account.setPasswordHash(account.getHashType().hash(generatedPassword));
        account.logout(config.getSessionDurability());
        account.kick(linkType.getProxyMessages().getStringMessage("kicked", linkType.newMessageContext(account)));
        actorWrapper.reply(linkType.getLinkMessages().getMessage("restored", linkType.newMessageContext(account)).replaceAll("(?i)%password%",
                generatedPassword));
        accountStorage.saveOrUpdateAccount(account);
    }
}

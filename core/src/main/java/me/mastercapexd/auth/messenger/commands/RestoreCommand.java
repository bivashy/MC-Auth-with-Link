package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(RestoreCommand.CONFIGURATION_KEY)
public class RestoreCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "restore";
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountDatabase;

    @ConfigurationArgumentError("restore-not-enough-arguments")
    @DefaultFor("~")
    @CommandCooldown(CommandCooldown.DEFAULT_VALUE)
    public void onRestore(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        String generatedPassword = linkType.getSettings().getRestoreSettings().generateCode();
        account.setPasswordHash(account.getCryptoProvider().hash(HashInput.of(generatedPassword)));
        account.logout(config.getSessionDurability());
        account.kick(linkType.getServerMessages().getStringMessage("kicked", linkType.newMessageContext(account)));
        actorWrapper.reply(linkType.getLinkMessages().getMessage("restored", linkType.newMessageContext(account)).replaceAll("(?i)%password%",
                generatedPassword));
        accountDatabase.saveOrUpdateAccount(account);
    }
}

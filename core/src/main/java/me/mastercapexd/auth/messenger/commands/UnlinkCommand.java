package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(UnlinkCommand.CONFIGURATION_KEY)
public class UnlinkCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "unlink";
    @Dependency
    private AccountDatabase accountDatabase;

    @ConfigurationArgumentError("unlink-not-enough-arguments")
    @DefaultFor("~")
    public void onUnlink(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        account.findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType)
                .getLinkUserInfo()
                .setIdentificator(linkType.getDefaultIdentificator());
        accountDatabase.updateAccountLinks(account);
        actorWrapper.reply(linkType.getLinkMessages().getMessage("unlinked", linkType.newMessageContext(account)));
    }
}

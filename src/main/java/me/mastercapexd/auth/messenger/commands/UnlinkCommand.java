package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class UnlinkCommand implements OrphanCommand {
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @ConfigurationArgumentError("unlink-not-enough-arguments")
    public void onUnlink(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        account.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).get().getLinkUserInfo().setIdentificator(linkType.getDefaultIdentificator());
        accountStorage.saveOrUpdateAccount(account);
        actorWrapper.reply(linkType.getLinkMessages().getMessage("unlinked", linkType.newMessageContext(account)));
    }
}

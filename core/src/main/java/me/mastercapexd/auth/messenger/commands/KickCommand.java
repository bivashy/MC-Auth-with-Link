package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.type.KickResultType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class KickCommand implements OrphanCommand {
    @Default
    @ConfigurationArgumentError("kick-not-enough-arguments")
    public void onKick(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        actorWrapper.reply(linkType.getLinkMessages().getMessage("kick-starting", linkType.newMessageContext(account)));
        KickResultType kickResult = account.kick(linkType.getServerMessages().getStringMessage("kicked"));
        actorWrapper.reply(linkType.getLinkMessages().getMessage(kickResult.getConfigurationPath(), linkType.newMessageContext(account)));
    }
}

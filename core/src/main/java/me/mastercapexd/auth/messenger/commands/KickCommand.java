package me.mastercapexd.auth.messenger.commands;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.type.KickResultType;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(KickCommand.CONFIGURATION_KEY)
public class KickCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "kick";

    @ConfigurationArgumentError("kick-not-enough-arguments")
    @DefaultFor("~")
    public void onKick(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        actorWrapper.reply(linkType.getLinkMessages().getMessage("kick-starting", linkType.newMessageContext(account)));
        KickResultType kickResult = account.kick(linkType.getServerMessages().getStringMessage("kicked"));
        actorWrapper.reply(linkType.getLinkMessages().getMessage(kickResult.getConfigurationPath(), linkType.newMessageContext(account)));
    }
}

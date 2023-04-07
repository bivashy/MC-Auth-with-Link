package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.annotation.LinkCommand;
import me.mastercapexd.auth.shared.commands.LinkCodeCommand;
import me.mastercapexd.auth.shared.commands.annotation.DefaultForOrphan;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.annotation.Optional;

@LinkCommand(VkLinkCodeCommand.LINK_NAME)
public class VkLinkCodeCommand extends LinkCodeCommand {
    public static final String LINK_NAME = "VK";
    public VkLinkCodeCommand(LinkConfirmationType linkConfirmationType) {
        super(linkConfirmationType, VKLinkType.getInstance().getServerMessages());
    }

    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    @DefaultForOrphan
    public void onCodeLink(MessageableCommandActor actor, MessengerLinkContext linkContext, @Optional LinkUserIdentificator possibleIdentificator) {
        onLink(actor, linkContext, possibleIdentificator);
    }
}
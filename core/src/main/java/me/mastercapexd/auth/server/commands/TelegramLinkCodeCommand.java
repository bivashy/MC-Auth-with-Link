package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.ConfigurationArgumentError;
import me.mastercapexd.auth.shared.commands.annotation.LinkCommand;
import me.mastercapexd.auth.shared.commands.LinkCodeCommand;
import me.mastercapexd.auth.shared.commands.annotation.DefaultForOrphan;
import me.mastercapexd.auth.shared.commands.parameter.MessengerLinkContext;
import revxrsal.commands.annotation.Optional;

@LinkCommand(TelegramLinkCodeCommand.LINK_NAME)
public class TelegramLinkCodeCommand extends LinkCodeCommand {
    public static final String LINK_NAME = "TELEGRAM";

    public TelegramLinkCodeCommand(LinkConfirmationType linkConfirmationType) {
        super(linkConfirmationType, TelegramLinkType.getInstance().getServerMessages());
    }

    @ConfigurationArgumentError("confirmation-not-enough-arguments")
    @DefaultForOrphan
    public void onCodeLink(MessageableCommandActor actor, MessengerLinkContext linkContext, @Optional LinkUserIdentificator possibleIdentificator) {
        onLink(actor, linkContext, possibleIdentificator);
    }
}
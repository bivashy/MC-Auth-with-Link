package me.mastercapexd.auth.link;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.ubivaska.messenger.common.message.Message;

import revxrsal.commands.command.CommandActor;

public interface LinkCommandActorWrapper extends CommandActor, MessageableCommandActor {
    void send(Message message);

    LinkUserIdentificator userId();

    @Override
    default void replyWithMessage(Object message) {
        if (message instanceof String)
            reply((String) message);
    }
}

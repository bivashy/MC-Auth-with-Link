package me.mastercapexd.auth.link;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.ubivaska.messenger.common.message.Message;

import revxrsal.commands.command.CommandActor;

public interface LinkCommandActorWrapper extends CommandActor {
    void send(Message message);

    LinkUserIdentificator userId();
}

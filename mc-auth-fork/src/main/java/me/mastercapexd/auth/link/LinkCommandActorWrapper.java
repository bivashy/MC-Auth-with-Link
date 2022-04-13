package me.mastercapexd.auth.link;

import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import revxrsal.commands.command.CommandActor;

public interface LinkCommandActorWrapper extends CommandActor {
	void send(Message message);

	LinkUserIdentificator userId();
}

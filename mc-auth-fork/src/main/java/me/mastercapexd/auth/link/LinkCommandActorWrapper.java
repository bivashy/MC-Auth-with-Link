package me.mastercapexd.auth.link;

import java.util.UUID;

import me.mastercapexd.auth.link.message.Message;
import revxrsal.commands.command.CommandActor;

public interface LinkCommandActorWrapper {
	void send(Message message);

	String getName();

	UUID getUniqueId();

	void reply(String message);

	void error(String message);

	<T extends CommandActor> T as(Class<T> type);
}

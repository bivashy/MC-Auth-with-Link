package me.mastercapexd.auth.bungee.commands.exception;

import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.SendableException;

public class BungeeSendableException extends SendableException {
	public BungeeSendableException(String message) {
		super(message);

	}

	@Override
	public void sendTo(CommandActor actor) {
		actor.as(BungeeCommandActor.class).reply(getMessage());
	}
}

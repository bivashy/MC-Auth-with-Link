package me.mastercapexd.auth.link;

import java.util.UUID;

import revxrsal.commands.command.CommandActor;

public abstract class AbstractLinkCommandActorWrapper implements LinkCommandActorWrapper {

	protected final CommandActor actor;

	public AbstractLinkCommandActorWrapper(CommandActor actor) {
		this.actor = actor;
	}

	@Override
	public String getName() {
		return actor.getName();
	}

	@Override
	public UUID getUniqueId() {
		return actor.getUniqueId();
	}

	@Override
	public void reply(String message) {
		actor.reply(message);
	}

	@Override
	public void error(String message) {
		actor.error(message);
	}

	@Override
	public <T extends CommandActor> T as(Class<T> type) {
		return actor.as(type);
	}

}

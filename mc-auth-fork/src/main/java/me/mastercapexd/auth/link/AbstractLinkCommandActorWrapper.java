package me.mastercapexd.auth.link;

import java.util.UUID;

import revxrsal.commands.command.CommandActor;

public abstract class AbstractLinkCommandActorWrapper<T extends CommandActor> implements LinkCommandActorWrapper {

	protected final T actor;

	public AbstractLinkCommandActorWrapper(T actor) {
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
	public <E extends CommandActor> E as(Class<E> type) {
		return actor.as(type);
	}

}

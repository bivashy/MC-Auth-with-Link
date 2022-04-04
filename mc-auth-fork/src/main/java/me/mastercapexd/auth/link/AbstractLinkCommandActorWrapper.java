package me.mastercapexd.auth.link;

import java.util.Locale;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.locales.Translator;

public abstract class AbstractLinkCommandActorWrapper<T extends CommandActor> implements LinkCommandActorWrapper {

	protected final T actor;

	public AbstractLinkCommandActorWrapper(T actor) {
		this.actor = actor;
	}

	@Override
	public @NotNull String getName() {
		return actor.getName();
	}

	@Override
	public @NotNull UUID getUniqueId() {
		return actor.getUniqueId();
	}

	@Override
	public void reply(@NotNull String message) {
		actor.reply(message);
	}

	@Override
	public void error(@NotNull String message) {
		actor.error(message);
	}

	@Override
	public CommandHandler getCommandHandler() {
		return actor.getCommandHandler();
	}

	@Override
	public Translator getTranslator() {
		return actor.getTranslator();
	}

	@Override
	public @NotNull Locale getLocale() {
		return actor.getLocale();
	}

	@Override
	public void replyLocalized(@NotNull String key, Object... args) {
		actor.replyLocalized(key, args);
	}

	@Override
	public void errorLocalized(@NotNull String key, Object... args) {
		actor.errorLocalized(key, args);
	}
}

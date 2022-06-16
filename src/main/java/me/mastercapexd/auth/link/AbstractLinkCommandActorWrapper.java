package me.mastercapexd.auth.link;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;

import java.util.UUID;

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
}

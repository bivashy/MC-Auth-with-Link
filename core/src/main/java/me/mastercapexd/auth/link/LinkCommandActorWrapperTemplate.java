package me.mastercapexd.auth.link;

import java.util.UUID;

import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;

public abstract class LinkCommandActorWrapperTemplate<T extends CommandActor> implements LinkCommandActorWrapper {
    protected final T actor;

    public LinkCommandActorWrapperTemplate(T actor) {
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
    public CommandHandler getCommandHandler() {
        return actor.getCommandHandler();
    }
}

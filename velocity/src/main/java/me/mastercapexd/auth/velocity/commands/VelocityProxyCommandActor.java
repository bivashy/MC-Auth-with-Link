package me.mastercapexd.auth.velocity.commands;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.velocity.VelocityCommandActor;

public class VelocityProxyCommandActor implements ProxyCommandActor {
    private final VelocityCommandActor actor;

    public VelocityProxyCommandActor(VelocityCommandActor actor) {
        this.actor = actor;
    }

    @Override
    public void reply(ProxyComponent component) {
        component.safeAs(VelocityComponent.class).ifPresent(velocityComponent -> actor.reply(velocityComponent.component()));
        component.safeAs(AdventureProxyComponent.class).ifPresent(adventureProxyComponent -> actor.reply(adventureProxyComponent.getComponent()));
    }

    @Override
    @NotNull
    public String getName() {
        return actor.getName();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return actor.getUniqueId();
    }

    @Override
    public void reply(@NotNull String s) {
        actor.reply(s);
    }

    @Override
    public void error(@NotNull String s) {
        actor.error(s);
    }

    @Override
    public CommandHandler getCommandHandler() {
        return actor.getCommandHandler();
    }
}

package me.mastercapexd.auth.bungee.commands;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bungee.BungeeCommandActor;

public class BungeeProxyCommandActor implements ProxyCommandActor {
    private final BungeeCommandActor actor;

    public BungeeProxyCommandActor(BungeeCommandActor actor) {
        this.actor = actor;
    }

    @Override
    public void reply(ProxyComponent component) {
        component.safeAs(BungeeComponent.class).ifPresent(bungeeComponent -> actor.getSender().sendMessage(bungeeComponent.components()));
        component.safeAs(AdventureProxyComponent.class)
                .ifPresent(adventureProxyComponent -> AuthPlugin.getInstance()
                        .getAudienceProvider()
                        .sender(actor.getSender())
                        .sendMessage(adventureProxyComponent.getComponent()));
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

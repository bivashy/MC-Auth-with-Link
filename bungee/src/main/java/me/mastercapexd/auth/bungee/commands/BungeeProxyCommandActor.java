package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
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
}

package me.mastercapexd.auth.bungee.commands;

import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import revxrsal.commands.bungee.BungeeCommandActor;

public class BungeeServerCommandActor implements ServerCommandActor, MessageableCommandActor {
    private final BungeeCommandActor actor;

    public BungeeServerCommandActor(BungeeCommandActor actor) {
        this.actor = actor;
    }

    @Override
    public void reply(ServerComponent component) {
        component.safeAs(BungeeComponent.class).ifPresent(bungeeComponent -> actor.getSender().sendMessage(bungeeComponent.components()));
        component.safeAs(AdventureServerComponent.class)
                .ifPresent(adventureProxyComponent -> BungeeAuthPluginBootstrap.getInstance()
                        .getBungeeAudiences()
                        .sender(actor.getSender())
                        .sendMessage(adventureProxyComponent.component()));
    }

    @Override
    public void replyWithMessage(Object message) {
        if (message instanceof ServerComponent)
            reply((ServerComponent) message);
        if (message instanceof String)
            actor.reply((String) message);
    }
}

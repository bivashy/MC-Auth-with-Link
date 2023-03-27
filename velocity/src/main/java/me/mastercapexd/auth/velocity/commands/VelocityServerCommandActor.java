package me.mastercapexd.auth.velocity.commands;

import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;

import net.kyori.adventure.text.Component;
import revxrsal.commands.velocity.VelocityCommandActor;

public class VelocityServerCommandActor implements ServerCommandActor, MessageableCommandActor {
    private final VelocityCommandActor actor;

    public VelocityServerCommandActor(VelocityCommandActor actor) {
        this.actor = actor;
    }

    @Override
    public void reply(ServerComponent component) {
        component.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component).ifPresent(actor::reply);
    }

    @Override
    public void replyWithMessage(Object message) {
        if (message instanceof ServerComponent)
            reply((ServerComponent) message);
        if (message instanceof Component)
            actor.reply((Component) message);
        if (message instanceof String)
            actor.reply((String) message);
    }
}

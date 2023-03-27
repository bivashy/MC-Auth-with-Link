package me.mastercapexd.auth.velocity.commands;

import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;

import revxrsal.commands.velocity.VelocityCommandActor;

public class VelocityServerCommandActor implements ServerCommandActor {
    private final VelocityCommandActor actor;

    public VelocityServerCommandActor(VelocityCommandActor actor) {
        this.actor = actor;
    }

    @Override
    public void reply(ServerComponent component) {
        component.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component).ifPresent(actor::reply);
    }
}

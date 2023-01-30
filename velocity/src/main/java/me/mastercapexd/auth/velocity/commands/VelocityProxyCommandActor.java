package me.mastercapexd.auth.velocity.commands;

import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
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
}

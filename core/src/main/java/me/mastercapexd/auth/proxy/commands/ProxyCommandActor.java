package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import revxrsal.commands.command.CommandActor;

public interface ProxyCommandActor extends CommandActor {
    void reply(ProxyComponent component);
}

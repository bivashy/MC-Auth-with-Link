package me.mastercapexd.auth.server.commands.exception;

import com.bivashy.auth.api.server.message.ServerComponent;

import revxrsal.commands.exception.ThrowableFromCommand;

@ThrowableFromCommand
public class SendComponentException extends RuntimeException {
    private final ServerComponent component;

    public SendComponentException(ServerComponent component) {
        this.component = component;
    }

    public ServerComponent getComponent() {
        return component;
    }
}

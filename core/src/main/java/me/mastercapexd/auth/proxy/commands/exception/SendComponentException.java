package me.mastercapexd.auth.proxy.commands.exception;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import revxrsal.commands.exception.ThrowableFromCommand;

@ThrowableFromCommand
public class SendComponentException extends RuntimeException {
    private final ProxyComponent component;

    public SendComponentException(ProxyComponent component) {
        this.component = component;
    }

    public ProxyComponent getComponent() {
        return component;
    }
}

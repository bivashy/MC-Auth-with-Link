package com.bivashy.auth.api.event.result;

import com.bivashy.auth.api.server.message.ServerComponent;

public class PreLoginResult {
    private final PreLoginState state;
    private ServerComponent disconnectMessage;

    public PreLoginResult(PreLoginState state, ServerComponent disconnectMessage) {
        this.state = state;
        this.disconnectMessage = disconnectMessage;
    }

    public PreLoginResult(PreLoginState state) {
        this.state = state;
    }

    public PreLoginState getState() {
        return state;
    }

    public ServerComponent getDisconnectMessage() {
        return disconnectMessage;
    }

    public enum PreLoginState {
        FORCE_ONLINE,
        FORCE_OFFLINE,
        DENIED
    }
}

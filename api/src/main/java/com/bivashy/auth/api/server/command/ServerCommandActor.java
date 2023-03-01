package com.bivashy.auth.api.server.command;

import com.bivashy.auth.api.server.message.ServerComponent;

public interface ServerCommandActor {
    void reply(ServerComponent component);
}
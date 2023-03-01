package com.bivashy.auth.api.config.message.server;

import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.server.message.ServerComponent;

public interface ServerMessages extends Messages<ServerComponent> {
    ComponentDeserializer getDeserializer();
}

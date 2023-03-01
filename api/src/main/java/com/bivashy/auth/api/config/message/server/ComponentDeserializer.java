package com.bivashy.auth.api.config.message.server;

import com.bivashy.auth.api.server.message.ServerComponent;

public interface ComponentDeserializer {
    ServerComponent deserialize(String rawText);
}

package com.bivashy.auth.api.step;

import com.bivashy.auth.api.server.player.ServerPlayer;

public interface MessageableAuthenticationStep {
    void process(ServerPlayer player);
}

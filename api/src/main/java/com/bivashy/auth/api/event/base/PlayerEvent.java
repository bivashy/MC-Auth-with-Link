package com.bivashy.auth.api.event.base;

import com.bivashy.auth.api.server.player.ServerPlayer;

import io.github.revxrsal.eventbus.gen.Index;

public interface PlayerEvent {
    @Index(0)
    ServerPlayer getPlayer();
}

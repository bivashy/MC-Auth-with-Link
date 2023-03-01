package com.bivashy.auth.api.server.title;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.util.Castable;

public abstract class ServerTitle implements Castable<ServerTitle> {
    private static final ServerComponent EMPTY = ServerComponent.fromPlain("");
    protected ServerComponent title = EMPTY, subtitle = EMPTY;
    protected int fadeIn = 10, stay = 60, fadeOut = 10;

    public ServerTitle title(ServerComponent title) {
        this.title = title;
        return this;
    }

    public ServerTitle subtitle(ServerComponent subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public ServerTitle fadeIn(int ticks) {
        this.fadeIn = ticks;
        return this;
    }

    public ServerTitle stay(int ticks) {
        this.stay = ticks;
        return this;
    }

    public ServerTitle fadeOut(int ticks) {
        this.fadeOut = ticks;
        return this;
    }

    public abstract ServerTitle send(ServerPlayer... players);
}

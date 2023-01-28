package me.mastercapexd.auth.proxy.api.title;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public abstract class ProxyTitle implements Castable<ProxyTitle> {
    private static final ProxyComponent EMPTY = ProxyComponent.fromPlain("");
    protected ProxyComponent title = EMPTY, subtitle = EMPTY;
    protected int fadeIn = 10, stay = 60, fadeOut = 10;

    public ProxyTitle title(ProxyComponent title) {
        this.title = title;
        return this;
    }

    public ProxyTitle subtitle(ProxyComponent subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public ProxyTitle fadeIn(int ticks) {
        this.fadeIn = ticks;
        return this;
    }

    public ProxyTitle stay(int ticks) {
        this.stay = ticks;
        return this;
    }

    public ProxyTitle fadeOut(int ticks) {
        this.fadeOut = ticks;
        return this;
    }

    public abstract ProxyTitle send(ProxyPlayer... players);
}

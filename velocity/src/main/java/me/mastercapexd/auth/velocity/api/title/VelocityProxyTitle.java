package me.mastercapexd.auth.velocity.api.title;

import java.time.Duration;

import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import me.mastercapexd.auth.velocity.player.VelocityProxyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class VelocityProxyTitle extends ProxyTitle {
    private static final int MILLIS_PER_TICK = 1000 / 20;
    private Component titleComponent = Component.empty();
    private Component subTitleComponent = Component.empty();

    public VelocityProxyTitle(ProxyComponent title) {
        title(title);
    }

    public VelocityProxyTitle() {
    }

    public void setTitleComponent(Component titleComponent) {
        this.titleComponent = titleComponent;
    }

    public void setSubTitleComponent(Component subTitleComponent) {
        this.subTitleComponent = subTitleComponent;
    }

    @Override
    public ProxyTitle title(ProxyComponent title) {
        super.title(title);
        title.safeAs(VelocityComponent.class).map(VelocityComponent::component).ifPresent(this::setTitleComponent);
        title.safeAs(AdventureProxyComponent.class).map(AdventureProxyComponent::getComponent).ifPresent(this::setTitleComponent);
        return this;
    }

    @Override
    public ProxyTitle subtitle(ProxyComponent subtitle) {
        super.subtitle(subtitle);
        subtitle.safeAs(VelocityComponent.class).map(VelocityComponent::component).ifPresent(this::setSubTitleComponent);
        subtitle.safeAs(AdventureProxyComponent.class).map(AdventureProxyComponent::getComponent).ifPresent(this::setSubTitleComponent);
        return this;
    }

    @Override
    public ProxyTitle fadeIn(int ticks) {
        return super.fadeIn(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ProxyTitle stay(int ticks) {
        return super.stay(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ProxyTitle fadeOut(int ticks) {
        return super.fadeOut(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ProxyTitle send(ProxyPlayer... players) {
        Title createdTitle = Title.title(titleComponent, subTitleComponent,
                Title.Times.of(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut)));
        for (ProxyPlayer player : players)
            player.as(VelocityProxyPlayer.class).getPlayer().showTitle(createdTitle);
        return this;
    }
}

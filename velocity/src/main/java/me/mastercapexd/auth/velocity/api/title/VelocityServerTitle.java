package me.mastercapexd.auth.velocity.api.title;

import java.time.Duration;

import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.title.ServerTitle;

import me.mastercapexd.auth.velocity.player.VelocityServerPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class VelocityServerTitle extends ServerTitle {
    private static final int MILLIS_PER_TICK = 1000 / 20;
    private Component titleComponent = Component.empty();
    private Component subTitleComponent = Component.empty();

    public VelocityServerTitle(ServerComponent title) {
        title(title);
    }

    public VelocityServerTitle() {
    }

    public void setTitleComponent(Component titleComponent) {
        this.titleComponent = titleComponent;
    }

    public void setSubTitleComponent(Component subTitleComponent) {
        this.subTitleComponent = subTitleComponent;
    }

    @Override
    public ServerTitle title(ServerComponent title) {
        super.title(title);
        title.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component).ifPresent(this::setTitleComponent);
        return this;
    }

    @Override
    public ServerTitle subtitle(ServerComponent subtitle) {
        super.subtitle(subtitle);
        subtitle.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component).ifPresent(this::setSubTitleComponent);
        return this;
    }

    @Override
    public ServerTitle fadeIn(int ticks) {
        return super.fadeIn(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ServerTitle stay(int ticks) {
        return super.stay(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ServerTitle fadeOut(int ticks) {
        return super.fadeOut(ticks * MILLIS_PER_TICK);
    }

    @Override
    public ServerTitle send(ServerPlayer... players) {
        Title createdTitle = Title.title(titleComponent, subTitleComponent,
                Title.Times.of(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut)));
        for (ServerPlayer player : players)
            player.as(VelocityServerPlayer.class).getPlayer().showTitle(createdTitle);
        return this;
    }
}

package me.mastercapexd.auth.server.adventure;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.SelfHandledServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class BaseAdventureServerComponent implements SelfHandledServerComponent, AdventureServerComponent {
    private final Component component;

    public BaseAdventureServerComponent(Component component) {
        this.component = component;
    }

    @Override
    public String jsonText() {
        return GsonComponentSerializer.gson().serialize(component);
    }

    @Override
    public String legacyText() {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    @Override
    public String plainText() {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    @Override
    public void send(ServerPlayer player) {
        getAudience(player).sendMessage(component);
    }

    public Component getComponent() {
        return component;
    }

    private Audience getAudience(ServerPlayer player) {
        return AuthPlugin.instance().getCore().getAudience(player);
    }

    @Override
    public Component component() {
        return component;
    }
}

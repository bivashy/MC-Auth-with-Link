package me.mastercapexd.auth.velocity.component;

import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class VelocityComponent implements ServerComponent, AdventureServerComponent {
    private final Component component;

    public VelocityComponent(Component component) {
        this.component = component;
    }

    @Override
    public String jsonText() {
        return GsonComponentSerializer.gson().serialize(component);
    }

    @Override
    public String legacyText() {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    @Override
    public String plainText() {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    @Override
    public Component component() {
        return component;
    }
}

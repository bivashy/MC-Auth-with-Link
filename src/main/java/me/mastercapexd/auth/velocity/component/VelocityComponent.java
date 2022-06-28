package me.mastercapexd.auth.velocity.component;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class VelocityComponent implements ProxyComponent {
    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('&').hexColors().build();
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
        return LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    @Override
    public String plainText() {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}

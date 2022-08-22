package me.mastercapexd.auth.proxy.adventure;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.SelfHandledProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class AdventureProxyComponent implements SelfHandledProxyComponent {
    private final Component component;

    public AdventureProxyComponent(Component component) {
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
    public void send(ProxyPlayer player) {
        getAudience(player).sendMessage(component);
    }

    public Component getComponent() {
        return component;
    }

    private Audience getAudience(ProxyPlayer player){
        return ProxyPlugin.instance().getCore().getAudience(player);
    }
}

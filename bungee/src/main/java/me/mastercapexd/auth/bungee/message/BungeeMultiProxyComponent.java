package me.mastercapexd.auth.bungee.message;

import java.util.Arrays;

import me.mastercapexd.auth.proxy.message.MultiProxyComponent;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeMultiProxyComponent implements MultiProxyComponent, BungeeComponent {
    private BaseComponent[] components;

    public BungeeMultiProxyComponent(BaseComponent[] components) {
        this.components = components;
    }

    public BungeeMultiProxyComponent(String legacyText) {
        this(TextComponent.fromLegacyText(BungeeComponent.colorText(legacyText)));
    }

    @Override
    public String jsonText() {
        return ComponentSerializer.toString(components);
    }

    @Override
    public String legacyText() {
        return TextComponent.toLegacyText(components);
    }

    @Override
    public String plainText() {
        return ChatColor.stripColor(legacyText());
    }

    @Override
    public ProxyComponent[] getComponents() {
        return Arrays.stream(components).map(BungeeProxyComponent::new).toArray(ProxyComponent[]::new);
    }

    @Override
    public BaseComponent[] components() {
        return components;
    }
}

package me.mastercapexd.auth.bungee.message;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeProxyComponent implements ProxyComponent {
    protected BaseComponent component;

    protected BungeeProxyComponent() {
    }

    public BungeeProxyComponent(BaseComponent component) {
        this.component = component;
    }

    @Override
    public String jsonText() {
        return ComponentSerializer.toString(component);
    }

    @Override
    public String legacyText() {
        return TextComponent.toLegacyText(component);
    }

    @Override
    public String plainText() {
        return component.toPlainText();
    }

    public BaseComponent component() {
        return component;
    }
}

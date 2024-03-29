package me.mastercapexd.auth.bungee.message;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeServerComponent implements BungeeComponent {
    private BaseComponent[] components;

    public BungeeServerComponent(BaseComponent[] components) {
        this.components = components;
    }

    public BungeeServerComponent(String legacyText) {
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
    public BaseComponent[] components() {
        return components;
    }
}

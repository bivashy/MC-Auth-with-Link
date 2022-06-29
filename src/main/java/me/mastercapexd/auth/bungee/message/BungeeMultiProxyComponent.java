package me.mastercapexd.auth.bungee.message;

import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mastercapexd.auth.proxy.message.MultiProxyComponent;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeMultiProxyComponent implements MultiProxyComponent, BungeeComponent {
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");
    private BaseComponent[] components;

    public BungeeMultiProxyComponent(BaseComponent[] components) {
        this.components = components;
    }

    public BungeeMultiProxyComponent(String legacyText) {
        this(TextComponent.fromLegacyText(colorText(legacyText)));
    }

    private static String colorText(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while(matcher.find()) {
            final ChatColor hexColor = ChatColor.of(Color.decode(matcher.group()));
            final String before = message.substring(0, matcher.start());
            final String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
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

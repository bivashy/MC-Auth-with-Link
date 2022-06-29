package me.mastercapexd.auth.bungee.message;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public interface BungeeComponent extends ProxyComponent {
    Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");

    BaseComponent[] components();

    static String colorText(String message) {
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
}

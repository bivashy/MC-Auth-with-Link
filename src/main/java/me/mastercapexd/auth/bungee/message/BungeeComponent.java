package me.mastercapexd.auth.bungee.message;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.chat.BaseComponent;

public interface BungeeComponent extends ProxyComponent {
    BaseComponent[] components();
}

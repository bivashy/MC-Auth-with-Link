package me.mastercapexd.auth.proxy.message;

import net.kyori.adventure.text.TranslatableComponent;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Component that have many other components, for example
 * {@link TranslatableComponent}, or {@link BaseComponent} array.
 * 
 * @author U61vashka
 *
 */
public interface MultiProxyComponent extends ProxyComponent {
	ProxyComponent[] getComponents();
}

package me.mastercapexd.auth.proxy.message;

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

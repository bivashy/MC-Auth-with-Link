package me.mastercapexd.auth.proxy.message;

/**
 * Component that have many other components, for example
 * TranslatableComponent, or BaseComponent array.
 *
 * @author U61vashka
 */
public interface MultiProxyComponent extends ProxyComponent {
    ProxyComponent[] getComponents();
}

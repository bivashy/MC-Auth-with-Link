package me.mastercapexd.auth.proxy.message;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public interface ProxyComponent extends Castable<ProxyComponent> {
    static ProxyComponent fromPlain(String text) {
        return ProxyPlugin.instance().getCore().componentPlain(text);
    }

    static ProxyComponent fromJson(String text) {
        return ProxyPlugin.instance().getCore().componentJson(text);
    }

    static ProxyComponent fromLegacy(String text) {
        return ProxyPlugin.instance().getCore().componentLegacy(text);
    }

    /**
     * Returns component as raw type, return type depends on platform, but default
     * is json
     *
     * @return component converted to raw type
     */
    String jsonText();

    /**
     * Returns text with color codes, and hex (if possibly)
     *
     * @return
     */
    String legacyText();

    /**
     * Returns plain text of component. Removes all color codes and other things.
     *
     * @return
     */
    String plainText();
}

package com.bivashy.auth.api.server.message;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.util.Castable;

public interface ServerComponent extends Castable<ServerComponent> {
    static ServerComponent fromPlain(String text) {
        return AuthPlugin.instance().getCore().componentPlain(text);
    }

    static ServerComponent fromJson(String text) {
        return AuthPlugin.instance().getCore().componentJson(text);
    }

    static ServerComponent fromLegacy(String text) {
        return AuthPlugin.instance().getCore().componentLegacy(text);
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
     * @return legacy format text, like replacing &c with colors
     */
    String legacyText();

    /**
     * Returns plain text of component. Removes all color codes and other things.
     *
     * @return plain text, without any customization
     */
    String plainText();
}

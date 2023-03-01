package com.bivashy.auth.api;

public class AuthPluginProvider {
    private static AuthPlugin pluginInstance;

    private AuthPluginProvider() {
    }

    public static AuthPlugin getPluginInstance() {
        return pluginInstance;
    }

    public static void setPluginInstance(AuthPlugin instance) {
        if (pluginInstance != null)
            throw new UnsupportedOperationException("Cannot set plugin instance twice");
        pluginInstance = instance;
    }
}

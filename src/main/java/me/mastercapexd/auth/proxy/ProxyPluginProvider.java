package me.mastercapexd.auth.proxy;

public class ProxyPluginProvider {
    private static ProxyPlugin pluginInstance;

    private ProxyPluginProvider() {
    }

    public static ProxyPlugin getPluginInstance() {
        return pluginInstance;
    }

    public static void setPluginInstance(ProxyPlugin instance) {
        if (pluginInstance != null)
            throw new UnsupportedOperationException("Cannot set plugin instance twice");
        pluginInstance = instance;
    }
}

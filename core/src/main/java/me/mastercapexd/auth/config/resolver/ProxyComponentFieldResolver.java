package me.mastercapexd.auth.config.resolver;

import java.util.Arrays;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.configuration.context.ConfigurationFieldResolverContext;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;
import com.bivashy.configuration.resolver.field.ConfigurationFieldResolver;

public class ProxyComponentFieldResolver implements ConfigurationFieldResolver<ServerComponent> {
    @Override
    public ServerComponent resolveField(ConfigurationFieldResolverContext context) {
        AuthPlugin proxyPlugin = AuthPlugin.instance();
        if (context.isSection()) {
            ConfigurationSectionHolder sectionHolder = context.getSection();
            String componentType = sectionHolder.getString("type");
            switch (componentType) {
                case "json":
                    return proxyPlugin.getCore().componentJson(sectionHolder.getString("value"));
                case "legacy":
                    return proxyPlugin.getCore().componentLegacy(sectionHolder.getString("value"));
                case "plain":
                    return proxyPlugin.getCore().componentPlain(sectionHolder.getString("value"));
                default:
                    throw new IllegalArgumentException(
                            "Illegal component type in " + Arrays.toString(context.path()) + ":" + componentType + ", available: json,legacy,plain");
            }
        }
        return proxyPlugin.getCore()
                .componentLegacy(context.getString());
    }
}

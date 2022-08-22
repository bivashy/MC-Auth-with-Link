package me.mastercapexd.auth.config.resolver;

import java.util.Arrays;

import com.ubivashka.configuration.context.ConfigurationFieldResolverContext;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;
import com.ubivashka.configuration.resolver.field.ConfigurationFieldResolver;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class ProxyComponentFieldResolver implements ConfigurationFieldResolver<ProxyComponent> {
    @Override
    public ProxyComponent resolveField(ConfigurationFieldResolverContext context) {
        ProxyPlugin proxyPlugin = ProxyPlugin.instance();
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

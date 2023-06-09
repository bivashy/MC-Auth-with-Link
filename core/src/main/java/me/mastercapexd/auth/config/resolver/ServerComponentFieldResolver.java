package me.mastercapexd.auth.config.resolver;

import java.util.Arrays;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.configuration.context.ConfigurationFieldResolverContext;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;
import com.bivashy.configuration.resolver.field.ConfigurationFieldResolver;

public class ServerComponentFieldResolver implements ConfigurationFieldResolver<ServerComponent> {
    @Override
    public ServerComponent resolveField(ConfigurationFieldResolverContext context) {
        if (context.isSection()) {
            ConfigurationSectionHolder sectionHolder = context.getSection();
            String componentType = sectionHolder.getString("type");
            switch (componentType) {
                case "json":
                    return ServerComponent.fromJson(sectionHolder.getString("value"));
                case "legacy":
                    return ServerComponent.fromLegacy(sectionHolder.getString("value"));
                case "plain":
                    return ServerComponent.fromPlain(sectionHolder.getString("value"));
                default:
                    throw new IllegalArgumentException(
                            "Illegal component type in " + Arrays.toString(context.path()) + ":" + componentType + ", available: json,legacy,plain");
            }
        }
        return ServerComponent.fromLegacy(context.getString());
    }
}

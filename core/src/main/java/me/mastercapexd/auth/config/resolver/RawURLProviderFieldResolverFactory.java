package me.mastercapexd.auth.config.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ubivashka.configuration.context.ConfigurationFieldFactoryContext;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;
import com.ubivashka.configuration.resolver.field.ConfigurationFieldResolver;
import com.ubivashka.configuration.resolver.field.ConfigurationFieldResolverFactory;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class RawURLProviderFieldResolverFactory implements ConfigurationFieldResolverFactory {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private static final String REMOTE_URI_TEMPLATE = "jdbc:%s://%s:%s/%s";
    private static final String PLUGIN_FOLDER_PLACEHOLDER_KEY = "%plugin_folder%";
    public static final String DATABASE_TYPE_KEY = "type";
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";
    public static final String DATABASE_KEY = "database";

    @Override
    public ConfigurationFieldResolver<?> createResolver(ConfigurationFieldFactoryContext context) {
        if (context.hasAnnotation(SqlConnectionUrl.class) && context.isSection())
            return (ignored) -> {
                ConfigurationSectionHolder section = context.getSection();
                return RawURLProvider.of(String.format(REMOTE_URI_TEMPLATE, section.getString(DATABASE_TYPE_KEY), section.getString(HOST_KEY),
                                section.getString(PORT_KEY), section.getString(DATABASE_KEY))
                        .replace(PLUGIN_FOLDER_PLACEHOLDER_KEY, PLUGIN.getFolder().getAbsolutePath()));
            };
        return (ignored) -> RawURLProvider.of(context.getString().replace(PLUGIN_FOLDER_PLACEHOLDER_KEY, PLUGIN.getFolder().getAbsolutePath()));
    }

    public interface RawURLProvider {
        String url();

        static RawURLProvider of(String url) {
            return () -> url;
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SqlConnectionUrl {
    }
}

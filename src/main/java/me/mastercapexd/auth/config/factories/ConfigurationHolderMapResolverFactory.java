package me.mastercapexd.auth.config.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ubivashka.configuration.contexts.ConfigurationFieldContext;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import com.ubivashka.configuration.resolvers.ConfigurationFieldResolver;
import com.ubivashka.configuration.resolvers.ConfigurationFieldResolverFactory;

public class ConfigurationHolderMapResolverFactory implements ConfigurationFieldResolverFactory {

    @Override
    public ConfigurationFieldResolver<?> createResolver(ConfigurationFieldContext factoryContext) {
        return (context) -> {
            Map<String, Object> map = context.configuration().getKeys().stream().collect(Collectors.toMap(Function.identity(), (key) -> {
                ConfigurationSectionHolder sectionHolder = context.configuration().getSection(key);
                return newConfigurationHolder(context.getGeneric(0), sectionHolder);
            }));
            return new ConfigurationHolderMap<>(map);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> T newConfigurationHolder(Class<T> clazz, ConfigurationSectionHolder configurationSectionHolder) {
        try {
            Constructor<?> constructor = clazz.getConstructor(ConfigurationSectionHolder.class);
            try {
                return (T) constructor.newInstance(configurationSectionHolder);
            } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;

        } catch(NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ConfigurationHolderMap<V> extends HashMap<String, V> {
        public ConfigurationHolderMap() {
        }

        public ConfigurationHolderMap(Map<String, V> map) {
            super(map);
        }
    }
}

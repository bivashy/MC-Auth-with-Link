package me.mastercapexd.auth.config.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ubivashka.configuration.context.ConfigurationFieldFactoryContext;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;
import com.ubivashka.configuration.resolver.field.ConfigurationFieldResolver;
import com.ubivashka.configuration.resolver.field.ConfigurationFieldResolverFactory;

public class ConfigurationHolderMapResolverFactory implements ConfigurationFieldResolverFactory {

    @Override
    public ConfigurationFieldResolver<?> createResolver(ConfigurationFieldFactoryContext factoryContext) {
        return (context) -> {
            ConfigurationSectionHolder rootSectionHolder = context.path()[0].equals("self") ? context.configuration() : context.getSection();
            Map<String, Object> map =
                    rootSectionHolder.keys().stream().collect(Collectors.toMap(Function.identity(), (key) -> {
                        ConfigurationSectionHolder sectionHolder = rootSectionHolder.section(key);
                        return newConfigurationHolder(context.getGeneric(0), key, sectionHolder);
                    }));
            return new ConfigurationHolderMap<>(map);
        };
    }

    private Object newConfigurationHolder(Class<?> clazz, String key,
                                          ConfigurationSectionHolder configurationSectionHolder) {
        try {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (ConfigurationSectionHolder.class.isAssignableFrom(constructor.getParameterTypes()[0]))
                    return constructor.newInstance(configurationSectionHolder);

                if (constructor.getParameterCount() == 2 && String.class == constructor.getParameterTypes()[0] &&
                        ConfigurationSectionHolder.class.isAssignableFrom(constructor.getParameterTypes()[1]))
                    return constructor.newInstance(key, configurationSectionHolder);
            }
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Cannot create class " + clazz.getSimpleName() + " because it doesn`t have valid constructor");
    }

    public static class ConfigurationHolderMap<V> extends HashMap<String, V> {
        public ConfigurationHolderMap() {
        }

        public ConfigurationHolderMap(Map<String, V> map) {
            super(map);
        }
    }
}
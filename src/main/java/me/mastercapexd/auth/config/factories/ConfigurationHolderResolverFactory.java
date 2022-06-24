package me.mastercapexd.auth.config.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.ubivashka.configuration.contexts.ConfigurationFieldContext;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import com.ubivashka.configuration.resolvers.ConfigurationFieldResolver;
import com.ubivashka.configuration.resolvers.ConfigurationFieldResolverFactory;

import me.mastercapexd.auth.config.ConfigurationHolder;

public class ConfigurationHolderResolverFactory implements ConfigurationFieldResolverFactory {

    @Override
    public ConfigurationFieldResolver<?> createResolver(ConfigurationFieldContext factoryContext) {
        if (factoryContext.isValueCollection())
            throw new UnsupportedOperationException("Collection unsupported for ConfigurationHolder");
        Class<?> fieldClass = factoryContext.valueType();
        try {
            Constructor<?> constructor = fieldClass.getConstructor(ConfigurationSectionHolder.class);
            return (context) -> {
                try {
                    return (ConfigurationHolder) constructor.newInstance(context.configuration().getSection(context.path()));
                } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            };
        } catch(NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;

    }
}

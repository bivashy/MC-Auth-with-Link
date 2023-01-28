package me.mastercapexd.auth.config.message.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.adventure.ComponentDeserializer;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.message.SelfHandledProxyComponent;

public class ProxyMessages extends AbstractMessages<ProxyComponent> {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private static final Pattern INNER_DESERIALIZER_PATTERN = Pattern.compile("\\[([^]\\s]+)]");
    private final Map<String, ProxyComponent> componentCachedMessages = new HashMap<>();
    @ConfigField("deserializer")
    private ComponentDeserializer deserializer = ComponentDeserializer.LEGACY_AMPERSAND;

    public ProxyMessages(ConfigurationSectionHolder configurationSection) {
        super(DEFAULT_DELIMITER, null);
        PLUGIN.getConfigurationProcessor().resolve(configurationSection, this);
        initializeMessages(configurationSection);
    }

    public ProxyMessages(ConfigurationSectionHolder configurationSection, ComponentDeserializer deserializer) {
        super(DEFAULT_DELIMITER, null);
        this.deserializer = deserializer;
        initializeMessages(configurationSection);
    }

    @Override
    public ProxyComponent getMessage(String key, MessageContext context) {
        String message = getStringMessage(key);
        if (message == null)
            return SelfHandledProxyComponent.NULL_COMPONENT;
        return fromText(context.apply(message));
    }

    @Override
    public ProxyComponent getMessage(String key) {
        String message = getStringMessage(key);
        if (message == null)
            return SelfHandledProxyComponent.NULL_COMPONENT;
        if (componentCachedMessages.containsKey(key))
            return componentCachedMessages.get(key);
        ProxyComponent result = fromText(message);
        componentCachedMessages.put(key, result);
        return result;
    }

    @Override
    public ProxyComponent fromText(String text) {
        if (text == null)
            return SelfHandledProxyComponent.NULL_COMPONENT;
        Matcher innerDeserializerMatcher = INNER_DESERIALIZER_PATTERN.matcher(text.trim());
        if (innerDeserializerMatcher.find())
            return new AdventureProxyComponent(ComponentDeserializer.findWithName(innerDeserializerMatcher.group(1))
                    .map(foundDeserializer -> foundDeserializer.deserialize(text.replaceFirst(INNER_DESERIALIZER_PATTERN.pattern(), "")))
                    .orElseGet(() -> deserializer.deserialize(text)));
        return new AdventureProxyComponent(deserializer.deserialize(text));
    }

    @Override
    public String formatString(String message) {
        return PLUGIN.getCore().colorize(message);
    }

    @Override
    protected Messages<ProxyComponent> createMessages(ConfigurationSectionHolder configurationSection) {
        return new ProxyMessages(configurationSection, deserializer);
    }

    public ComponentDeserializer getDeserializer() {
        return deserializer;
    }
}
package me.mastercapexd.auth.config.message.proxy;

import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.adventure.ComponentDeserializer;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class ProxyMessages extends AbstractMessages<ProxyComponent> {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    @ConfigField("deserializer")
    private final ComponentDeserializer deserializer = ComponentDeserializer.LEGACY_AMPERSAND;

    public ProxyMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, DEFAULT_DELIMITER, "");
        ProxyPlugin.instance().getConfigurationProcessor().resolve(configurationSection, this);
    }

    @Override
    public ProxyComponent getMessageNullable(String key) {
        String message = getStringMessage(key, (String) null);
        if (message == null)
            return null;
        return fromText(message);
    }

    @Override
    public ProxyComponent getMessage(String key, MessageContext context) {
        return fromText(context.apply(getStringMessage(key)));
    }

    @Override
    public ProxyComponent fromText(String text) {
        return new AdventureProxyComponent(deserializer.deserialize(text));
    }

    @Override
    public String formatString(String message) {
        return PLUGIN.getCore().colorize(message);
    }

    @Override
    protected Messages<ProxyComponent> createMessages(ConfigurationSectionHolder configurationSection) {
        return new ProxyMessages(configurationSection);
    }

    public ComponentDeserializer getDeserializer() {
        return deserializer;
    }
}
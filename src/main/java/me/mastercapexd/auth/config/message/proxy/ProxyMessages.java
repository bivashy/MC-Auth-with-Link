package me.mastercapexd.auth.config.message.proxy;

import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class ProxyMessages extends AbstractMessages<ProxyComponent> {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    public ProxyMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection, DEFAULT_DELIMITER, "");
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
        return PLUGIN.getCore().componentLegacy(text);
    }

    @Override
    public String formatString(String message) {
        return PLUGIN.getCore().colorize(message);
    }

    @Override
    protected Messages<ProxyComponent> createMessages(ConfigurationSectionHolder configurationSection) {
        return new ProxyMessages(configurationSection);
    }
}
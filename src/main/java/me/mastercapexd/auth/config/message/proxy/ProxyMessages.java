package me.mastercapexd.auth.config.message.proxy;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class ProxyMessages extends AbstractMessages<ProxyComponent> {

    public ProxyMessages(ConfigurationSectionHolder configurationSection) {
        super(configurationSection);
    }

    @Override
    public ProxyComponent getMessageNullable(String key) {
        return ProxyComponent.fromLegacy(getStringMessage(key));
    }

    @Override
    public ProxyComponent getMessage(String key, MessageContext context) {
        return ProxyComponent.fromLegacy(context.apply(getStringMessage(key)));
    }

    @Override
    public String formatString(String message) {
        return ProxyComponent.fromLegacy(message).legacyText();
    }

    @Override
    protected Messages<ProxyComponent> createMessages(ConfigurationSectionHolder configurationSection) {
        return new ProxyMessages(configurationSection);
    }

    @Override
    public ProxyComponent fromText(String text) {
        return ProxyComponent.fromLegacy(text);
    }

}
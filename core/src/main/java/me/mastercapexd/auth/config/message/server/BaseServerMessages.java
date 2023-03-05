package me.mastercapexd.auth.config.message.server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.config.message.server.ServerMessages;
import com.bivashy.auth.api.server.message.SelfHandledServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.MessagesTemplate;
import me.mastercapexd.auth.server.adventure.BaseComponentDeserializer;

public class BaseServerMessages extends MessagesTemplate<ServerComponent> implements ServerMessages {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final Pattern INNER_DESERIALIZER_PATTERN = Pattern.compile("\\[([^]\\s]+)]");
    private final Map<String, ServerComponent> componentCachedMessages = new HashMap<>();
    @ConfigField("deserializer")
    private BaseComponentDeserializer deserializer = BaseComponentDeserializer.LEGACY_AMPERSAND;

    public BaseServerMessages(ConfigurationSectionHolder configurationSection) {
        super(DEFAULT_DELIMITER, null);
        PLUGIN.getConfigurationProcessor().resolve(configurationSection, this);
        initializeMessages(configurationSection);
    }

    public BaseServerMessages(ConfigurationSectionHolder configurationSection, BaseComponentDeserializer deserializer) {
        super(DEFAULT_DELIMITER, null);
        this.deserializer = deserializer;
        initializeMessages(configurationSection);
    }

    @Override
    public ServerComponent getMessage(String key, MessageContext context) {
        String message = getStringMessage(key);
        if (message == null)
            return SelfHandledServerComponent.NULL_COMPONENT;
        return fromText(context.apply(message));
    }

    @Override
    public ServerComponent getMessage(String key) {
        String message = getStringMessage(key);
        if (message == null)
            return SelfHandledServerComponent.NULL_COMPONENT;
        if (componentCachedMessages.containsKey(key))
            return componentCachedMessages.get(key);
        ServerComponent result = fromText(message);
        componentCachedMessages.put(key, result);
        return result;
    }

    @Override
    public ServerComponent fromText(String text) {
        if (text == null)
            return SelfHandledServerComponent.NULL_COMPONENT;
        Matcher innerDeserializerMatcher = INNER_DESERIALIZER_PATTERN.matcher(text.trim());
        if (innerDeserializerMatcher.find())
            return BaseComponentDeserializer.findWithName(innerDeserializerMatcher.group(1))
                    .map(foundDeserializer -> foundDeserializer.deserialize(text.replaceFirst(INNER_DESERIALIZER_PATTERN.pattern(), "")))
                    .orElseGet(() -> deserializer.deserialize(text));
        return deserializer.deserialize(text);
    }

    @Override
    public String formatString(String message) {
        return PLUGIN.getCore().colorize(message);
    }

    @Override
    protected Messages<ServerComponent> createMessages(ConfigurationSectionHolder configurationSection) {
        return new BaseServerMessages(configurationSection, deserializer);
    }

    @Override
    public BaseComponentDeserializer getDeserializer() {
        return deserializer;
    }
}
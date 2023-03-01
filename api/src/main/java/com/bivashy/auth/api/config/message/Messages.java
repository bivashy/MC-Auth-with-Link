package com.bivashy.auth.api.config.message;

public interface Messages<T> {
    String NULL_STRING = null;

    T getMessage(String key, MessageContext context);

    T getMessage(String key);

    String getStringMessage(String key, String defaultValue);

    Messages<T> getSubMessages(String key);

    T fromText(String text);

    default String getStringMessage(String key) {
        return getStringMessage(key, NULL_STRING); // We provide specific type because of ambiguous signature
    }

    default String getStringMessage(String key, MessageContext context) {
        return context.apply(getStringMessage(key));
    }

    default String formatString(String message) {
        return message;
    }
}
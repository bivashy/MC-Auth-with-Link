package com.bivashy.auth.api.config.message;

import com.bivashy.auth.api.util.CollectionUtil.ArrayPairHashMapAdapter;

public interface MessageContext {
    MessageContext EMPTY = rawString -> rawString;

    static MessageContext of(String... placeholders) {
        ArrayPairHashMapAdapter<String> arrayPairMapAdapter = new ArrayPairHashMapAdapter<>(placeholders);
        return rawStringInput -> arrayPairMapAdapter.entrySet()
                .stream()
                .reduce(rawStringInput, (rawString, entry) -> rawString.replace(entry.getKey(), entry.getValue()), (first, second) -> first);
    }

    String apply(String rawString);
}

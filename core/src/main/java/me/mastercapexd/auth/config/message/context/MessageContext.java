package me.mastercapexd.auth.config.message.context;

import me.mastercapexd.auth.utils.CollectionUtils.ArrayPairHashMapAdapter;

public interface MessageContext {
    MessageContext EMPTY = rawString -> rawString;
    static MessageContext of(String... placeholders) {
        ArrayPairHashMapAdapter<String> arrayPairMapAdapter = new ArrayPairHashMapAdapter<>(placeholders);
        return rawStringInput -> arrayPairMapAdapter.entrySet()
                .stream()
                .reduce(rawStringInput, (rawString, entry) -> rawString.replace(entry.getKey(), entry.getValue()), (first, second) -> first);
    }

    static MessageContext empty(){
        return EMPTY;
    }

    String apply(String rawString);
}

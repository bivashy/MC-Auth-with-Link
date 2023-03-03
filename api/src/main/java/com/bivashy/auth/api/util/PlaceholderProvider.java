package com.bivashy.auth.api.util;

import java.util.function.Supplier;

public interface PlaceholderProvider {
    static PlaceholderProvider of(String value, String placeholder) {
        return of(() -> value, placeholder);
    }

    static PlaceholderProvider of(Supplier<String> valueSupplier, String placeholder) {
        return new PlaceholderProvider() {
            @Override
            public String replaceAll(String text) {
                return text.replaceAll(placeholder, valueSupplier.get());
            }

            @Override
            public boolean containsPlaceholder(String text) {
                return text.contains(placeholder);
            }
        };
    }

    static PlaceholderProvider of(String value, String... placeholders) {
        return of(() -> value, placeholders);
    }

    static PlaceholderProvider of(Supplier<String> valueSupplier, String... placeholders) {
        return new PlaceholderProvider() {
            @Override
            public String replaceAll(String text) {
                for (String placeholder : placeholders)
                    text = text.replaceAll(placeholder, valueSupplier.get());
                return text;
            }

            @Override
            public boolean containsPlaceholder(String text) {
                for (String placeholder : placeholders)
                    if (text.contains(placeholder))
                        return true;
                return false;
            }
        };
    }

    boolean containsPlaceholder(String text);

    String replaceAll(String text);
}

package com.bivashy.auth.api.crypto;

public interface HashInput {
    static HashInput of(String rawInput) {
        return () -> rawInput;
    }

    String getRawInput();
}

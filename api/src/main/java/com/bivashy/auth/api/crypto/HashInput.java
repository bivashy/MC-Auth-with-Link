package com.bivashy.auth.api.crypto;

public interface HashInput {
    static HashInput of(String rawInput, int hashIteration) {
        return new HashInput() {
            @Override
            public String getRawInput() {
                return rawInput;
            }

            @Override
            public int getHashIteration() {
                return hashIteration;
            }
        };
    }

    static HashInput of(String rawInput) {
        return of(rawInput, 1);
    }

    String getRawInput();

    int getHashIteration();
}

package com.bivashy.auth.api.crypto;

public interface HashInput {
    static HashInput of(String rawInput, String salt) {
        return new HashInput() {
            @Override
            public String getRawInput() {
                return rawInput;
            }

            @Override
            public String getSalt() {
                return salt;
            }
        };
    }

    static HashInput of(String rawInput) {
        return of(rawInput, null);
    }

    String getRawInput();

    String getSalt();
}

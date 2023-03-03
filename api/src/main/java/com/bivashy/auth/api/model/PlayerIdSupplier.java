package com.bivashy.auth.api.model;

public interface PlayerIdSupplier {
    static PlayerIdSupplier of(String id) {
        return () -> id;
    }

    String getPlayerId();
}

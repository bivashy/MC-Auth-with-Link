package me.mastercapexd.auth.model;

public interface PlayerIdSupplier {
    static PlayerIdSupplier of(String accountPlayerId) {
        return () -> accountPlayerId;
    }

    String getPlayerId();
}

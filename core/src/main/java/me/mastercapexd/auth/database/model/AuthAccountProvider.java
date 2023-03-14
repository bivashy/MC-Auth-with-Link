package me.mastercapexd.auth.database.model;

import java.util.concurrent.CompletableFuture;

public interface AuthAccountProvider {
    AuthAccount getAuthAccount();

    CompletableFuture<Void> syncLinkAdaptersWithLinks();
}

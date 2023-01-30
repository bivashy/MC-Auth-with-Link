package me.mastercapexd.auth.storage.model;

import java.util.concurrent.CompletableFuture;

public interface AuthAccountProvider {
    AuthAccount getAuthAccount();

    CompletableFuture<Void> syncLinkAdaptersWithLinks();
}

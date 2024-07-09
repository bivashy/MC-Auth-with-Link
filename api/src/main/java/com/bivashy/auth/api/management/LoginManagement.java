package com.bivashy.auth.api.management;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.server.player.ServerPlayer;

/**
 * Class that manages player login. For example send player to the server if he has session. Or block if player trying to join multiple accounts with
 * same ip. Or remove
 */
public interface LoginManagement {
    /**
     * Handle player pre login. Verify/force premium account.
     *
     * @return should force online mode
     */
    void onPreLogin(String username, Consumer<Boolean> continueConnection);

    /**
     * Handle player join. Start authentication/registration/session process.
     * On BungeeCord this will use LoginEvent and player from "connection".
     *
     * @return future of account that was taken from database, may be null
     */
    CompletableFuture<Account> onLogin(ServerPlayer player);

    /**
     * Handle player leave. Remove him from caching or modify player on database (Save player quit event for example).
     */
    void onDisconnect(ServerPlayer player);
}

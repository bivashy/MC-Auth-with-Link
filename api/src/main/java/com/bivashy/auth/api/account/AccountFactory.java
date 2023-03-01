package com.bivashy.auth.api.account;

import java.util.UUID;

import com.bivashy.auth.api.type.HashType;
import com.bivashy.auth.api.type.IdentifierType;

public interface AccountFactory {
    Boolean DEFAULT_TELEGRAM_CONFIRMATION_STATE = true;
    Boolean DEFAULT_VK_CONFIRMATION_STATE = true;
    String DEFAULT_PASSWORD = null;
    String DEFAULT_GOOGLE_KEY = null;
    String DEFAULT_IP = null;
    Integer DEFAULT_LAST_QUIT = 0;
    Integer DEFAULT_LAST_SESSION_START = 0;
    Long DEFAULT_TELEGRAM_ID = (long) -1;
    Integer DEFAULT_VK_ID = -1;

    Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType, String password, String googleKey, int vkID,
            boolean vkConfirmationEnabled, long telegramId, boolean telegramConfirmationEnabled, long lastQuit, String lastIp, long lastSessionStart,
            long sessionTime);

    default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType, long sessionTime) {
        return createAccount(id, identifierType, uuid, name, hashType, DEFAULT_PASSWORD, DEFAULT_GOOGLE_KEY, DEFAULT_VK_ID, DEFAULT_VK_CONFIRMATION_STATE,
                DEFAULT_TELEGRAM_ID, DEFAULT_TELEGRAM_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START, sessionTime);
    }
}
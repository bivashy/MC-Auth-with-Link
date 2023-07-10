package com.bivashy.auth.api.account;

import java.util.UUID;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.type.IdentifierType;

public interface AccountFactory {
    long DEFAULT_TELEGRAM_ID = -1;
    int DEFAULT_VK_ID = -1;

    Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, CryptoProvider cryptoProvider, String passwordHash, String lastIp);
}
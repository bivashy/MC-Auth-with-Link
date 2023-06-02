package me.mastercapexd.auth.database.model;

import java.util.UUID;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.type.IdentifierType;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import me.mastercapexd.auth.database.persister.CryptoProviderPersister;

@DatabaseTable(tableName = "mc_auth_accounts")
public class AuthAccount {
    public static final String PLAYER_ID_FIELD_KEY = "player_id";
    public static final String UNIQUE_ID_FIELD_KEY = "unique_id";
    public static final String PLAYER_NAME_FIELD_KEY = "player_name";
    public static final String PASSWORD_HASH_FIELD_KEY = "password_hash";
    public static final String LAST_QUIT_TIMESTAMP_FIELD_KEY = "last_quit";
    public static final String LAST_IP_FIELD_KEY = "last_ip";
    public static final String LAST_SESSION_TIMESTAMP_START_FIELD_KEY = "last_session_start";
    public static final String PLAYER_ID_TYPE_FIELD_KEY = "player_id_type";
    public static final String HASH_TYPE_FIELD_KEY = "hash_type";
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = PLAYER_ID_FIELD_KEY, unique = true, canBeNull = false)
    private String playerId;
    @DatabaseField(columnName = PLAYER_ID_TYPE_FIELD_KEY, canBeNull = false, dataType = DataType.ENUM_NAME)
    private IdentifierType playerIdType;
    @DatabaseField(columnName = HASH_TYPE_FIELD_KEY, canBeNull = false, persisterClass = CryptoProviderPersister.class)
    private CryptoProvider cryptoProvider;
    @DatabaseField(columnName = LAST_IP_FIELD_KEY)
    private String lastIp;
    @DatabaseField(columnName = UNIQUE_ID_FIELD_KEY, canBeNull = false, dataType = DataType.UUID)
    private UUID uniqueId;
    @DatabaseField(columnName = PLAYER_NAME_FIELD_KEY, canBeNull = false)
    private String playerName;
    @DatabaseField(columnName = PASSWORD_HASH_FIELD_KEY)
    private String passwordHash;
    @DatabaseField(columnName = LAST_QUIT_TIMESTAMP_FIELD_KEY, dataType = DataType.LONG)
    private long lastQuitTimestamp;
    @DatabaseField(columnName = LAST_SESSION_TIMESTAMP_START_FIELD_KEY, dataType = DataType.LONG)
    private long lastSessionStartTimestamp;
    @ForeignCollectionField
    private ForeignCollection<AccountLink> links;

    AuthAccount() {
    }

    public AuthAccount(String playerId, IdentifierType playerIdType, String playerName, UUID uniqueId) {
        this.playerId = playerId;
        this.playerIdType = playerIdType;
        this.playerName = playerName;
        this.uniqueId = uniqueId;
    }

    public AuthAccount(String playerId, IdentifierType playerIdType, CryptoProvider cryptoProvider, String lastIp, UUID uniqueId, String playerName,
                       String passwordHash,
                       long lastQuitTimestamp, long lastSessionStartTimestamp) {
        this.playerId = playerId;
        this.playerIdType = playerIdType;
        this.cryptoProvider = cryptoProvider;
        this.lastIp = lastIp;
        this.uniqueId = uniqueId;
        this.playerName = playerName;
        this.passwordHash = passwordHash;
        this.lastQuitTimestamp = lastQuitTimestamp;
        this.lastSessionStartTimestamp = lastSessionStartTimestamp;
    }

    public long getId() {
        return id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public IdentifierType getPlayerIdType() {
        return playerIdType;
    }

    public CryptoProvider getHashType() {
        return cryptoProvider;
    }

    public void setHashType(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public long getLastQuitTimestamp() {
        return lastQuitTimestamp;
    }

    public void setLastQuitTimestamp(long lastQuitTimestamp) {
        this.lastQuitTimestamp = lastQuitTimestamp;
    }

    public long getLastSessionStartTimestamp() {
        return lastSessionStartTimestamp;
    }

    public void setLastSessionStartTimestamp(long lastSessionStartTimestamp) {
        this.lastSessionStartTimestamp = lastSessionStartTimestamp;
    }

    public ForeignCollection<AccountLink> getLinks() {
        return links;
    }
}

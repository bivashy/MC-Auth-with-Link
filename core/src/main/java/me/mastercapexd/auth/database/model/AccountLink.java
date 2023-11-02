package me.mastercapexd.auth.database.model;

import java.util.Objects;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "auth_links")
public class AccountLink {

    public static final String LINK_TYPE_FIELD_KEY = "link_type";
    public static final String LINK_USER_ID_FIELD_KEY = "link_user_id";
    public static final String LINK_ENABLED_FIELD_KEY = "link_enabled";
    public static final String ACCOUNT_ID_FIELD_KEY = "account_id";
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = LINK_TYPE_FIELD_KEY, canBeNull = false, uniqueCombo = true)
    private String linkType;
    @DatabaseField(columnName = LINK_USER_ID_FIELD_KEY)
    private String linkUserId;
    @DatabaseField(columnName = LINK_ENABLED_FIELD_KEY, dataType = DataType.BOOLEAN_INTEGER, canBeNull = false, defaultValue = "true")
    private boolean linkEnabled;
    @DatabaseField(foreign = true, columnName = ACCOUNT_ID_FIELD_KEY, uniqueCombo = true)
    private AuthAccount account;

    AccountLink() {
    }

    public AccountLink(long id, String linkType, String linkUserId, boolean linkEnabled, AuthAccount account) {
        this.id = id;
        this.linkType = linkType;
        this.linkUserId = linkUserId;
        this.linkEnabled = linkEnabled;
        this.account = account;
    }

    public AccountLink(String linkType, String linkUserId, boolean linkEnabled, AuthAccount account) {
        this.linkType = linkType;
        this.linkUserId = linkUserId;
        this.linkEnabled = linkEnabled;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getLinkUserId() {
        return linkUserId;
    }

    public boolean isLinkEnabled() {
        return linkEnabled;
    }

    public AuthAccount getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccountLink))
            return false;
        AccountLink that = (AccountLink) o;
        return getId() == that.getId() && isLinkEnabled() == that.isLinkEnabled() && Objects.equals(getLinkType(), that.getLinkType()) &&
                Objects.equals(getLinkUserId(), that.getLinkUserId()) && getAccount().getId() == that.getAccount().getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLinkType(), getLinkUserId(), isLinkEnabled(), getAccount().getId());
    }

}
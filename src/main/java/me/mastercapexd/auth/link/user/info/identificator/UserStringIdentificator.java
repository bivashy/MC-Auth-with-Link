package me.mastercapexd.auth.link.user.info.identificator;

import java.util.Objects;

public class UserStringIdentificator implements LinkUserIdentificator {
    private String userId;

    public UserStringIdentificator(String userId) {
        this.userId = userId;
    }

    @Override
    public String asString() {
        return userId;
    }

    @Override
    public LinkUserIdentificator setString(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!LinkUserIdentificator.class.isAssignableFrom(obj.getClass()))
            return false;
        LinkUserIdentificator other = (LinkUserIdentificator) obj;
        if (other.isNumber())
            return false;
        return Objects.equals(userId, other.asString());
    }
}

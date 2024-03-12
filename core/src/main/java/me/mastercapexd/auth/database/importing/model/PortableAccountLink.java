package me.mastercapexd.auth.database.importing.model;

import com.bivashy.auth.api.link.LinkType;

public final class PortableAccountLink {

    private final LinkType linkType;
    private final String identificator;
    private final PortableAccount account;

    public PortableAccountLink(LinkType linkType, String identificator, PortableAccount account) {
        this.linkType = linkType;
        this.identificator = identificator;
        this.account = account;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public PortableAccount getAccount() {
        return account;
    }

    public String getIdentificator() {
        return identificator;
    }

}
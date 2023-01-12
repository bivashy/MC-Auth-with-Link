package me.mastercapexd.auth.link;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class LinkTypeProvider {
    private final Map<String, LinkType> linkTypeMap = new HashMap<>();

    public static LinkTypeProvider defaultProvider() {
        LinkTypeProvider provider = new LinkTypeProvider();
        provider.putLinkType(GoogleLinkType.getInstance());
        provider.putLinkType(VKLinkType.getInstance());
        provider.putLinkType(TelegramLinkType.getInstance());
        return provider;
    }

    public void putLinkType(LinkType linkType) {
        putLinkType(linkType.getName(), linkType);
    }

    public void putLinkType(String name, LinkType linkType) {
        linkTypeMap.put(name, linkType);
    }

    public Optional<LinkType> getLinkType(String name) {
        return Optional.ofNullable(linkTypeMap.getOrDefault(name, null));
    }
}

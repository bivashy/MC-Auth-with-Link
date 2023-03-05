package me.mastercapexd.auth.link;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.provider.LinkTypeProvider;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class BaseLinkTypeProvider implements LinkTypeProvider {
    private final Map<String, LinkType> linkTypeMap = new HashMap<>();

    public static BaseLinkTypeProvider allLinks() {
        BaseLinkTypeProvider provider = new BaseLinkTypeProvider();
        provider.putLinkType(GoogleLinkType.getInstance());
        provider.putLinkType(VKLinkType.getInstance());
        provider.putLinkType(TelegramLinkType.getInstance());
        return provider;
    }

    @Override
    public void putLinkType(LinkType linkType) {
        putLinkType(linkType.getName(), linkType);
    }

    @Override
    public void putLinkType(String name, LinkType linkType) {
        linkTypeMap.put(name, linkType);
    }

    @Override
    public Optional<LinkType> getLinkType(String name) {
        return Optional.ofNullable(linkTypeMap.getOrDefault(name, null));
    }
}

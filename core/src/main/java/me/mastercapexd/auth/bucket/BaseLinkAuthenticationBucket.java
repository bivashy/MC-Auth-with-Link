package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.bivashy.auth.api.bucket.LinkAuthenticationBucket;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

public class BaseLinkAuthenticationBucket<T extends LinkUser> implements LinkAuthenticationBucket<T> {
    private final List<T> linkUsers = new ArrayList<>();

    public boolean hasLinkUser(Predicate<T> filter) {
        return linkUsers.stream().anyMatch(filter);
    }

    public boolean hasLinkUser(String id, LinkType linkType) {
        return hasLinkUser(linkUser -> linkUser.getAccount().getPlayerId().equals(id) && linkUser.getLinkType().equals(linkType));
    }

    public void addLinkUser(T linkUser) {
        linkUsers.add(linkUser);
    }

    public void removeLinkUsers(Predicate<T> filter) {
        linkUsers.removeIf(filter);
    }

    public void removeLinkUser(String id, LinkType linkType) {
        removeLinkUsers(linkUser -> linkUser.getAccount().getPlayerId().equals(id) && linkUser.getLinkType().equals(linkType));
    }

    public void removeLinkUser(T linkUser) {
        linkUsers.remove(linkUser);
    }

    public Optional<T> findFirstLinkUser(Predicate<T> filter) {
        return linkUsers.stream().filter(filter).findFirst();
    }

    public List<T> getLinkUsers(Predicate<T> filter) {
        return linkUsers.stream().filter(filter).collect(Collectors.toList());
    }

    public T getLinkUser(String id, LinkType linkType) {
        return findFirstLinkUser(linkUser -> linkUser.getAccount().getPlayerId().equals(id) && linkUser.getLinkType().equals(linkType)).orElse(null);
    }
}

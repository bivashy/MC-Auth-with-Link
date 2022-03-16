package me.mastercapexd.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;

public class LinkAuth<T extends LinkUser> {
	private final List<T> linkUsers = Collections.synchronizedList(new ArrayList<>());

	public synchronized boolean hasLinkUser(Predicate<T> filter) {
		return linkUsers.stream().anyMatch(filter);
	}

	public synchronized boolean hasLinkUser(String id, LinkType linkType) {
		return hasLinkUser(
				linkUser -> linkUser.getAccount().getId().equals(id) && linkUser.getLinkType().equals(linkType));
	}

	public synchronized void addLinkUser(T linkUser) {
		linkUsers.add(linkUser);
	}

	public synchronized void removeLinkUsers(Predicate<T> filter) {
		linkUsers.removeIf(filter);
	}

	public synchronized void removeLinkUser(String id, LinkType linkType) {
		removeLinkUsers(
				linkUser -> linkUser.getAccount().getId().equals(id) && linkUser.getLinkType().equals(linkType));
	}
	
	public synchronized void removeLinkUser(T linkUser) {
		linkUsers.remove(linkUser);
	}

	public synchronized Optional<T> findFirstLinkUser(Predicate<T> filter) {
		return linkUsers.stream().filter(filter).findFirst();
	}

	public synchronized List<T> getLinkUsers(Predicate<T> filter) {
		return linkUsers.stream().filter(filter).collect(Collectors.toList());
	}

	public synchronized T getLinkUser(String id, LinkType linkType) {
		return findFirstLinkUser(
				linkUser -> linkUser.getAccount().getId().equals(id) && linkUser.getLinkType().equals(linkType))
						.orElse(null);
	}

}

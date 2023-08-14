package com.bivashy.auth.api.bucket;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Bucket<T> {
    Collection<T> getUnmodifiableRaw();

    Stream<T> stream();

    ModifiableBucket<T> modifiable();

    default Optional<T> findFirst(Predicate<T> predicate) {
        return filter(predicate).findFirst();
    }

    default List<T> find(Predicate<T> predicate) {
        return filter(predicate).collect(Collectors.toList());
    }

    default boolean has(Predicate<T> predicate) {
        return findFirst(predicate).isPresent();
    }

    default Stream<T> filter(Predicate<T> predicate) {
        return getUnmodifiableRaw().stream().filter(predicate);
    }
}

package me.mastercapexd.auth.bucket;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.bivashy.auth.api.bucket.ModifiableBucket;

public class BaseModifiableListBucket<T> implements ModifiableBucket<T> {
    private final List<T> list;

    public BaseModifiableListBucket(List<T> list) {
        this.list = list;
    }

    @Override
    public Collection<T> getUnmodifiableRaw() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public ModifiableBucket<T> modifiable() {
        return this;
    }

    @Override
    public boolean add(T element) {
        return list.add(element);
    }

    @Override
    public boolean remove(T element) {
        return list.remove(element);
    }

    @Override
    public void removeIf(Predicate<T> predicate) {
        list.removeIf(predicate);
    }
}
